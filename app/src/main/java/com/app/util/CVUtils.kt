package com.app.util

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfKeyPoint
import org.opencv.core.Size
import org.opencv.features2d.Features2d
import org.opencv.features2d.SIFT
import org.opencv.imgproc.Imgproc


class CVUtils {
    companion object{
        fun gaussianFilter(bitmap: Bitmap) : Bitmap {
            val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val src = Mat()
            Utils.bitmapToMat(bitmap, src)
            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY)
            Imgproc.GaussianBlur(src, src, Size(3.0,3.0), 0.0)
            Imgproc.adaptiveThreshold(src, src, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 5, 4.0)
            Utils.matToBitmap(src, ret)
            return ret
        }

        fun canny(bitmap: Bitmap) : Bitmap {
            val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val src = Mat()
            val dst = Mat()
            val edges = Mat()
            Utils.bitmapToMat(bitmap, src)
            Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY)
            Imgproc.GaussianBlur(src, src, Size(5.0,5.0), 0.0)
            Imgproc.Canny(src, edges, 0.02, 0.1)
            src.copyTo(dst, edges)
            Utils.matToBitmap(dst, ret)
            return ret
        }

        fun sift(bitmap: Bitmap) : Bitmap {
            val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val src = Mat()
            val dst = Mat()
            val filtered = Mat()
            val keypoint = MatOfKeyPoint()
            Utils.bitmapToMat(bitmap, src)
            Imgproc.cvtColor(src, filtered, Imgproc.COLOR_BGR2GRAY)
            val sift = SIFT.create()
            sift.detect(filtered, keypoint)
            Features2d.drawKeypoints(src, keypoint, dst)
            Utils.matToBitmap(dst, ret)
            return ret
        }
    }
}