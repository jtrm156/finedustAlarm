package com.example.finedustalarm

import android.annotation.SuppressLint
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
import retrofit2.Callback
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

        getfinedustdata("종로","month",1,100,"json",
            "qczxfsoiNYHP%2BcLy4o3caY21YIBvNixg8JPDcvLF3dHfzjxvw9ntSmrV7V0R5ygvQ827zrMPUN39zxxb8075og%3D%3D")
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

    private fun getfinedustdata(stationName1 : String, dataTerm1 : String, pageNo1 : Int, numOfRows1 : Int , returnType1 : String, serviceKey1: String){
        val finedustInterface = RetrofitClient.sRetrofit.create(finedustInterface::class.java)
        finedustInterface.finedust(stationName1, dataTerm1, pageNo1, numOfRows1, returnType1, serviceKey1).enqueue(object : Callback<Item>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<Item>,
                response: Response<Item>
            ) {
                if(response.isSuccessful) {
                    val result = response.body() as Item
                    binding.mainTxt5.text = "미세먼지농도 : " + result.pm10Value
                }
                else{
                    Log.d("MainActivity", "getfinedustdata = onResponse : error code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })

    }
}