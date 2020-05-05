package com.example.twicephotocards

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_wszystkie.*

class wszystkie : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wszystkie)

        nayeon_BUTTON.setOnClickListener {

            var nowaAktywnosc1c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc1c)

        }

        jeongyeon_BUTTON.setOnClickListener {

            var nowaAktywnosc2c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc2c)

        }

        momo_BUTTON.setOnClickListener {

            var nowaAktywnosc3c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc3c)

        }

        sana_BUTTON.setOnClickListener {

            var nowaAktywnosc4c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc4c)

        }

        jihyo_BUTTON.setOnClickListener {

            var nowaAktywnosc5c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc5c)

        }

        mina_BUTTON.setOnClickListener {

            var nowaAktywnosc6c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc6c)

        }

        dahyun_BUTTON.setOnClickListener {

            var nowaAktywnosc7c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc7c)

        }

        chaeyoung_BUTTON.setOnClickListener {

            var nowaAktywnosc8c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc8c)

        }

        tzuyu_BUTTON.setOnClickListener {

            var nowaAktywnosc9c: Intent = Intent(applicationContext, albumy::class.java)
            startActivity(nowaAktywnosc9c)

        }
    }
}
