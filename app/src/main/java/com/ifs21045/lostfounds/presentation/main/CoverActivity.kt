package com.ifs21045.lostfounds.presentation.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import android.widget.Button
import com.ifs21045.lostfounds.R
import com.ifs21045.lostfounds.adapter.ViewPagerAdapter.ViewPagerAdapter

class CoverActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cover)

        viewPager = findViewById(R.id.viewPager)
        val images = listOf(
            R.drawable.lf,
            R.drawable.loste,
            R.drawable.lf
        )
        viewPager.adapter = ViewPagerAdapter(images)

        // Optional: Auto-scroll functionality
        val handler = Handler()
        val update = Runnable {
            var currentPage = viewPager.currentItem
            if (currentPage == images.size - 1) {
                currentPage = 0
            } else {
                currentPage++
            }
            viewPager.setCurrentItem(currentPage, true)
        }

        val timer = java.util.Timer()
        timer.schedule(object : java.util.TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 3000, 3000)

        // Handle button click to open MainActivity
        val openButton: Button = findViewById(R.id.openButton)
        openButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
