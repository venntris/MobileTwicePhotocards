package com.example.twicephotocards

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        log_BUTTON.setOnClickListener {

            var nowaAktywnosc: Intent = Intent(applicationContext, logowanie::class.java)
            startActivity(nowaAktywnosc)

        }

        rej_BUTTON.setOnClickListener {

            var nowaAktywnosc2: Intent = Intent(applicationContext, rejestracja::class.java)
            startActivity(nowaAktywnosc2)

        }

        zalogowany.setOnClickListener {

            var nowaAktywnosc3: Intent = Intent(applicationContext, konto::class.java)
            startActivity(nowaAktywnosc3)

        }

    }
}
