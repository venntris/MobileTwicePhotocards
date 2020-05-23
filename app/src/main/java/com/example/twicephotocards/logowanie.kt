package com.example.twicephotocards

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_logowanie.*

class logowanie : AppCompatActivity() {

    private lateinit var baseUrl: String
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var apiToken: String
    private val apiTokenPreferenceString: String = "apiToken"


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_logowanie)
            baseUrl = "http://ct.zobacztu.pl/"

            zalog_BUTTON.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    try {
                        username = TextView_login.text.toString()
                        password = TextView_password2.text.toString()

                        val api: ApiAuthenticationClient =
                            ApiAuthenticationClient(baseUrl, username, password)
                        api.setParameter("email", username)
                        api.setParameter("password", password)
                        api.setUrlResource("/api/login")
                        val execute: AsyncTask<Void, Void, String> =
                            ExecuteNetworkOperation(
                                api,
                                loadingPanel,
                                applicationContext,
                                this@logowanie
                            )
                        execute.execute()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })


            button_end_main.setOnClickListener(object: View.OnClickListener{
                override fun onClick(v: View?) {
                    prefs.apiToken = ""
                    finishAffinity()
                }
            })
        }

        public class ExecuteNetworkOperation(
            private var api: ApiAuthenticationClient,
            private var loadingPanel: ConstraintLayout,
            private var applicationContext: Context,
            private var activity: logowanie
        ) : AsyncTask<Void, Void, String>() {
            private lateinit var isValidCredentials: String

            override fun onPreExecute() {
                super.onPreExecute()

                loadingPanel.visibility = View.VISIBLE
            }

            override fun doInBackground(vararg params: Void?): String {
                prefs.apiToken = ""
                try {
                    isValidCredentials = api.execute()
                    prefs.apiToken = isValidCredentials

                } catch (e: Exception) {
                    e.printStackTrace();
                }
                return "";
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                loadingPanel.visibility = View.GONE
                if (!prefs.apiToken.isNullOrEmpty()) {
                    activity.goToProfile();
                } else {
                    Toast.makeText(applicationContext, "Logowanie nieudane", Toast.LENGTH_LONG).show()
                }
            }
        }

        public fun goToProfile() {
            prefs.userName = username
            prefs.password = password
            prefs.baseUrl = baseUrl

            val intent = Intent(this@logowanie, konto::class.java)
            startActivity(intent)
            TextView_login.setText("")
            TextView_password2.setText("")
        }

    }


