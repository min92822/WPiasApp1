package fineinsight.app.service.wpias.restApi

import fineinsight.app.service.wpias.dataClass.MycaseInfo
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.dataClass.UserInfo
import fineinsight.app.service.wpias.public_function.FCM
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    // 회원가입
    @POST("https://wpias.azurewebsites.net/INSERT_USER")
    @FormUrlEncoded
    fun insert_user(@FieldMap sql : Map<String, String>) : Call<String>

    // 회원정보조회
    @POST("https://wpias.azurewebsites.net/SELECT_USERWITHTOKENOS")
    @FormUrlEncoded
    fun select_userwithtokenos(@FieldMap sql : Map<String, String>) : Call<ArrayList<UserInfo>>


    // 사용자 질문조회
    @POST("https://wpias.azurewebsites.net/SELECT_MYQUESTION")
    @FormUrlEncoded
    fun select_myquestion(@FieldMap sql : Map<String, String>) : Call<ArrayList<QuestionInfo>>

    // 사용자 질문케이스조회
    @POST("https://wpias.azurewebsites.net/SELECT_MYCASE")
    @FormUrlEncoded
    fun select_mycase(@FieldMap sql : Map<String, String>) : Call<ArrayList<MycaseInfo>>

    // 답변 리뷰 등록
    @POST("https://wpias.azurewebsites.net/UPDATE_FEEDBACK")
    @FormUrlEncoded
    fun update_feedback(@FieldMap sql : Map<String, String>) : Call<String>


    // 사용자 질문등록
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


    // FCM 전송
    @Headers("Authorization: key=AAAAFqobJsQ:APA91bEHj_c08w_2GrtOMFyOBwb6S6cUQx-K3E56VkoG4Tq6NBR48T64JFvQyMqczVbr6ZJjlMMNXEPQuu5Gb6XB6OYetxXxXS894Amv2j1CmsFFurVYp_T5CQjTTh9ofssUt9AHA7ju"
        , "Content-Type:application/json")
    @POST("https://fcm.googleapis.com/fcm/send")
    fun sendFCM(@Body FCMInteraction : Any) : Call<ResponseBody>


    // 로그아웃시 토큰 공백으로 변경
    @POST("https://wpias.azurewebsites.net/UPDATE_TOKENLOGOUT")
    @FormUrlEncoded
    fun update_tokenlogout(@FieldMap sql : Map<String, String>) : Call<String>

}