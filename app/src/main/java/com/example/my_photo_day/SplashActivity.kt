package com.example.my_photo_day

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Найдем наш анимируемый элемент
        val logoImageView = findViewById<AppCompatImageView>(R.id.splash_image)

        // Создадим и запустим анимацию
        val anim = ObjectAnimator.ofFloat(logoImageView, "rotationY", 0.0f, 360f)
        anim.interpolator = LinearInterpolator()
        anim.duration = 1500

        // Обработаем событие завершения анимации
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                // Найдем текстовый элемент, который будем анимировать
                val title = findViewById<MaterialTextView>(R.id.title)

                // Создадим анимацию для текстового элемента
                title.alpha = 0f
                title.scaleX = 0.7f
                title.scaleY = 0.7f
                title.animate()
                    .setStartDelay(1500)
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(BounceInterpolator())
                    .setDuration(1000)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}

                        override fun onAnimationEnd(animation: Animator) {
                            // Переходим на MainActivity
                            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                            finish()
                        }

                        override fun onAnimationCancel(animation: Animator) {}

                        override fun onAnimationRepeat(animation: Animator) {}
                    })
            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        anim.start()
    }
}




