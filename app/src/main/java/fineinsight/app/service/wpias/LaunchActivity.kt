package fineinsight.app.service.wpias

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import fineinsight.app.service.wpias.dataClass.UserInfo
import fineinsight.app.service.wpias.publicObject.PubVariable
import fineinsight.app.service.wpias.restApi.ApiUtill
import fineinsight.app.service.wpias.user_Main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LaunchActivity : RootActivity() {

    var authStateListener : FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginStateCheck()

    }

    //로그인 상태 체크 앱 실행시 메인으로 바로 진행할지 여부를 결정하는 펑션
    fun loginStateCheck(){

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            var user = firebaseAuth.currentUser

            if(user != null) {

                getUserinfo()
                startActivity(
                    Intent(this@LaunchActivity, MainActivity::class.java).setFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP))

            }else{

                startActivity(
                    Intent(this@LaunchActivity, LoginActivity::class.java).setFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP))

            }

        }

    }

    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener!!)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener!!)
    }

    //로그인 후 파이어베이스에서 가져온 uuid를 파라미터로 azure 서버로 보내 회원 정보를 가져온다
    fun getUserinfo(){

        var map = HashMap<String,String>()

        map["IDKEY"] = FirebaseAuth.getInstance().currentUser?.uid!!

        ApiUtill().getSELECT_USER().select_user(map).enqueue(object :
            Callback<ArrayList<UserInfo>> {

            override fun onResponse(
                call: Call<ArrayList<UserInfo>>,
                response: Response<ArrayList<UserInfo>>
            ) {

                if(response.isSuccessful){

                    if(response.body()?.size != 0){

                        PubVariable.userInfo = response.body()!![0]

                        startActivity(Intent(this@LaunchActivity, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

                    }else{

                        Toast.makeText(this@LaunchActivity, "해당하는 계정이 없습니다.", Toast.LENGTH_LONG).show()

                    }

                }

            }

            override fun onFailure(call: Call<ArrayList<UserInfo>>, t: Throwable) {
                Toast.makeText(this@LaunchActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }

}