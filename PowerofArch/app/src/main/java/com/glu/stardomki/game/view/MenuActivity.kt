package com.glu.stardomki.game.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.glu.stardomki.R
import com.glu.stardomki.databinding.ActivityMenuBinding


class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, RowActivity::class.java)
                startActivity(intent)
                finish()
            }, 250)
        }

    }
}

