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

#include <opencv2/highgui.hpp>

#if __ARM_NEON

#include <arm_neon.h>

#endif // __ARM_NEON


static const std::string base64_chars =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        "abcdefghijklmnopqrstuvwxyz"
        "0123456789+/";

static std::string base64_encode(uchar const *bytes_to_encode, unsigned int in_len) {
    std::string ret;

    int i = 0;
    int j = 0;
    unsigned char char_array_3[3];
    unsigned char char_array_4[4];

    while (in_len--) {
        char_array_3[i++] = *(bytes_to_encode++);
        if (i == 3) {
            char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
            char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
            char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
            char_array_4[3] = char_array_3[2] & 0x3f;

            for (i = 0; (i < 4); i++) {
                ret += base64_chars[char_array_4[i]];
            }
            i = 0;
        }
    }

    if (i) {
        for (j = i; j < 3; j++) {
            char_array_3[j] = '\0';
        }

        char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
        char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
        char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
        char_array_4[3] = char_array_3[2] & 0x3f;

        for (j = 0; (j < i + 1); j++) {
            ret += base64_chars[char_array_4[j]];
        }

        while ((i++ < 3)) {
            ret += '=';
        }
    }

    return ret;
}


static std::string mat2str(const cv::Mat &image) {
    int params[3] = {0};
    params[0] = CV_IMWRITE_JPEG_QUALITY;
    params[1] = 100;

    std::vector<uchar> buf;
    bool code = cv::imencode(".jpg", image, buf, std::vector<int>(params, params + 2));
    uchar *result = reinterpret_cast<uchar *> (&buf[0]);

    return base64_encode(result, buf.size());
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

static std::vector<cv::Rect_<float>> *finalDetectedObjects;
static cv::Mat *finalRgb;

static std::vector<Object> detectedObjects;
static cv::Mat *currentRgb;


void MyNdkCamera::on_image_render(cv::Mat &rgb) const {
    {
        ncnn::MutexLockGuard g(lock);
        std::vector<Object> objs;

        if (g_yolo) {
            g_yolo->detect(rgb, objs);
            g_yolo->draw(rgb, objs);

            currentRgb = &rgb;
            detectedObjects = objs;
        } else {

        }
    }

    // draw_fps(rgb);
}

// return detectedObjects;
extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_android_mediproject_feature_camera_aimodel_Yolo_detectedObjects(JNIEnv *env, jobject thiz) {
    if (finalDetectedObjects == nullptr || finalRgb == nullptr) {
        return nullptr;
    }

    jobjectArray objectArray = env->NewObjectArray(finalDetectedObjects->size(),
                                                   env->FindClass("com/android/mediproject/feature/camera/aimodel/DetectedObject"),
                                                   nullptr);

    __android_log_print(ANDROID_LOG_DEBUG, "finalDetectedObjects", "finalDetectedObjects size: %d", finalDetectedObjects->size());

    for (int i = 0; i < finalDetectedObjects->size(); i++) {
        cv::Mat croppedMat = finalRgb->operator()(finalDetectedObjects->at(i));
        cv::Mat croppedMat2;

        cv::cvtColor(croppedMat, croppedMat2, cv::COLOR_BGR2RGB);
        std::string base64String = mat2str(croppedMat2);

        jstring base64StringJ = env->NewStringUTF(base64String.c_str());
        jint width = finalDetectedObjects->at(i).width;
        jint height = finalDetectedObjects->at(i).height;

        jclass detectedObjectClass = env->FindClass("com/android/mediproject/feature/camera/aimodel/DetectedObject");
        jmethodID constructor = env->GetMethodID(detectedObjectClass, "<init>", "(Ljava/lang/String;II)V");
        jobject resultEntity = env->NewObject(detectedObjectClass, constructor, base64StringJ,
                                              width, height);

        env->SetObjectArrayElement(objectArray, i, resultEntity);
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
Java_com_android_mediproject_feature_camera_aimodel_Yolo_loadModel(JNIEnv *env, jobject thiz, jobject assetManager, jint modelid,
                                                                   jint cpugpu) {
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
Java_com_android_mediproject_feature_camera_aimodel_Yolo_openCamera(JNIEnv *env, jobject thiz, jint facing) {
    g_camera->open((int) facing);

    return JNI_TRUE;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mediproject_feature_camera_aimodel_Yolo_closeCamera(JNIEnv *env, jobject thiz) {
    g_camera->close();

    return JNI_TRUE;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_android_mediproject_feature_camera_aimodel_Yolo_setOutputWindow(JNIEnv *env, jobject thiz, jobject surface) {
    ANativeWindow *win = ANativeWindow_fromSurface(env, surface);

    g_camera->set_window(win);

    return JNI_TRUE;
}
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_android_mediproject_feature_camera_aimodel_Yolo_getCurrentImage(JNIEnv *env, jobject thiz) {
    if (currentRgb != nullptr) {
        delete finalRgb;
    }
    if (finalDetectedObjects != nullptr) {
        delete finalDetectedObjects;
    }

    finalRgb = new cv::Mat();
    currentRgb->copyTo(*finalRgb);
    finalDetectedObjects = new std::vector<cv::Rect_<float>>();

    for (int i = 0; i < detectedObjects.size(); i++) {
        finalDetectedObjects->push_back(detectedObjects[i].rect);
    }

    __android_log_print(ANDROID_LOG_DEBUG, "detectedObjects", "detectedObjects size: %d", detectedObjects.size());

    __android_log_print(ANDROID_LOG_DEBUG, "finalDetectedObjects", "finalDetectedObjects size: %d", finalDetectedObjects->size());


    cv::cvtColor(*finalRgb, *finalRgb, cv::COLOR_BGR2RGB);
    std::string base64String = mat2str(*finalRgb);

    jstring base64 = env->NewStringUTF(base64String.c_str());
    jint width = finalRgb->cols;
    jint height = finalRgb->rows;

    jclass detectedObjectClass = env->FindClass("com/android/mediproject/feature/camera/aimodel/DetectedImage");
    jmethodID constructor = env->GetMethodID(detectedObjectClass, "<init>", "(Ljava/lang/String;II)V");
    jobject resultEntity = env->NewObject(detectedObjectClass, constructor, base64,
                                          width, height);

    return resultEntity;
}