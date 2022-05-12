package com.kcw.beanstory

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.kcw.beanstory.databinding.ActivityMainBinding
import java.lang.Exception
import java.text.SimpleDateFormat

class MainActivity : BaseActivity() {

    //전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null

    //매번 null 체크를 할 필요없이 편의성을 위해 바인딩 변수 재선언
    private val binding get() = mBinding!!

    var backBtnTime: Long = 0

    val PERM_STORAGE = 9
    val PERM_CAMERA = 10

    val REQ_CAMERA = 11

    //원본 이미지 주소
    var realUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        //Activity에서 사용할 바인딩 클래스의 인스턴스 생성
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFrag()

        //공용 저장소 권한
        requirePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERM_STORAGE)

    }

    private fun setFrag() {

        binding.mainMenu.run {
            setOnItemSelectedListener {
                val ft = supportFragmentManager.beginTransaction()
                when (it.itemId) {
                    R.id.friend -> {
                        ft.replace(R.id.main_frame, Frag_friend(), "fragment_friend").commit()
                    }
                    R.id.talk -> {
                        ft.replace(R.id.main_frame, Frag_talk(), "fragment_talk").commit()
                    }
                    R.id.story -> {
                        ft.replace(R.id.main_frame, Frag_story(), "fragment_story").commit()
                    }
                    R.id.settings -> {
                        ft.replace(R.id.main_frame, Frag_settings(), "fragment_settings").commit()
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

    fun initViews() {
        //(supportFragmentManager.findFragmentByTag("fragment_settings") as Frag_settings).camera_check()
    }

    fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(createFileName(), "image/jpg")?.let { uri ->  
            realUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)

            startActivityForResult(intent, REQ_CAMERA);
        }

    }

    //원본 이미지를 저장할 Uri를 MediaStore에 저장하는 메소드
    fun createImageUri (filename:String, mimeType:String) :Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    //파일 이름을 생성하는 메소드
    fun createFileName():String {
        val simpleDataFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
        val timestamp = simpleDataFormat.format(System.currentTimeMillis())
        val filename = "photo_${timestamp}.jpg"

        return filename
    }

    fun loadBitmap(photoUri: Uri) :Bitmap? {
        var image:Bitmap? = null
        try {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                val source = ImageDecoder.createSource(contentResolver, photoUri)
                image = ImageDecoder.decodeBitmap(source)
            } else {
                image = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }

        return image
    }

    override fun permissionGranted(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> {initViews()}
            PERM_CAMERA -> {openCamera()}
        }
    }

    override fun permissionDenied(requestCode: Int) {
        when (requestCode) {
            PERM_STORAGE -> {
                Toast.makeText(this, "공용 저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            PERM_CAMERA -> {Toast.makeText(this, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()}
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_CAMERA -> {
                    //val bitmap = data?.extras?.get("data") as Bitmap //미리보기 이미지
                    //(supportFragmentManager.findFragmentByTag("fragment_settings") as Frag_settings).set_Image(bitmap)
                    realUri?.let { uri ->
                        val bitmap = loadBitmap(uri)
                        bitmap?.let {
                            (supportFragmentManager.findFragmentByTag("fragment_settings") as Frag_settings).set_Image(it)
                        }

                        realUri = null
                    }
                }
            }
        }
    }
}