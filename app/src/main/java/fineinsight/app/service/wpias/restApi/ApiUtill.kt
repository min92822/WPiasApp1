package fineinsight.app.service.wpias.restApi

class ApiUtill {

    val select_user_url : String = "https://wpias.azurewebsites.net/SELECT_USER/"

    fun getSELECT_USER() : ApiInterface{
        return ApiRequest().getResponse(select_user_url)!!.create(ApiInterface::class.java)
    }


    val select_myquestion_url : String = "https://wpias.azurewebsites.net/SELECT_MYQUESTION/"

    fun getSELECT_MYQUESTION() : ApiInterface{
        return ApiRequest().getResponse(select_myquestion_url)!!.create(ApiInterface::class.java)
    }


    val select_mycase_url : String = "https://wpias.azurewebsites.net/SELECT_MYCASE/"

    fun getSELECT_MYCASE() : ApiInterface{
        return ApiRequest().getResponse(select_mycase_url)!!.create(ApiInterface::class.java)
    }

    val insert_question_url : String = "https://wpias.azurewebsites.net/INSERT_QUESTION/"

    fun getINSERT_QUESTION() : ApiInterface{
        return ApiRequest().getStringResponse(insert_question_url)!!.create(ApiInterface::class.java)
    }

    val update_switch1_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH1/"

    fun getUPDATE_SWITCH1() : ApiInterface{
        return ApiRequest().getStringResponse(update_switch1_url)!!.create(ApiInterface::class.java)
    }

    val update_switch2_url : String = "https://wpias.azurewebsites.net/UPDATE_SWITCH2/"

    fun getUPDATE_SWITCH2() : ApiInterface{
        return ApiRequest().getStringResponse(update_switch2_url)!!.create(ApiInterface::class.java)
    }

}