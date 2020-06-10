package com.example.twicephotocards

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profil.*
import org.json.JSONObject

class profil : AppCompatActivity() {

    private lateinit var baseUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        baseUrl = "http://ct.zobacztu.pl/"

        try {

            val api: ApiAuthenticationClient =
                ApiAuthenticationClient(baseUrl, "", "")
            api.setUrlResource("/api/users/details")
            val execute: AsyncTask<Void, Void, String> =
                profil.ExecuteNetworkOperation(
                    api,
                    applicationContext,
                    this@profil
                )
            execute.execute()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    public class ExecuteNetworkOperation(
        private var api: ApiAuthenticationClient,
        private var applicationContext: Context,
        private var activity: profil,
        private var error: errorMenager = errorMenager(applicationContext, "", Toast.LENGTH_LONG)
    ) : AsyncTask<Void, Void, String>() {
        private lateinit var userData: JSONObject

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Void?): String {
            try {
                userData = api.executeAndGetJSON()
                val id: String = userData.getString("id")
                val name: String = userData.getString("name")
                val email: String = userData.getString("email")

                    prefs.ID = id
                    prefs.userName = name
                    prefs.email = email



            } catch (e: Exception) {
                e.printStackTrace();
            }
            return "";
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            activity.showProfileValues()
        }

    }
    fun showProfileValues(){
        ID_value.setText(prefs.ID)
        name_value.setText(prefs.userName)
        email_value.setText(prefs.email)
    }
}
