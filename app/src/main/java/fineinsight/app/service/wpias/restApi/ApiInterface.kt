package fineinsight.app.service.wpias.restApi

import fineinsight.app.service.wpias.dataClass.MycaseInfo
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.dataClass.UserInfo
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    // 회원정보조회
    @POST("https://wpias.azurewebsites.net/SELECT_USER")
    @FormUrlEncoded
    fun select_user(@FieldMap sql : Map<String, String>) : Call<ArrayList<UserInfo>>

    // 사용자 질문조회
    @POST("https://wpias.azurewebsites.net/SELECT_MYQUESTION")
    @FormUrlEncoded
    fun select_myquestion(@FieldMap sql : Map<String, String>) : Call<ArrayList<QuestionInfo>>

    // 사용자 질문케이스조회
    @POST("https://wpias.azurewebsites.net/SELECT_MYCASE")
    @FormUrlEncoded
    fun select_mycase(@FieldMap sql : Map<String, String>) : Call<ArrayList<MycaseInfo>>

    // 사용자 질문케이스조회
    @POST("https://wpias.azurewebsites.net/INSERT_QUESTION")
    @FormUrlEncoded
    fun insert_question(@FieldMap sql : Map<String, String>) : Call<String>

    // 설정창 스위치1 상태 변경
    @POST("https://wpias.azurewebsites.net/UPDATE_SWITCH1")
    @FormUrlEncoded
    fun update_switch1(@FieldMap sql : Map<String, String>) : Call<String>

    // 설정창 스위치2 상태 변경
    @POST("https://wpias.azurewebsites.net/UPDATE_SWITCH2")
    @FormUrlEncoded
    fun update_switch2(@FieldMap sql : Map<String, String>) : Call<String>

}