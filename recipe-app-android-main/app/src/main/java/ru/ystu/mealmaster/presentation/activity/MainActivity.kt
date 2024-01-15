package ru.ystu.mealmaster.presentation.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.ystu.mealmaster.R

class MainActivity : AppCompatActivity() {
    private var back: ImageView? = null
    private var tittle: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        installSplashScreen().setOnExitAnimationListener {
            run { }
        }

        back = findViewById(R.id.imageView2)
        tittle = findViewById(R.id.tittle)

        tittle!!.text = intent.getStringExtra("tittle")

        back?.setOnClickListener { finish() }
    }
}
