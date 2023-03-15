package `is`.hi.hbv601g.petraapp.networking

import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import java.nio.charset.Charset

open class Utf8StringRequest(
    method: Int,
    url: String,
    listener: Response.Listener<String>,
    errorListener: Response.ErrorListener
) : StringRequest(method, url, listener, errorListener) {

    override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
        val parsed = String(
            response?.data ?: ByteArray(0),
            Charset.forName("UTF-8")
        )
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response))
    }
}