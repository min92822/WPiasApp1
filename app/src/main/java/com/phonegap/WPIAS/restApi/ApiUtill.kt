package com.phonegap.WPIAS.restApi

class ApiUtill {

    // 회원가입
    val insert_user_url : String = "https://wpias.azurewebsites.net/INSERT_USER/"

    fun getINSERT_USER() : ApiInterface {
        return ApiRequest().getStringResponse(insert_user_url)!!.create(
            ApiInterface::class.java)
    }

    // 회원정보조회
    val select_user_url : String = "https://wpias.azurewebsites.net/SELECT_USERWITHTOKENOS/"

    fun getSELECT_USERWITHTOKENOS() : ApiInterface {
        return ApiRequest().getResponse(select_user_url)!!.create(ApiInterface::class.java)
    }

    // 사용자 질문조회
    val select_myquestion_url : String = "https://wpias.azurewebsites.net/SELECT_MYQUESTION/"

    fun getSELECT_MYQUESTION() : ApiInterface {
        return ApiRequest().getResponse(select_myquestion_url)!!.create(
            ApiInterface::class.java)
    }

    // 사용자 질문케이스조회
    val select_mycase_url : String = "https://wpias.azurewebsites.net/SELECT_MYCASE/"

    fun getSELECT_MYCASE() : ApiInterface {
        return ApiRequest().getResponse(select_mycase_url)!!.create(ApiInterface::class.java)
    }

    // 경과추가 답변미요청
    val insert_case_url : String = "https://wpias.azurewebsites.net/INSERT_CASE/"

    fun getINSERT_CASE() : ApiInterface {
        return ApiRequest().getStringResponse(insert_case_url)!!.create(
            ApiInterface::class.java)
    }

    // 경과추가 답변요청
    val insert_caserequest_url : String = "https://wpias.azurewebsites.net/INSERT_CASEREQUEST/"

    fun getINSERT_CASEREQUEST() : ApiInterface {
        return ApiRequest().getStringResponse(insert_caserequest_url)!!.create(
            ApiInterface::class.java)
    }


    // 답변 리뷰 등록
    val update_feedback_url : String = "https://wpias.azurewebsites.net/UPDATE_FEEDBACK/"

    fun getUPDATE_FEEDBACK() : ApiInterface {
        return ApiRequest().getStringResponse(update_feedback_url)!!.create(
            ApiInterface::class.java)
    }

    // 사용자 질문등록
    val insert_question_url : String = "https://wpias.azurewebsites.net/INSERT_QUESTION/"

    fun getINSERT_QUESTION() : ApiInterface {
        return ApiRequest().getStringResponse(insert_question_url)!!.create(
            ApiInterface::class.java)
    }

    // 사용자 질문등록 + 위치추가
    val insert_questionwitharea_url : String = "https://wpias.azurewebsites.net/INSERT_QUESTIONWITHAREA/"

    fun getINSERT_QUESTIONWITHAREA() : ApiInterface {
        return ApiRequest().getStringResponse(insert_questionwitharea_url)!!.create(
            ApiInterface::class.java)
    }

    // 설정창 스위치1 변경
    val update_switch1_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH1/"

    fun getUPDATE_SWITCH1() : ApiInterface {
        return ApiRequest().getStringResponse(update_switch1_url)!!.create(
            ApiInterface::class.java)
    }

    // 설정창 스위치2 변경
    val update_switch2_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH2/"

    fun getUPDATE_SWITCH2() : ApiInterface {
        return ApiRequest().getStringResponse(update_switch2_url)!!.create(
            ApiInterface::class.java)
    }

    //PUSH 메세지 전송
    val send_FCM_url : String = "https://fcm.googleapis.com/fcm/send/"

    fun sendFCM(): ApiInterface {
        return ApiRequest().getInterAction(send_FCM_url)!!.create(ApiInterface::class.java)
    }

    // 로그아웃시 토큰 공백으로 변경
    val update_tokenlogout_url : String = "https://wpias.azurewebsites.net/UPDATE_TOKENLOGOUT/"

    fun getUPDATE_TOKENLOGOUT() : ApiInterface {
        return ApiRequest().getStringResponse(update_tokenlogout_url)!!.create(
            ApiInterface::class.java)
    }


    // 상대방 푸시정보 조회
    val select_checkagree_url : String = "https://wpias.azurewebsites.net/SELECT_CHECKAGREE/"

    fun getSELECT_CHECKAGREE() : ApiInterface {
        return ApiRequest().getResponse(select_checkagree_url)!!.create(
            ApiInterface::class.java)
    }

    // 시,도 조회
    val select_city_url : String = "https://wpias.azurewebsites.net/SELECT_CITY/"

    fun getSELECT_CITY() : ApiInterface {
        return ApiRequest().getResponse(select_city_url)!!.create(
            ApiInterface::class.java)
    }

    // 구 조회
    val select_district_url : String = "https://wpias.azurewebsites.net/SELECT_DISTRICT/"

    fun getSELECT_DISTRICT() : ApiInterface {
        return ApiRequest().getResponse(select_district_url)!!.create(
            ApiInterface::class.java)
    }

}