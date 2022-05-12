package com.kcw.beanstory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kcw.beanstory.databinding.FragSettingsBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Frag_settings : Fragment() {

    private var mBinding: FragSettingsBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.frag_settings, container, false)
        mBinding = FragSettingsBinding.inflate(inflater, container, false)

        var auto =
            (activity as MainActivity).getSharedPreferences("autoLogin", Activity.MODE_PRIVATE)
        var autoLoginEdit: SharedPreferences.Editor = auto.edit()

        binding.etId.setText(auto.getString("id", null))
        binding.etPassword.setText(auto.getString("password", null))
        binding.etPassword2.setText(auto.getString("password", null))
        binding.etNickname.setText(auto.getString("nickname", null))

        binding.btnLogout.setOnClickListener {

            autoLoginEdit.clear()
            autoLoginEdit.commit()

            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)

                (activity as MainActivity).finish()
            }

        }

        binding.ivProfile.setOnClickListener {
            //카메라 권한
            (activity as MainActivity).requirePermissions(arrayOf(Manifest.permission.CAMERA), (activity as MainActivity).PERM_CAMERA)
        }

        return binding.root
    }


    fun set_Image(bitmap: Bitmap) {
        binding.ivProfile.setImageBitmap(bitmap)
    }

}