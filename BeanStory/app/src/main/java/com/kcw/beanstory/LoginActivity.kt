package com.kcw.beanstory

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.kcw.beanstory.databinding.ActivityLoginBinding
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    //전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityLoginBinding? = null

    //매번 null 체크를 할 필요없이 편의성을 위해 바인딩 변수 재선언
    private val binding get() = mBinding!!

    var backBtnTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        //Activity에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etId.text.isEmpty()) {
                Toast.makeText(this, "아이디를 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else if (binding.etPassword.text.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                loginRequest()
            }
        }

    }

    private fun loginRequest() {

        val address = "http://101.101.219.203/login_user.php"
        val request = object : StringRequest(
            Method.POST, address,
            Response.Listener {

                try {

                    var json = JSONObject(it)

                    var code = json.getInt("code")
                    var name = json.getString("name")

                    if (code == 777) {
                        Toast.makeText(this, "${name}님, 반갑습니다!", Toast.LENGTH_LONG).show()

                        //로그인 데이터 저장
                        var auto :SharedPreferences = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
                        var autoLoginEdit :SharedPreferences.Editor = auto.edit()
                        if (binding.autoLogin.isChecked) {
                            autoLoginEdit.putInt("auto", 1)
                            autoLoginEdit.putString("id", binding.etId.text.toString())
                            autoLoginEdit.putString("password", binding.etPassword.text.toString())
                            autoLoginEdit.putString("nickname", name)
                            autoLoginEdit.commit()
                        } else {
                            autoLoginEdit.putInt("auto", 0)
                            autoLoginEdit.putString("id", binding.etId.text.toString())
                            autoLoginEdit.putString("password", binding.etPassword.text.toString())
                            autoLoginEdit.putString("nickname", name)
                            autoLoginEdit.commit()
                        }

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        finish()
                    } else {
                        Toast.makeText(this, "아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
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

                return params
            }
        }
        val queue = Volley.newRequestQueue(this@LoginActivity)
        queue.add(request)
    }

    override fun onBackPressed() {
        var curTime = System.currentTimeMillis()
        var gapTime = curTime - backBtnTime

        if (0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed()
        } else {
            backBtnTime = curTime
            Toast.makeText(this, "뒤로가기를 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

}