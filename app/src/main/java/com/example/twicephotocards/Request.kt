package com.example.twicephotocards

import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class Request(
    r_url: String?,
    private var postDataParams: JSONObject,
    private val method: String,
    private var authorizationToken: String = ""
) {
        private var url:URL = URL(r_url)
        private lateinit var conn:HttpURLConnection
        private lateinit var os: OutputStream
        private lateinit var writer: BufferedWriter
        private var responseCode: Int = 0

    companion object ParametersEncoder {
        @Throws(java.lang.Exception::class)
        fun encodeParams(params: JSONObject): String? {
            val result = StringBuilder()
            var first = true
            val itr = params.keys()
            while (itr.hasNext()) {
                val key = itr.next()
                val value = params[key]
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }
            return result.toString()
        }

        @Throws(java.lang.Exception::class)
        fun getParamsAsJson(params:MutableMap<String, String>): JSONObject {
            val payload = JSONObject()
            val iterator: MutableIterator<*> = params.entries.iterator()
            while (iterator.hasNext()) {
                val pair =
                    iterator.next() as Map.Entry<*, *>
                payload.put(pair.key.toString(), pair.value.toString())
                iterator.remove()
            }
            return payload
        }
    }

    fun request(): String? {
        val isWritable: Boolean = ((method.equals("POST")) || (method.equals("PUT")))
        return if(isWritable) requestWritable(method) else requestReadable(method)
    }

    fun requestPOST(): String? {
        return requestWritable("POST")
    }

    fun requestPUT(): String?{
        return requestWritable("PUT")
    }

    fun requestWritable(method: String): String? {
        connect()
        setConnectionOptions(method)
        setAuthorization()
        getWriter()
        write(encodeParams(postDataParams))
        getResponseCode()
        return readResposne()
    }

    fun requestGET(): String? {
        return requestReadable("GET")
    }

    fun requestDELTE(): String?{
        return requestReadable("DELETE")
    }

    fun requestReadable(method: String): String? {
        connect()
        setAuthorization()
        setMethod(method)
        return readResposne()
    }

    fun connect() {
        conn = url.openConnection() as HttpURLConnection
    }

    fun getWriter(){
        os = conn.outputStream
        writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
    }

    fun setConnectionOptions(method:String){
        conn.readTimeout = 3000
        conn.connectTimeout = 3000
        setMethod(method)
        conn.doInput = true
        conn.doOutput = true
        conn.addRequestProperty("Accept", "application/json")
    }

    fun setMethod(method: String){
        conn.requestMethod= method
    }

    fun setAuthorization(){
        if (!authorizationToken.isNullOrEmpty()) conn.addRequestProperty("Authorization", "Bearer " + authorizationToken)
    }

    fun write(dataToWrite:String?){
        writer.write(dataToWrite)
        writer.flush()
        writer.close()
        os.close()
    }

    fun getResponseCode() {
        responseCode = conn.responseCode
    }

    fun readResposne():String?{
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            val `in` = BufferedReader(InputStreamReader(conn.inputStream))
            val sb = StringBuffer("")
            var line: String? = ""
            while (`in`.readLine().also { line = it } != null) {
                sb.append(line)
                //break
            }
            `in`.close()
            return sb.toString()
        }
        return null
    }

}