package com.example.twicephotocards

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
//import android.support.constraint.ConstraintLayout
//import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_logowanie.*

class Logowanie : AppCompatActivity() {
    private lateinit var login: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_logowanie)
            zalog_BUTTON.setOnClickListener {
                try {
                    login = getLogin()
                    password = getPassword()
                    val api = ApiAuthenticationClient(prefs.baseUrl as String, login, password)
                    api.setUrlResource("/api/login")
                    val execute: AsyncTask<Void, Void, String> =
                        ExecuteNetworkOperation(
                            api,
                            loadingPanel as ConstraintLayout,
                            applicationContext,
                            this@Logowanie
                        )
                    execute.execute()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        button_end_main.setOnClickListener {
            prefs.apiToken = ""
            finishAffinity()
        }
    }

        fun getLogin(): String {
            return TextView_login.text.toString()
        }

        fun getPassword(): String{
            return TextView_password2.text.toString()
        }

        class ExecuteNetworkOperation(
            private var api: ApiAuthenticationClient,
            private var loadingPanel: ConstraintLayout,
            private var applicationContext: Context,
            private var activity: Logowanie
        ) : AsyncTask<Void, Void, String>() {

            override fun onPreExecute() {
                super.onPreExecute()
                panelShow()
            }

            override fun doInBackground(vararg params: Void?): String {
                prefs.apiToken = ""
                try {
                    prefs.apiToken = api.executeAndGetToken()

                } catch (e: Exception) {
                    e.printStackTrace();
                }
                return "";
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                panelHide()
                checkLogged()
            }

            fun panelShow(){
                loadingPanel.visibility = View.VISIBLE
            }

            fun panelHide(){
                loadingPanel.visibility = View.GONE
            }

            fun checkLogged(){
                if (!prefs.apiToken.isNullOrEmpty()) {
                    activity.goToProfile();
                } else {
                    Toast.makeText(applicationContext, "Logowanie nieudane", Toast.LENGTH_LONG).show()
                    panelShow()
                }
            }
        }

        fun goToProfile() {
            saveCredentials()
            clearFields()
            val intent = Intent(this@Logowanie, konto::class.java)
            startActivity(intent)
        }

        fun saveCredentials() {
            prefs.userName = login
            prefs.password = password
        }

        fun clearFields () {
            TextView_login.setText("")
            TextView_password2.setText("")

        }

    }


