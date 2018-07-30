package com.trofiventures.chameleon

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.util.Log
import com.trofiventures.chameleon.model.SkinClient
import com.trofiventures.chameleon.service.RetrofitClient
import org.jetbrains.anko.doAsync
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ChameleonViewModel : ViewModel() {

    val chameleon: MutableLiveData<HashMap<String, String>> = MutableLiveData()

    companion object {
        var chameleonMap: HashMap<String, String> = HashMap()
    }

    private fun change(context: Context, clearCache: Boolean = false) {

        if (clearCache)
            readJson(context)

        if (chameleonMap.isEmpty())
            readJson(context)
        // notify views to change colors
        chameleon.postValue(chameleonMap)
    }

    private fun value(property: JSONObject): String {
        var value = property.getString("propertyValue")
        var type = property.getInt("propertyType")
        return if (type == 0 && value.length > 7)
            "#".plus(value.substring(7)).plus(value.substring(1, 7))
        else value
    }

    private fun readJson(context: Context) {
        Chameleon.getSkin(context).let { it ->
            val chameleonJson = JSONObject(it)
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

    }

    /**
     * retrieve the skin from the server.
     */
    fun getSkin(context: Context, walletId: String, serviceId: String?) {
        val retrofit = RetrofitClient()
        retrofit.get()?.let {
            doAsync {
                it.getSkin(SkinClient(walletId, serviceId)).enqueue(
                        object : Callback<Any> {
                            override fun onFailure(call: Call<Any>?, t: Throwable?) {
                                getSkin(context, walletId, null)
                            }

                            override fun onResponse(call: Call<Any>?, response: Response<Any>?) {
                                Log.d("chameleon", "" + response?.body())
                                //save skin
                                Chameleon.saveSkin(context, JSONObject(response?.body() as Map<String, String>))
                                //notify views to change color
                                change(context, true)
                            }
                        }
                )
            }
        }
    }
}