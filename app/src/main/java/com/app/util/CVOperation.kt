package com.app.util

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.Features2d
import org.opencv.features2d.ORB
import org.opencv.features2d.SIFT
import org.opencv.imgproc.Imgproc


class CVOperation {
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

        fun harris(bitmap: Bitmap, k : Double) : Bitmap {
            val ret = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val src = Mat()
            val filtered = Mat()
            val dst = Mat()

            Utils.bitmapToMat(bitmap, src)
            Imgproc.cvtColor(src, filtered, Imgproc.COLOR_BGR2GRAY)

            val blockSize = 2
            val apertureSize = 3

            Imgproc.cornerHarris(filtered, dst, blockSize, apertureSize, k)
            Core.normalize(dst, dst, 0.0, 255.0, Core.NORM_MINMAX)
            Core.convertScaleAbs(dst, dst)

            Utils.matToBitmap(dst, ret)
            return ret
        }

        fun featureMatching(img1 : Bitmap, img2 : Bitmap, distance : Double) : Bitmap{
            val src1 = Mat()
            val src2 = Mat()
            val dst = Mat()
            Utils.bitmapToMat(img1, src1)
            Imgproc.cvtColor(src1, src1, Imgproc.COLOR_BGR2GRAY)

            Utils.bitmapToMat(img2, src2)
            Imgproc.cvtColor(src2, src2, Imgproc.COLOR_BGR2GRAY)

            val orb = ORB.create()
            val kp1 = MatOfKeyPoint()
            val kp2 = MatOfKeyPoint()
            val descriptor1 = Mat()
            val descriptor2 = Mat()
            orb.detectAndCompute(src1, Mat() , kp1, descriptor1)
            orb.detectAndCompute(src2, Mat() , kp2, descriptor2)

            val bf = BFMatcher.create()
            val matches = arrayListOf<MatOfDMatch>()
            val goodMatchList = arrayListOf<DMatch>()

            bf.knnMatch(descriptor1, descriptor2, matches, 2)

            val iterator = matches.iterator()
            while (iterator.hasNext()){
                val m = iterator.next()
                if((m.toArray()[0].distance)/(m.toArray()[1].distance) < distance){
                    goodMatchList.add(m.toArray()[0])
                }
            }

            val goodMatches = MatOfDMatch()
            goodMatches.fromList(goodMatchList)

            Features2d.drawMatches(src1, kp1, src2, kp2, goodMatches, dst)
            val ret = Bitmap.createBitmap(dst.width(), dst.height(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(dst, ret)
            return ret
        }
    }
}