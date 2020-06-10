package com.example.twicephotocards

import android.content.Intent
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_login_BUTTON.setOnClickListener {

            var nowaAktywnosc: Intent = Intent(applicationContext, Logowanie::class.java)
            startActivity(nowaAktywnosc)

        }

        rej_BUTTON.setOnClickListener {

            var nowaAktywnosc2: Intent = Intent(applicationContext, rejestracja::class.java)
            startActivity(nowaAktywnosc2)

        }

    }
}
