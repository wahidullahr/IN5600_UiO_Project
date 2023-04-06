package com.example.uioproject;

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("methodPostRemoteLogin")
    fun methodPostRemoteLogin(
        @Query("em") email: String,
        @Query("ph") passwordHash: String
    ): Call<Person>
//does not exist
    @GET("getUserInfo")
    fun getUserInfo(
        @Query("username") username: String
    ): Call<Person>

    @Multipart
    @POST("postMethodUploadPhoto")
    fun uploadPhoto(
        @Part photo: MultipartBody.Part,
        @Query("userId") userId: String,
        @Query("tagId") tagId: String,
        @Query("fileName") fileName: String,
        @Query("imageStringBase64") imageStringBase64: String?
    ): Call<ResponseBody>

    @GET("getMethodDownloadPhoto")
    fun getPhotos(
        @Query("username") username: String
    ): Call<List<String>>

    @POST("methodPostChangePasswd")
    fun changePassword(
        @Query("em") email: String,
        @Query("np") newPassword: String,
        @Query("ph") passwordHash: String
    ): Call<ResponseBody>

    @POST("methodPostReplacePhoto")
    fun replacePhoto(
        @Query("userId") userId: String,
        @Query("tagId") tagId: String,
        @Query("newTag") newTag: String
    ): Call<ResponseBody>


    @POST("postUpdateTag")
    fun uploadTag(
        @Query("userId") userId: String,
        @Query("indexUpdateTag") indexUpdateTag: String,
        @Query("updateTagDes") updateTagDes: String,
        @Query("updateTagPho") updateTagPho: String,
        @Query("updateTagLoc") updateTagLoc: String,
        @Query("updateTagPeopleName") newTagPeopleName: String
    ): Call<ResponseBody>
}
