package com.example.twicephotocards

import android.os.Build
import android.support.annotation.RequiresApi
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

class ApiAuthenticationClient(
    baseUrl: String,
    username: String,
    password: String
) {
    private var apiToken = ""
    private var baseUrl: String? = null
    private var username: String
    private var password: String
    private var urlResource: String
    private var httpMethod: String
    private var urlPath: String
    var lastResponse: String?
        private set
    private var payload: String
    private var parameters: HashMap<String, String>
    private var headerFields: MutableMap<String, List<String>>
    fun setBaseUrl(baseUrl: String): ApiAuthenticationClient {
        this.baseUrl = baseUrl
        if (baseUrl.substring(baseUrl.length - 1) != "/") {
            this.baseUrl += "/"
        }
        return this
    }

    fun setUrlResource(urlResource: String): ApiAuthenticationClient {
        var urlResource = urlResource
        if (urlResource.substring(0, 1) == "/") urlResource = urlResource.substring(1)
        this.urlResource = urlResource
        return this
    }

    fun setUrlPath(urlPath: String): ApiAuthenticationClient {
        this.urlPath = urlPath
        return this
    }

    fun setHttpMethod(httpMethod: String): ApiAuthenticationClient {
        this.httpMethod = httpMethod
        return this
    }

    fun getHeaderFields(): Map<String, List<String>> {
        return headerFields
    }

    fun clearHeaderFields(): ApiAuthenticationClient {
        headerFields.clear()
        return this
    }

    fun setParameters(parameters: HashMap<String, String>): ApiAuthenticationClient {
        this.parameters = parameters
        return this
    }

    fun setParameter(key: String, value: String): ApiAuthenticationClient {
        parameters[key] = value
        return this
    }

    fun clearParameters(): ApiAuthenticationClient {
        parameters.clear()
        return this
    }

    fun removeParameter(key: String): ApiAuthenticationClient {
        parameters.remove(key)
        return this
    }

    fun clearAll(): ApiAuthenticationClient {
        clearParameters()
        clearHeaderFields()
        baseUrl = ""
        username = ""
        password = ""
        urlPath = ""
        urlResource = ""
        httpMethod = ""
        lastResponse = ""
        apiToken = ""
        payload = ""
        return this
    }

    val lastResponseAsJsonObject: JSONObject?
        get() {
            try {
                return JSONObject(lastResponse.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return null
        }

    @get:Throws(UnsupportedEncodingException::class)
    val payloadAsString: String
        get() {
            val stringBuffer = StringBuilder()
            val iterator: MutableIterator<*> = parameters.entries.iterator()
            var count = 0
            while (iterator.hasNext()) {
                val pair =
                    iterator.next() as Map.Entry<*, *>
                if (count > 0) {
                    stringBuffer.append("&")
                }
                stringBuffer.append(URLEncoder.encode(pair.key.toString(), "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(pair.value.toString(), "UTF-8"))
                iterator.remove()
                count++
            }
            return stringBuffer.toString()
        }

    @get:Throws(JSONException::class)
    val payloadAsJson: JSONObject
        get() {
            val payload = JSONObject()
            val iterator: MutableIterator<*> = parameters.entries.iterator()
            while (iterator.hasNext()) {
                val pair =
                    iterator.next() as Map.Entry<*, *>
                payload.put(pair.key.toString(), pair.value.toString())
                iterator.remove()
            }
            return payload
        }

    @Throws(JSONException::class)
    fun execute(): String {
        val request = Request()
        lastResponse = request.requestPOST(baseUrl + urlResource, payloadAsJson)
        val apiRespJson = JSONObject(lastResponse)
        val successArray = apiRespJson["success"] as JSONObject
        apiToken = successArray.getString("token")
        return apiToken
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun execute2(): String {
        var line: String?
        val outputStringBuilder = StringBuilder()
        try {
            val urlString = StringBuilder(baseUrl + urlResource)
            if (urlPath != "") {
                urlString.append(urlPath)
            }
            if (parameters.size > 0 && httpMethod == "GET") {
                payload = payloadAsString
                urlString.append("?").append(payload)
            }
            val url = URL(urlString.toString())
            //String encoding = Base64.encode((username+":"+password).getBytes(), 0).toString();
            val connection =
                url.openConnection() as HttpURLConnection
            connection.requestMethod = httpMethod
            if (apiToken != "") {
                connection.setRequestProperty("Authorization", "Bearer " + apiToken)
            }
            //connection.setRequestProperty("Accept", "application/json");
            val Boundary = "WebBoundary"
            val LF = "\r\n"
            connection.setRequestProperty("Accept-encoding", "identity")
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.setRequestProperty("Connection", "keep-alive")
            connection.setChunkedStreamingMode(0)
            connection.readTimeout = 30000
            connection.connectTimeout = 30000
            if (httpMethod == "POST" || httpMethod == "PUT") {
                payload = payloadAsString
                connection.setRequestProperty("Content-Length", "" + payload.length)
                /*payload = "--"+Boundary+LF+"Content-Disposition: form-data; name=\"cos\""+LF
                    +"nićątko"+LF+"--"+Boundary+LF+"Content-Disposition: form-data; nasme=\"email\""
                    +LF+"kurwawafel@prl"+LF+"--"+Boundary+"--";*/connection.doInput = true
                connection.doOutput = true
                try {
                    val outputStream: OutputStream =
                        BufferedOutputStream(connection.outputStream)
                    val writer =
                        BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
                    writer.write(payload)
                    writer.flush()
                    writer.close()
                    outputStream.close()
                    headerFields = connection.headerFields
                    val reader =
                        BufferedReader(InputStreamReader(connection.inputStream))
                    while (reader.readLine().also { line = it } != null) {
                        outputStringBuilder.append(line)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                connection.disconnect()
            } else {
                val content =
                    connection.inputStream as InputStream
                headerFields = connection.headerFields
                val reader =
                    BufferedReader(InputStreamReader(content))
                while (reader.readLine().also { line = it } != null) {
                    outputStringBuilder.append(line)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (outputStringBuilder.toString() != "") {
            lastResponse = outputStringBuilder.toString()
        }
        return outputStringBuilder.toString()
    }

    init {
        setBaseUrl(baseUrl)
        this.username = username
        this.password = password
        urlResource = ""
        apiToken = ""
        urlPath = ""
        httpMethod = "POST"
        parameters = HashMap()
        lastResponse = ""
        payload = ""
        headerFields = HashMap()
        System.setProperty("jsse.enableSNIExtension", "false")
    }
}