package `is`.hi.hbv601g.petraapp.networking

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.auth0.android.result.UserProfile
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.petraapp.Entities.*
import `is`.hi.hbv601g.petraapp.R
import java.lang.reflect.Type
import java.util.Base64.Encoder


class NetworkManager private constructor(context: Context) {
    private val AUTH_URL = "https://dev-xzuj3qsd.eu.auth0.com/userInfo"
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

    fun getFullDCW(email: String?, callback: NetworkCallback<FullDCW>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("daycareworker")
            .appendPath(email)
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = Gson()
                val dcws = gson.fromJson(response, FullDCW::class.java)
                callback.onSuccess(dcws)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }


    fun getParent(auth0id: String?, callback: NetworkCallback<Parent>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("parent")
            .appendPath(auth0id)
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = Gson()
                val parent = gson.fromJson(response, Parent::class.java)
                callback.onSuccess(parent)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }

    fun getLocations(callback: NetworkCallback<ArrayList<String>>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("locations")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Location>>(){}.type
                val location: List<Location> = gson.fromJson(response, listType)
                val strLocationList: ArrayList<String> = ArrayList()
                location.map { loc -> strLocationList.add(loc.toString()) }
                callback.onSuccess(strLocationList)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }

    fun getUserInfo(token: String, callback: NetworkCallback<UserProfile>) {
        val request = object : Utf8StringRequest(
            Method.GET, AUTH_URL,
            Response.Listener { response ->
                val gson = Gson()
                val userProfile = gson.fromJson(response, UserProfile::class.java)
                callback.onSuccess(userProfile)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }
        }
        mQueue?.add(request)
    }

    fun createChild(auth0id: String?, callback: NetworkCallback<Child>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("createchild")
            .appendPath(auth0id)
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val gson = Gson()
                val child = gson.fromJson(response, Child::class.java)
                callback.onSuccess(child)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }
    // Rest of the class implementation goes here
}