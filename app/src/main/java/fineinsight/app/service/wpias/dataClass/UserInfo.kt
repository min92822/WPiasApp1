package fineinsight.app.service.wpias.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserInfo (@SerializedName("IDKEY") @Expose var idkey : String,
                     @SerializedName("EMAIL") @Expose var email : String,
                     @SerializedName("NICKNAME") @Expose var nickname : String,
                     @SerializedName("GENDER") @Expose var gender : String,
                     @SerializedName("BIRTHDAY") @Expose var birthday : String,
                     @SerializedName("OS") @Expose var os : String,
                     @SerializedName("SWITCH1") @Expose var switch1 : String,
                     @SerializedName("SWITCH2") @Expose var switch2 : String,
                     @SerializedName("TOKEN") @Expose var token : String,
                     @SerializedName("REMARK") @Expose var remark : String,
                     @SerializedName("USERTYPE") @Expose var usertype : String)

/*
    IDKEY		UUID
    EMIAL		이메일
    NICKNAME	닉네임(이름)
    GENDER		성별
    BIRTHDAY	생년월일
    OS		    접속OS
    SWITCH1		설정창 스위치1
    SWITCH2		설정창 스위치2
    TOKEN		fcm Token
    REMARK		기타
    USERTYPE	유저타입

 */