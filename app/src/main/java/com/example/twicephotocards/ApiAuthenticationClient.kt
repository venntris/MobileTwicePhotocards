package com.example.twicephotocards

import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

class ApiAuthenticationClient(
    baseUrl: String,
    login: String,
    password: String
) {
    public  val DATA_ARRAY: String = "data"
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
        if (!baseUrl.substring(baseUrl.length - 1).equals("/")) {
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
    fun executeAndGetToken(): String{
        val api = ApiCommunicator(baseUrl as String)
        setForLogin(api)
        return api.executeAndGetToken()
    }

    fun setForLogin(apiCommunicator: ApiCommunicator){
        apiCommunicator.setUrlResource(urlResource)
        apiCommunicator.setHttpMethod("POST")
        apiCommunicator.setParameter("email", username)
        apiCommunicator.setParameter("password", password)
    }

    @Throws(JSONException::class)
    fun execute(): String {
        val request = Request(baseUrl + urlResource, payloadAsJson,"POST")
        lastResponse = request.request()
        val apiRespJson = JSONObject(lastResponse.toString())
        val successArray = apiRespJson[DATA_ARRAY] as JSONObject
        apiToken = successArray.getString("token")
        return apiToken
    }

    @Throws(JSONException::class)
    fun executeAndGetJSON(): JSONObject {
        val token: String = prefs.apiToken.toString()
        val request = Request(baseUrl + urlResource, payloadAsJson, token)
        lastResponse = request.requestPOST()
        val apiRespJson = JSONObject(lastResponse.toString())
        val successArray = apiRespJson[DATA_ARRAY] as JSONObject
        return successArray
    }


    init {
        setBaseUrl(baseUrl)
        this.username = login
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