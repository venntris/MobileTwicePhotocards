package com.example.twicephotocards

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_konto.*
import kotlinx.android.synthetic.main.activity_main.*

class konto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konto)

        profile_BUTTON.setOnClickListener {

            var nowaAktywnosc1b: Intent = Intent(applicationContext, profil::class.java)
            startActivity(nowaAktywnosc1b)

        }

        allcards_BUTTON.setOnClickListener {

            var nowaAktywnosc2b: Intent = Intent(applicationContext, wszystkie::class.java)
            startActivity(nowaAktywnosc2b)

        }

    }
}
