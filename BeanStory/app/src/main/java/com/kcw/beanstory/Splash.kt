package com.kcw.beanstory

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var auto: SharedPreferences = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var type = auto.getInt("auto", 0)
        var id = auto.getString("id", null)
        var password = auto.getString("password", null)

        Handler(Looper.getMainLooper()).postDelayed({
            if (type == 1 && id != null && password != null) {

                loginRequest(id, password)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)

                finish()
            }
        }, 2000)

    }

    private fun loginRequest(id: String, password: String) {

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

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        finish()
                    } else {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                        finish()
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

                params["id"] = id
                params["password"] = password

                return params
            }
        }
        val queue = Volley.newRequestQueue(this@Splash)
        queue.add(request)
    }
}