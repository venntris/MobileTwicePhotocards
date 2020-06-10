package com.example.twicephotocards

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkConnection {
    private fun checkNetworkConnection(context: Context): Boolean {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo: NetworkInfo? = cm.activeNetworkInfo
        /*
        if(isConnected){
            /*tvIsConnected.setText("Connected "+networkInfo?.typeName)
            tvIsConnected.setBackgroundColor(0xFF7CCC26.toInt())*/ //pole tvIsConnected typu TextView na widoku

        }else{
            /*tvIsConnected.setText("Not Connected!")
            tvIsConnected.setBackgroundColor(0xFFFF0000.toInt())*/ //pole tvIsConnected typu TextView na widoku
        }   */
        return networkInfo?.isConnected ?: false;
    }

    suspend fun getResult(myURL: String?) = Dispatchers.Default{
        val result: String
        httpGet(myURL)
        //return@Default
    }

    suspend fun httpGet(myURL: String?): String {

        val inputStream:InputStream
        val url: URL = URL(myURL)
        val result = withContext(Dispatchers.IO) {
//            val jo = JSONObject()
//            val request = Request(myURL, jo, "GET")
//
//                val resu = request.request().toString()
//                resu

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            setHeader(conn)
            conn.connect()
            inputStream = conn.inputStream
            if (inputStream != null)
                convertInputStreamToString(inputStream)
            else
                "Did not work!"
        }
        return result
    }

    private fun setHeader(conn:HttpURLConnection){
        conn.setRequestProperty("Authorization", "Bearer "+prefs.apiToken)
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (comatible)")
        conn.setRequestProperty("Accept", "application/json")
    }

    private fun convertInputStreamToString(inputStream: InputStream): String {
        val bufferedReader: BufferedReader? = BufferedReader(InputStreamReader(inputStream))
        var line:String? = bufferedReader?.readLine()
        var result:String = ""

        while (line != null) {
            result += line
            line = bufferedReader?.readLine()
        }

        inputStream.close()
        return result
    }


    inner class HTTPAsyncTask: AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String?): String {
            return "0"//httpGet(urls[0])
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            //tvRelult.setText(result)
            return
        }
    }

}