package `is`.hi.hbv601g.petraapp.networking

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.R

class NetworkManager private constructor(context: Context) {
    private val mContext: Context = context

    companion object {
        private const val AUTH_URL = "https://dev-xzuj3qsd.eu.auth0.com/oauth/token"
        private const val BASE_URL = "https://hbv2-bakendi-production.up.railway.app/api/"

        @SuppressLint("StaticFieldLeak")
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

    fun getDCWs(endpoint: String, callback: NetworkCallback<DaycareWorker>) {
        val request = object : StringRequest(
            Method.GET, BASE_URL+endpoint,
            Response.Listener { response ->
                val gson = Gson()
                val dcws = gson.fromJson(response, DaycareWorker::class.java)
                callback.onSuccess(dcws)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue.add(request)
    }

    fun getDCW(auth0id: String, callback: NetworkCallback<DaycareWorker>) {
        val request = object : StringRequest(
            Method.GET, BASE_URL+"/daycareworker",
            Response.Listener { response ->
                val gson = Gson()
                val dcws = gson.fromJson(response, DaycareWorker::class.java)
                callback.onSuccess(dcws)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue.add(request)
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
        mQueue.add(request)
    }
    // Rest of the class implementation goes here
}