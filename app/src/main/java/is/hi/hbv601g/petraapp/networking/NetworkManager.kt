package `is`.hi.hbv601g.petraapp.networking

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.petraapp.R

class NetworkManager private constructor(context: Context) {
    private val mContext: Context = context

    companion object {
        private const val AUTH_URL = "https://dev-xzuj3qsd.eu.auth0.com/oauth/token"

        private lateinit var mInstance: NetworkManager
        private lateinit var mQueue: RequestQueue
        @Synchronized
        fun getInstance(context: Context): NetworkManager {
            mInstance = NetworkManager(context)
            return mInstance
        }
    }


    fun getRequestQueue(): RequestQueue {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext.applicationContext)
        }
        return mQueue
    }

    fun getToken(email: String, password: String, callback: NetworkCallback<String>) {
        val request = object : StringRequest(
            Method.POST, AUTH_URL,
            Response.Listener { response ->
                val gson = Gson()
                val jsonObject = gson.fromJson(response, JsonObject::class.java)
                val accessToken = jsonObject.get("access_token").asString
                callback.onSuccess(accessToken)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = email
                params["password"] = password
                params["client_id"] = mContext.getString(R.string.auth0_client_id)
                params["client_secret"] = mContext.getString(R.string.auth0_client_secret)
                params["audience"] = "https://dev-xzuj3qsd.eu.auth0.com/api/v2/"
                params["grant_type"] = "client_credentials"
                return params
            }
        }
        Volley.newRequestQueue(mContext).add(request)
    }
    // Rest of the class implementation goes here
}