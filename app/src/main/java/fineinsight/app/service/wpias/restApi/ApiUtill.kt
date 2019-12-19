package fineinsight.app.service.wpias.restApi

class ApiUtill {

    // 회원가입
    val insert_user_url : String = "https://wpias.azurewebsites.net/INSERT_USER/"

    fun getINSERT_USER() : ApiInterface{
        return ApiRequest().getStringResponse(insert_user_url)!!.create(ApiInterface::class.java)
    }

    // 회원정보조회
    val select_user_url : String = "https://wpias.azurewebsites.net/SELECT_USER/"

    fun getSELECT_USER() : ApiInterface{
        return ApiRequest().getResponse(select_user_url)!!.create(ApiInterface::class.java)
    }

    // 사용자 질문조회
    val select_myquestion_url : String = "https://wpias.azurewebsites.net/SELECT_MYQUESTION/"

    fun getSELECT_MYQUESTION() : ApiInterface{
        return ApiRequest().getResponse(select_myquestion_url)!!.create(ApiInterface::class.java)
    }

    // 사용자 질문케이스조회
    val select_mycase_url : String = "https://wpias.azurewebsites.net/SELECT_MYCASE/"

    fun getSELECT_MYCASE() : ApiInterface{
        return ApiRequest().getResponse(select_mycase_url)!!.create(ApiInterface::class.java)
    }

    // 사용자 질문등록
    val insert_question_url : String = "https://wpias.azurewebsites.net/INSERT_QUESTION/"

    fun getINSERT_QUESTION() : ApiInterface{
        return ApiRequest().getStringResponse(insert_question_url)!!.create(ApiInterface::class.java)
    }

    // 설정창 스위치1 변경
    val update_switch1_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH1/"

    fun getUPDATE_SWITCH1() : ApiInterface{
        return ApiRequest().getStringResponse(update_switch1_url)!!.create(ApiInterface::class.java)
    }

    // 설정창 스위치2 변경
    val update_switch2_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH2/"

    fun getUPDATE_SWITCH2() : ApiInterface{
        return ApiRequest().getStringResponse(update_switch2_url)!!.create(ApiInterface::class.java)
    }

}