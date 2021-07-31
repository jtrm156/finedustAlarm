package com.example.finedustalarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.widget.ViewPager2
import com.example.finedustalarm.databinding.ActivityMainBinding
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.mainMap.setOnClickListener()
        {
            var intent = Intent(this, mapactivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy(){
        super.onDestroy()

        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
            }else {
                //Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }
}