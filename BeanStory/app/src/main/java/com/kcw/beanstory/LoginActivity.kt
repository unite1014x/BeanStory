package com.kcw.beanstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kcw.beanstory.databinding.ActivityLoginBinding

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