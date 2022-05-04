package com.kcw.beanstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kcw.beanstory.databinding.ActivityLoginBinding
import com.kcw.beanstory.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null

    //매번 null 체크를 할 필요없이 편의성을 위해 바인딩 변수 재선언
    private val binding get() = mBinding!!

    var backBtnTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        //Activity에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFrag()
    }

    private fun setFrag() {

        binding.mainMenu.run {
            setOnItemSelectedListener {
                val ft = supportFragmentManager.beginTransaction()
                when (it.itemId) {
                    R.id.friend -> {
                        ft.replace(R.id.main_frame, Frag_friend()).commit()
                    }
                    R.id.talk -> {
                        ft.replace(R.id.main_frame, Frag_talk()).commit()
                    }
                    R.id.story -> {
                        ft.replace(R.id.main_frame, Frag_story()).commit()
                    }
                    R.id.settings -> {
                        ft.replace(R.id.main_frame, Frag_settings()).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.friend
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