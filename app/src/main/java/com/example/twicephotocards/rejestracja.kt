package com.example.twicephotocards

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_logowanie.*
import kotlinx.android.synthetic.main.activity_logowanie.TextView_password2
import kotlinx.android.synthetic.main.activity_rejestracja.*

class rejestracja : AppCompatActivity() {

    private lateinit var baseUrl: String
    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var c_password: String
    private lateinit var apiToken: String
    private var policyAccepted: Boolean = false
    private lateinit var error: errorMenager
    private val apiTokenPreferenceString: String = "apiToken"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rejestracja)
        baseUrl = "http://ct.zobacztu.pl/"

        error = errorMenager(applicationContext, "", Toast.LENGTH_LONG)

        zarej_BUTTON.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                try {
                    username = TextView_login2.text.toString()
                    email = TextView_email.text.toString()
                    password = regpassword.text.toString()
                    c_password = TextView_c_password.text.toString()

                    policyAccepted = switch_BUTTON.isChecked
                    if (!policyAccepted){
                        error.setMessage("Zaakceptuj regulamin!").show()
                        return
                    }

                    val isPasswordsEquals: Boolean = password.equals(c_password)
                        if (!isPasswordsEquals){
                            error.setMessage("Hasła nie są jednakowe!").show()
                            return
                        }

                    val api: ApiAuthenticationClient =
                        ApiAuthenticationClient(baseUrl, username, password)
                    api.setParameter("name", username)
                    api.setParameter("email", email)
                    api.setParameter("password", password)
                    api.setParameter("c_password", c_password)
                    api.setUrlResource("/api/users/register")
                    val execute: AsyncTask<Void, Void, String> =
                        ExecuteNetworkOperation(
                            api,
                            applicationContext,
                            this@rejestracja
                        )
                    execute.execute()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        //?? dodać
       /* button_end_main.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                prefs.apiToken = ""
                finishAffinity()
            }
        })*/
    }

    public class ExecuteNetworkOperation(
        private var api: ApiAuthenticationClient,
        private var applicationContext: Context,
        private var activity: rejestracja,
        private var error: errorMenager = errorMenager(applicationContext, "", Toast.LENGTH_LONG)
    ) : AsyncTask<Void, Void, String>() {
        private lateinit var isValidCredentials: String

        override fun onPreExecute() {
            super.onPreExecute()

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
            if (!prefs.apiToken.isNullOrEmpty()) {
                activity.goToLoginPage();
            } else {
                error.setMessage("Rejestracja nieudana!").show()
            }

        }

    }

    public fun goToLoginPage() {
        prefs.userName = username
        prefs.password = password
        prefs.c_password = c_password
        prefs.email = email
        prefs.baseUrl = baseUrl

        val intent = Intent(this@rejestracja, logowanie::class.java)
        startActivity(intent)
        TextView_login2.setText("")
        regpassword.setText("")
        TextView_c_password.setText("")
        TextView_email.setText("")
    }
}


