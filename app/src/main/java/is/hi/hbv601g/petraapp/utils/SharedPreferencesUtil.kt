package `is`.hi.hbv601g.petraapp.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import `is`.hi.hbv601g.petraapp.Entities.FullDCW
import `is`.hi.hbv601g.petraapp.Entities.Parent
import `is`.hi.hbv601g.petraapp.MainActivity

class SharedPreferencesUtil {
    companion object {
        fun clearSharedPreferences(context: Context) {
            val prefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()
            editor.apply()
            Log.d(MainActivity.TAG, "User was cleared from sharedPrefs!")
        }

        fun saveUserToSharedPreferences(context: Context, value: Any) {
            val gson = Gson()
            val json = gson.toJson(value)
            val sharedPreferences = context.getSharedPreferences(
                "MY_APP_PREFS",
                Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("USER_KEY", json).apply()
        }

        fun fetchUserFromSharedPreferences(context: Context, role: String): Any {
            val prefs = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
            val userString = prefs.getString("USER_KEY", "")
            val gson = Gson()
            return when (role) {
                "DCW" -> gson.fromJson(userString, FullDCW::class.java)
                "Parent" -> gson.fromJson(userString, Parent::class.java)
                else -> throw Error("No role found to determine class!")
            }
        }

        fun saveStringToSharedPreferences(context: Context, key: String, value: String) {
            val sharedPreferences = context.getSharedPreferences(
                "MY_APP_PREFS",
                Context.MODE_PRIVATE
            )
            sharedPreferences.edit().putString(key, value).apply()
        }
    }
}

