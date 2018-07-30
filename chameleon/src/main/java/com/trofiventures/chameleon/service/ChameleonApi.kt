package com.trofiventures.chameleon.service

import com.trofiventures.chameleon.model.SkinClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * only for test
 */
interface ChameleonApi {

    @POST("skin/v2/getSkin")
    fun getSkin(@Body skinClient: SkinClient): Call<Any>
}
