package com.kcw.beanstory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kcw.beanstory.databinding.ActivityJoinBinding
import org.json.JSONException
import org.json.JSONObject

class JoinActivity : AppCompatActivity() {

    //전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityJoinBinding? = null

    //매번 null 체크를 할 필요없이 편의성을 위해 바인딩 변수 재선언
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_join)

        //Activity에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            finish()
        }

        binding.btnJoin.setOnClickListener {
            if (binding.etPassword.text.toString().equals(binding.etPassword2.text.toString())) {
                JoinRequest()
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun JoinRequest() {

        val address = "http://101.101.219.203/join_user.php"
        val request = object : StringRequest(
            Method.POST, address,
            Response.Listener {

                try {

                    var json = JSONObject(it)

                    var code = json.getInt("code")
                    if (code == 777) {
                        Toast.makeText(this, "BeanStory에 오신 것을 환영합니다!", Toast.LENGTH_LONG).show()
                        
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        finish()
                    } else {
                        Toast.makeText(this, "회원가입 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Log.d("JSON Exception", e.toString())
                }

            },
            Response.ErrorListener {

                Log.e("ERROR", "Error [$it]")
                Toast.makeText(baseContext, "Cannot connect to server", Toast.LENGTH_LONG)
                    .show()

            }) {
            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()

                params["id"] = binding.etId.text.toString()
                params["password"] = binding.etPassword.text.toString()
                params["nickname"] = binding.etNickname.text.toString()

                return params
            }
        }
        val queue = Volley.newRequestQueue(this@JoinActivity)
        queue.add(request)

    }


}