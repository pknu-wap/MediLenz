
#include <android/asset_manager_jni.h>
#include <android/native_window_jni.h>
#include <android/native_window.h>

#include <android/log.h>

#include <jni.h>

#include <string>
#include <vector>

#include <platform.h>
#include <benchmark.h>

#include "yolo.h"

#include "ndkcamera.h"

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#if __ARM_NEON
#include <arm_neon.h>
#endif // __ARM_NEON

static int draw_unsupported(cv::Mat &rgb) {
    const char text[] = "unsupported";

    int baseLine = 0;
    cv::Size label_size = cv::getTextSize(text, cv::FONT_HERSHEY_SIMPLEX, 1.0, 1, &baseLine);

    int y = (rgb.rows - label_size.height) / 2;
    int x = (rgb.cols - label_size.width) / 2;

    cv::rectangle(rgb, cv::Rect(cv::Point(x, y), cv::Size(label_size.width, label_size.height + baseLine)),
                  cv::Scalar(255, 255, 255), -1);

    cv::putText(rgb, text, cv::Point(x, y + label_size.height),
                cv::FONT_HERSHEY_SIMPLEX, 1.0, cv::Scalar(0, 0, 0));

    return 0;
}

static int draw_fps(cv::Mat &rgb) {
    // resolve moving average
    float avg_fps = 0.f;
    {
        static double t0 = 0.f;
        static float fps_history[10] = {0.f};

        double t1 = ncnn::get_current_time();
        if (t0 == 0.f) {
            t0 = t1;
            return 0;
        }

        float fps = 1000.f / (t1 - t0);
        t0 = t1;

        for (int i = 9; i >= 1; i--) {
            fps_history[i] = fps_history[i - 1];
        }
        fps_history[0] = fps;

        if (fps_history[9] == 0.f) {
            return 0;
        }

        for (int i = 0; i < 10; i++) {
            avg_fps += fps_history[i];
        }
        avg_fps /= 10.f;
    }

    char text[32];

    int baseLine = 0;
    cv::Size label_size = cv::getTextSize(text, cv::FONT_HERSHEY_SIMPLEX, 0.5, 1, &baseLine);

    int y = 0;
    int x = rgb.cols - label_size.width;

    cv::rectangle(rgb, cv::Rect(cv::Point(x, y), cv::Size(label_size.width, label_size.height + baseLine)),
                  cv::Scalar(255, 255, 255), -1);

    cv::putText(rgb, text, cv::Point(x, y + label_size.height),
                cv::FONT_HERSHEY_SIMPLEX, 0.5, cv::Scalar(0, 0, 0));

    return 0;
}

static Yolo *g_yolo = 0;
static ncnn::Mutex lock;

class MyNdkCamera : public NdkCameraWindow {
public:
    virtual void on_image_render(cv::Mat &rgb) const;
};

std::vector<Object> detectedObjects;
cv::Mat *currentRgb;

void MyNdkCamera::on_image_render(cv::Mat &rgb) const {
    // nanodet
    {
        ncnn::MutexLockGuard g(lock);
        std::vector<Object> objs;

        if (g_yolo) {
            g_yolo->detect(rgb, objs);
            g_yolo->draw(rgb, objs);

            currentRgb = &rgb;
            detectedObjects = objs;
        } else {
            draw_unsupported(rgb);
        }
    }

    // draw_fps(rgb);
}


// return detectedObjects;
extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_android_mediproject_feature_camera_ai_Yolo_detectedObjects(JNIEnv *env, jobject thiz) {

    if (detectedObjects.size() == 0) {
        return NULL;
    }

    jobjectArray objectArray = env->NewObjectArray(detectedObjects.size(),
                                                   env->FindClass("com/android/mediproject/feature/camera/ai/DetectedObject"), NULL);

    for (int i = 0; i < detectedObjects.size(); i++) {
        const cv::Mat croppedMat = currentRgb->operator()(detectedObjects[i].rect);

        jbyteArray resultImage = env->NewByteArray(croppedMat.total() * 4);
        jbyte *_data = new jbyte[croppedMat.total() * 4];
        memcpy(_data, croppedMat.data, croppedMat.total() * 4);

        env->SetByteArrayRegion(resultImage, 0, croppedMat.total() * 4, _data);

        jclass detectedObjectClass = env->FindClass("com/android/mediproject/feature/camera/ai/DetectedObject");
        jmethodID constructor = env->GetMethodID(detectedObjectClass, "<init>", "([B)V");
        jobject resultEntity = env->NewObject(detectedObjectClass, constructor, resultImage);

        env->SetObjectArrayElement(objectArray, i, resultEntity);
        delete[]_data;
    }


    return objectArray;
}

static MyNdkCamera *g_camera = 0;

extern "C" {

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "JNI_OnLoad");

    g_camera = new MyNdkCamera;

    return JNI_VERSION_1_4;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "JNI_OnUnload");

    {
        ncnn::MutexLockGuard g(lock);

        delete g_yolo;
        g_yolo = 0;
    }

    delete g_camera;
    g_camera = 0;
}


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mediproject_feature_camera_ai_Yolo_loadModel(JNIEnv *env, jobject thiz, jobject assetManager, jint modelid, jint cpugpu) {
    if (modelid < 0 || modelid > 6 || cpugpu < 0 || cpugpu > 1) {
        return JNI_FALSE;
    }

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    const int target_sizes[] =
            {
                    640,
                    640,
            };

    const float mean_vals[][3] =
            {
                    {103.53f, 116.28f, 123.675f},
                    {103.53f, 116.28f, 123.675f},
            };

    const float norm_vals[][3] =
            {
                    {1 / 255.f, 1 / 255.f, 1 / 255.f},
                    {1 / 255.f, 1 / 255.f, 1 / 255.f},
            };

    const char *modeltype = "s";
    int target_size = target_sizes[(int) modelid];
    bool use_gpu = (int) cpugpu == 1;

    // reload
    {
        ncnn::MutexLockGuard g(lock);

        if (use_gpu && ncnn::get_gpu_count() == 0) {
            // no gpu
            delete g_yolo;
            g_yolo = 0;
        } else {
            if (!g_yolo)
                g_yolo = new Yolo;
            g_yolo->load(mgr, modeltype, target_size, mean_vals[(int) modelid], norm_vals[(int) modelid], use_gpu);
        }
    }

    return JNI_TRUE;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mediproject_feature_camera_ai_Yolo_openCamera(JNIEnv *env, jobject thiz, jint facing) {
    g_camera->open((int) facing);

    return JNI_TRUE;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mediproject_feature_camera_ai_Yolo_closeCamera(JNIEnv *env, jobject thiz) {
    g_camera->close();

    return JNI_TRUE;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mediproject_feature_camera_ai_Yolo_setOutputWindow(JNIEnv *env, jobject thiz, jobject surface) {
    ANativeWindow *win = ANativeWindow_fromSurface(env, surface);

    g_camera->set_window(win);

    return JNI_TRUE;
}
}