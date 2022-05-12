package com.kcw.beanstory

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

abstract class BaseActivity : AppCompatActivity() {

    abstract fun permissionGranted(requestCode: Int)
    abstract fun permissionDenied(requestCode: Int)

    //권한 검사
    fun requirePermissions(permission:Array<String>, requestCode: Int) {
        //API 버전이 마시멜로 미만이면 권한 처리 필요없음
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(requestCode)
        } else {
            //권한이 없으면 요청
            ActivityCompat.requestPermissions(this, permission, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.all {it == PackageManager.PERMISSION_GRANTED}) {
            permissionGranted(requestCode)
        } else {
            permissionDenied(requestCode)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}