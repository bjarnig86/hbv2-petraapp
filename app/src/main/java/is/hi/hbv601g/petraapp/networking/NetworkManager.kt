package `is`.hi.hbv601g.petraapp.networking

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.auth0.android.result.UserProfile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import `is`.hi.hbv601g.petraapp.DTOs.*
import `is`.hi.hbv601g.petraapp.Entities.*
import `is`.hi.hbv601g.petraapp.utils.LocalDateTimeDeserializer
import `is`.hi.hbv601g.petraapp.utils.LocalDateTimeSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime


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
                Log.d("Engar dcws!", "getDCWs: $response")
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

    fun getChildrenByParent(parentId: Long, callback: NetworkCallback<List<Child>>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("parent")
            .appendPath(parentId.toString())
            .appendPath("children")
            .build().toString()

        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
                    .create()
                val listType: Type = object : TypeToken<List<Child>>(){}.type
                val children: List<Child> = gson.fromJson(response, listType)
                callback.onSuccess(children)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }

    fun getChildrenByDCW(dcwId: String, callback: NetworkCallback<List<Child>>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("daycareworker")
            .appendPath(dcwId)
            .appendPath("children")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Child>>(){}.type
                val children: List<Child> = gson.fromJson(response, listType)
                callback.onSuccess(children)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            }
        ) {}
        mQueue?.add(request)
    }

    fun getUnregisteredChildrenByParent(parentId: Long, callback: NetworkCallback<List<Child>>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("parent")
            .appendPath(parentId.toString())
            .appendPath("unregistered_children")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = Gson()
                val listType: Type = object : TypeToken<List<Child>>(){}.type
                val children: List<Child> = gson.fromJson(response, listType)
                callback.onSuccess(children)
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

    fun getDayReportsByChild(childId: Int, callback: NetworkCallback<ArrayList<DayReportDTO>>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("getdayreports")
            .appendPath(childId.toString())
            .build().toString()

        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : Utf8StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
                    .create()
                val listType: Type = object : TypeToken<ArrayList<DayReportDTO>>(){}.type
                val reports: ArrayList<DayReportDTO> = gson.fromJson(response, listType)
                callback.onSuccess(reports)
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


    fun createChild(child: ChildDTO, parent: Parent, callback: NetworkCallback<Child>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("createchild")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val gson = Gson()
                val element: JsonElement = gson.fromJson(response.toString(), JsonElement::class.java)
                val returnChild: Child = gson.fromJson(element,Child::class.java)
                callback.onSuccess(returnChild)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            },
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                val gson = Gson()
                val jsonObject = JsonObject().apply {
                    addProperty("firstName", child.firstName)
                    addProperty("lastName", child.lastName)
                    addProperty("ssn", child.ssn)
                    addProperty("parentId", parent.id.toString())
                }
                return gson.toJson(jsonObject).toString().toByteArray()
            }
        }
        mQueue?.add(request)
    }

    fun notifySickLeave(childId: Int, callback: NetworkCallback<String>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("notifysickleave")
            .appendPath(childId.toString())
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { result ->
                callback.onSuccess(result)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            },
        ) {}
        mQueue?.add(request)
    }

    fun applyToDCW(childId: Long, parentId: Long, dcwId: Long, callback: NetworkCallback<ApplicationDTO>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("daycareworker")
            .appendPath("apply")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val gson = Gson()
                val element: JsonElement = gson.fromJson(response.toString(), JsonElement::class.java)
                val application: ApplicationDTO = gson.fromJson(element, ApplicationDTO::class.java)
                callback.onSuccess(application)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            },
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                val gson = Gson()
                val jsonObject = JsonObject().apply {
                    addProperty("daycareWorkerId", dcwId)
                    addProperty("parentId", parentId)
                    addProperty("childId", childId)
                }
                return gson.toJson(jsonObject).toString().toByteArray()
            }
        }
        mQueue?.add(request)
    }
    // Rest of the class implementation goes here
    fun addDaycareWorker(dcw: DaycareWorkerDTO, callback: NetworkCallback<DaycareWorker>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("adddaycareworker")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val gson = Gson()
                val element: JsonElement = gson.fromJson(response.toString(), JsonElement::class.java)
                val returnDaycareWorker: DaycareWorker = gson.fromJson(element, DaycareWorker::class.java)
                callback.onSuccess(returnDaycareWorker)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            },
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                val gson = Gson()
                val jsonObject = JsonObject().apply {
                    addProperty("email", dcw.email)
                    addProperty("ssn", dcw.ssn)
                    addProperty("firstName", dcw.firstName)
                    addProperty("lastName", dcw.lastName)
                    addProperty("mobile", dcw.mobile)
                    addProperty("experienceInYears", dcw.experienceInYears)
                    addProperty("address", dcw.address)
                    addProperty("location", dcw.location)
                    addProperty("locationCode", dcw.locationCode)
                    addProperty("password", dcw.password)
                }
                return gson.toJson(jsonObject).toString().toByteArray()
            }
        }
        mQueue?.add(request)
    }

    fun addParent(parent: ParentDTO, callback: NetworkCallback<Parent>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("createparent")
            .build().toString()

        val request = object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val gson = Gson()
                val element: JsonElement = gson.fromJson(response.toString(), JsonElement::class.java)
                val returnParent: Parent = gson.fromJson(element, Parent::class.java)
                callback.onSuccess(returnParent)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            },
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                val gson = Gson()
                val jsonObject = JsonObject().apply {
                    addProperty("email", parent.email)
                    addProperty("ssn", parent.ssn)
                    addProperty("firstName", parent.firstName)
                    addProperty("lastName", parent.lastName)
                    addProperty("mobile", parent.mobile)
                    addProperty("password", parent.password)
                }
                return gson.toJson(jsonObject).toString().toByteArray()
            }
        }
        mQueue?.add(request)
    }

    fun createDayReport(dayReport: DayReport, callback: NetworkCallback<DayReportDTO>) {
        val url = Uri.parse(BASE_URL)
            .buildUpon()
            .appendPath("createdayreport")
            .build().toString()

        val request = @RequiresApi(Build.VERSION_CODES.O)
        object : Utf8StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
                    .create()
                val element: JsonElement = gson.fromJson(response, JsonElement::class.java)
                val returnDayreport: DayReportDTO = gson.fromJson(element, DayReportDTO::class.java)
                callback.onSuccess(returnDayreport)
            },
            Response.ErrorListener { error ->
                callback.onFailure(error.toString())
            },
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getBody(): ByteArray {
                val gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeDeserializer())
                    .create()

                val jsonObject = JsonObject().apply {
                    addProperty("sleepFrom", dayReport.sleepFrom.toString())
                    addProperty("sleepTo", dayReport.sleepTo.toString())
                    addProperty("appetite", dayReport.appetite)
                    addProperty("comment", dayReport.comment)
                    addProperty("dcwId", dayReport.dcwId)
                    addProperty("childId", dayReport.childId)
                }
                return gson.toJson(jsonObject).toString().toByteArray()
            }
        }
        mQueue?.add(request)
    }
}