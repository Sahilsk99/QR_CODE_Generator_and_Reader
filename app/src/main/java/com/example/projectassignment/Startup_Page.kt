package com.example.projectassignment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity


/// This Page will Going to Load WHen App is Started
class Startup_Page : AppCompatActivity() {

    // Initialize the Image View
    lateinit var splashImage:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup__page)
        val intent = Intent(this,QR_Code::class.java)

        /// This will Play a
        // sound while app loaded
        var mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.initizing_sound)
        mediaPlayer?.start()


        splashImage = findViewById(R.id.splashImage) as ImageView
        splashImage.setOnClickListener({
            startActivity(intent)
        })

        Handler().postDelayed({
            startActivity(intent)
        },5000)

    }
}
