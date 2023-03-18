package `is`.hi.hbv601g.petraapp.networking

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.petraapp.Entities.DaycareWorker
import `is`.hi.hbv601g.petraapp.R
import java.lang.reflect.Type
import java.util.Base64.Encoder


class NetworkManager private constructor(context: Context) {
    private val AUTH_URL = "https://dev-xzuj3qsd.eu.auth0.com/oauth/token/"
    private val BASE_URL = "https://hbv2-bakendi-production.up.railway.app/api/"
    private val mContext: Context = context.applicationContext
    private var mQueue: RequestQueue? = null

    companion object {
        private var INSTANCE: NetworkManager? = null

        fun getInstance(context: Context): NetworkManager {
            if (INSTANCE == null) {
                INSTANCE = NetworkManager(context)
            }
            return INSTANCE!!
        }
    }

    init {
        mQueue = getRequestQueue()
    }

    private fun getRequestQueue(): RequestQueue {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext)
        }
        return mQueue!!
    }

    // Inner class to hold a reference to the context object
    private inner class ContextHolder(val context: Context)

    // Method to get the context object from the holder
    private fun getContext(): Context {
        return mContextHolder.context
    }

    // Holder for the context object
    private val mContextHolder = ContextHolder(context)

    fun getDCWs(callback: NetworkCallback<List<DaycareWorker>>) {
        val request = object : Utf8StringRequest(
            Method.GET, BASE_URL + "daycareworkers",
            Response.Listener { response ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<DaycareWorker>>(){}.type
                val dcws: List<DaycareWorker> = gson.fromJson(response, listType)
                callback.onSuccess(dcws)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }

    fun getDCW(email: String?, callback: NetworkCallback<DaycareWorker>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("daycareworker")
            .appendPath(email)
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = Gson()
                val dcws = gson.fromJson(response, DaycareWorker::class.java)
                callback.onSuccess(dcws)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }

    fun getToken(email: String, password: String, callback: NetworkCallback<String>) {
        val request = object : Utf8StringRequest(
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
        mQueue?.add(request)
    }
    // Rest of the class implementation goes here
}