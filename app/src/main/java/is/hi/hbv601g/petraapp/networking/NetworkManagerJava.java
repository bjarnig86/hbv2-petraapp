package is.hi.hbv601g.petraapp.networking;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkManagerJava {
    private static final String BASE_URL = "";

    private static NetworkManagerJava mInstance;
    private static RequestQueue mQueue;
    private Context mContext;

    public static synchronized NetworkManagerJava getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkManagerJava(context);
        }
        return mInstance;
    }

    public NetworkManagerJava(Context context) {
        mContext = context;
        mQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null){
            mQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mQueue;
    }
}
