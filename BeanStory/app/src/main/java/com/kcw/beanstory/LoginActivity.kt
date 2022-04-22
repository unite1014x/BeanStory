package com.kcw.beanstory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    var backBtnTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var btn_login = findViewById<Button>(R.id.btn_login)
        var btn_join = findViewById<Button>(R.id.btn_join)

        btn_join.setOnClickListener {
            var intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

        var curTime = System.currentTimeMillis()
        var gapTime = curTime - backBtnTime

        if (0 <= gapTime && gapTime <= 2000){
            super.onBackPressed()
        } else {
            backBtnTime = curTime
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }

    }
}