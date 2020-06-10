package com.example.twicephotocards

import android.content.Intent
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_konto.*

class konto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konto)

        profile_BUTTON.setOnClickListener {

            var profileActivity: Intent = Intent(applicationContext, profil::class.java)
            startActivity(profileActivity)

        }

        allcards_BUTTON.setOnClickListener {

            var allCardsActivity: Intent = Intent(applicationContext, Wszystkie::class.java)
            startActivity(allCardsActivity)

        }

    }
}
