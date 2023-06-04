

#ifndef YOLO_H
#define YOLO_H

#include <opencv2/core/core.hpp>

#include <net.h>

struct Object {
    cv::Rect_<float> rect;
    int label;
    float prob;
};
struct GridAndStride {
    int grid0;
    int grid1;
    int stride;
};

class Yolo {
public:
    Yolo();

    int
    load(AAssetManager *mgr, const int *target_size, const float *mean_vals, const float *norm_vals, bool use_gpu = true);

    int detect(const cv::Mat &rgb, std::vector<Object> &objects);

    void draw(cv::Mat &rgb, const std::vector<Object> &objects);

private:
    ncnn::Net yolo;
    float mean_vals[3];
    float norm_vals[3];
    int net_h;
    int net_w;
    ncnn::UnlockedPoolAllocator blob_pool_allocator;
    ncnn::PoolAllocator workspace_pool_allocator;
};

#endif // NANODET_H