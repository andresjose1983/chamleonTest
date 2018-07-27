package com.trofiventures.chameleon

import android.content.Context
import org.json.JSONObject

class Chameleon {

    companion object {

        fun saveSkin(context: Context, jsonObject: JSONObject) {
            val sharedPreferences = context.getSharedPreferences("chameleon", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("theme", jsonObject.toString()).apply()
        }

        fun getSkin(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("chameleon", Context.MODE_PRIVATE)
            return sharedPreferences.getString("theme", null)
        }
    }
}