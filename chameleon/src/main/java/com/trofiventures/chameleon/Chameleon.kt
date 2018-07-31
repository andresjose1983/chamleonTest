package com.trofiventures.chameleon

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class Chameleon {

    companion object {

        private var chameleonMap: HashMap<String, String> = HashMap()

        fun saveSkin(context: Context, jsonObject: JSONObject) {
            val sharedPreferences = context.getSharedPreferences("chameleon", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("theme", jsonObject.toString()).apply()
        }

        fun getSkin(context: Context): String {
            val sharedPreferences = context.getSharedPreferences("chameleon", Context.MODE_PRIVATE)
            return sharedPreferences.getString("theme", null)
        }

        private fun value(property: JSONObject): String {
            var value = property.getString("propertyValue")
            var type = property.getInt("propertyType")
            return if (type == 0 && value.length > 7)
                "#".plus(value.substring(7)).plus(value.substring(1, 7))
            else value
        }

        fun getSkinHashMap(context: Context): HashMap<String, String> {
            val sharedPreferences = context.getSharedPreferences("chameleon", Context.MODE_PRIVATE)
            val skin = sharedPreferences.getString("theme", null)
            if (skin != null) {
                val chameleonJson = JSONObject(skin)
                val views = chameleonJson.getJSONObject("containers")
                val size = views?.names()?.length() ?: 0
                (0 until size).forEach { it ->
                    val element = views.names()[it].toString()
                    val values = views[element] as JSONArray
                    (0 until values.length()).forEach {
                        val property = values.getJSONObject(it)
                        val value = value(property)
                        if (!value.isNullOrEmpty())
                            chameleonMap[element.plus(".").plus(property.getString("propertyName"))] = value
                    }
                }
            }
            return chameleonMap
        }
    }
}