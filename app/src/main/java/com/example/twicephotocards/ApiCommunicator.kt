package com.example.twicephotocards

import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.util.*

class ApiCommunicator(
    baseUrl: String
) {
            val DATA_ARRAY: String = "data"
    private var baseUrl: String? = null
    private var urlResource: String
    private var httpMethod: String
    private var urlPath: String
    private var apiToken: String
    var lastResponse: String?
        private set
    private var payload: String
    private var parameters: HashMap<String, String>
    private var headerFields: MutableMap<String, List<String>>
    fun setBaseUrl(baseUrl: String): ApiCommunicator {
        this.baseUrl = baseUrl
        if (!baseUrl.substring(baseUrl.length - 1).equals("/")) {
            this.baseUrl += "/"
        }
        return this
    }



    fun setUrlResource(urlResource: String): ApiCommunicator {
        var urlResource = urlResource
        if (urlResource.substring(0, 1) == "/") urlResource = urlResource.substring(1)
        this.urlResource = urlResource
        return this
    }

    fun setUrlPath(urlPath: String): ApiCommunicator {
        this.urlPath = urlPath
        return this
    }

    fun setHttpMethod(httpMethod: String): ApiCommunicator {
        this.httpMethod = httpMethod
        return this
    }

    fun getHeaderFields(): Map<String, List<String>> {
        return headerFields
    }

    fun clearHeaderFields(): ApiCommunicator {
        headerFields.clear()
        return this
    }

    fun setParameters(parameters: HashMap<String, String>): ApiCommunicator {
        this.parameters = parameters
        return this
    }

    fun setParameter(key: String, value: String): ApiCommunicator {
        parameters[key] = value
        return this
    }

    fun clearParameters(): ApiCommunicator {
        parameters.clear()
        return this
    }

    fun removeParameter(key: String): ApiCommunicator {
        parameters.remove(key)
        return this
    }

    fun clearAll(): ApiCommunicator {
        clearParameters()
        clearHeaderFields()
        baseUrl = ""
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
            val jsonParams = JSONObject(parameters as Map<*, *>)
            return Request.encodeParams(jsonParams) as String
        }

    @get:Throws(JSONException::class)
    val payloadAsJson: JSONObject
        get() {
            return Request.getParamsAsJson(parameters)
        }

    @Throws(JSONException::class)
    fun executeAndGetToken(): String {
        val url = baseUrl+urlResource
        apiToken = ""
        try {
            val request = Request(url, payloadAsJson, httpMethod)
            val successArray = JSONObject(
                request.request().toString()
            )[DATA_ARRAY] as JSONObject
            apiToken = successArray.getString("token")
        } catch (e:Exception){
        }
        return apiToken
    }

    @Throws(JSONException::class)
    fun executeAndGetJSON(): JSONObject {
        val token: String = prefs.apiToken.toString()
        var successArray = JSONObject()
        try {
            val request = Request(baseUrl + urlResource, payloadAsJson, token)
            lastResponse = request.requestPOST()
            val apiRespJson = JSONObject(lastResponse.toString())
            successArray = apiRespJson[DATA_ARRAY] as JSONObject
        } catch (e:Exception){
        }
        return successArray
    }

    init {
        setBaseUrl(baseUrl)
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