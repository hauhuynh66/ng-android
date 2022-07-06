package com.app.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.app.adapter.EXFragmentAdapter
import com.app.ngn.R
import com.google.android.material.tabs.TabLayout

class FileExplorerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_flx)
        val pager = findViewById<ViewPager>(R.id.pager)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        pager.adapter = EXFragmentAdapter(supportFragmentManager, tabs)
        pager.setPageTransformer(true, ZoomOutPageTransformer())
        tabs.setupWithViewPager(pager)
    }

    companion object {
        class ZoomOutPageTransformer: ViewPager.PageTransformer{
            private val MIN_SCALE = 0.85f
            private val MIN_ALPHA = 0.5f
            override fun transformPage(page: View, position: Float) {
                page.apply {
                    val pageWidth = width
                    val pageHeight = height
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 1 -> {

                            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                            val vertMargin = pageHeight * (1 - scaleFactor) / 2
                            val horzMargin = pageWidth * (1 - scaleFactor) / 2
                            translationX = if (position < 0) {
                                horzMargin - vertMargin / 2
                            } else {
                                horzMargin + vertMargin / 2
                            }

                            scaleX = scaleFactor
                            scaleY = scaleFactor

                            alpha = (MIN_ALPHA +
                                    (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }

        class DepthPageTransFormer : ViewPager.PageTransformer{
            private val MIN_SCALE = 0.75f
            override fun transformPage(page: View, position: Float) {
                page.apply {
                    val pageWidth = width
                    when {
                        position < -1 -> {
                            alpha = 0f
                        }
                        position <= 0 -> {
                            alpha = 1f
                            translationX = 0f
                            scaleX = 1f
                            scaleY = 1f
                        }
                        position <= 1 -> {
                            alpha = 1 - position

                            translationX = pageWidth * -position

                            val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                            scaleX = scaleFactor
                            scaleY = scaleFactor
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        }
    }
}