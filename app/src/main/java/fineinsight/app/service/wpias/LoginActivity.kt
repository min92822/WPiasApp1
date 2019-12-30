package fineinsight.app.service.wpias

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import fineinsight.app.service.wpias.dataClass.UserInfo
import fineinsight.app.service.wpias.publicObject.PubVariable
import fineinsight.app.service.wpias.publicObject.UserToken
import fineinsight.app.service.wpias.restApi.ApiUtill
import fineinsight.app.service.wpias.user_Main.MainActivity
import fineinsight.app.service.wpias.user_SignUp.SignUpPreActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class LoginActivity : RootActivity(){

//    var authStateListener : FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        UserToken.token

        PubVariable.init()

        SetDarkBar()

        loginSubmit()

        signUp()
    }

    //로그인
    fun login(id : String, pw : String){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(id, pw).addOnSuccessListener {

            PubVariable.uid = it.user?.uid!!

            getUserinfo()

        }.addOnFailureListener {

            Toast.makeText(this, "계정정보를 확인해주세요", Toast.LENGTH_LONG).show()

        }

    }

    //로그인 버튼 동작
    fun loginSubmit(){

        loginSubmit.setOnClickListener {

            if(loginId.text.isNotEmpty() && loginPw.text.isNotEmpty()){

                login(loginId.text.toString(), loginPw.text.toString())

            }else{

                Toast.makeText(this, "계정정보를 확인해주세요", Toast.LENGTH_LONG).show()

            }

        }

    }

    //로그인 후 파이어베이스에서 가져온 uuid를 파라미터로 azure 서버로 보내 회원 정보를 가져온다
    fun getUserinfo(){

        var map = HashMap<String,String>()

        map["IDKEY"] = FirebaseAuth.getInstance().currentUser?.uid!!

        ApiUtill().getSELECT_USER().select_user(map).enqueue(object : Callback<ArrayList<UserInfo>>{

            override fun onResponse(
                call: Call<ArrayList<UserInfo>>,
                response: Response<ArrayList<UserInfo>>
            ) {

                if(response.isSuccessful){

                    if(response.body()?.size != 0){

                        PubVariable.userInfo = response.body()!![0]

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

                    }else{

                        Toast.makeText(this@LoginActivity, "해당하는 계정이 없습니다.", Toast.LENGTH_LONG).show()

                    }

                }

            }

            override fun onFailure(call: Call<ArrayList<UserInfo>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }

    fun signUp(){

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpPreActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        CloseAlert(this)

    }

}