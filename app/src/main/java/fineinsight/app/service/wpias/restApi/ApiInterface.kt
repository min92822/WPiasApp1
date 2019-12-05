package fineinsight.app.service.wpias.restApi

import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.dataClass.UserInfo
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @POST("https://wpias.azurewebsites.net/SELECT_USER")
    @FormUrlEncoded
    fun select_user(@FieldMap sql : Map<String, String>) : Call<ArrayList<UserInfo>>

    @POST("https://wpias.azurewebsites.net/SELECT_MYQUESTION")
    @FormUrlEncoded
    fun select_myquestion(@FieldMap sql : Map<String, String>) : Call<ArrayList<QuestionInfo>>

    @POST("https://wpias.azurewebsites.net/SELECT_MYCASE")
    @FormUrlEncoded
    fun select_mycase(@FieldMap sql : Map<String, String>) : Call<ArrayList<QuestionInfo>>


}