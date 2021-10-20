package com.rsschool.seka.musicplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.seka.musicplayer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlayerFragment()).commitNow()
        }

    }
}