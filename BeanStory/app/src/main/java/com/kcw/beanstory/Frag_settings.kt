package com.kcw.beanstory

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class Frag_settings :Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_settings, container, false)

        var auto = (activity as MainActivity).getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var autoLoginEdit :SharedPreferences.Editor = auto.edit()

        view.findViewById<EditText>(R.id.et_id).setText(auto.getString("id", null))
        view.findViewById<EditText>(R.id.et_password).setText(auto.getString("password", null))
        view.findViewById<EditText>(R.id.et_password2).setText(auto.getString("password", null))
        view.findViewById<EditText>(R.id.et_nickname).setText(auto.getString("nickname", null))

        view.findViewById<Button>(R.id.btn_logout).setOnClickListener {

            autoLoginEdit.clear()
            autoLoginEdit.commit()

            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)

                (activity as MainActivity).finish()
            }

        }

        return view
    }
}