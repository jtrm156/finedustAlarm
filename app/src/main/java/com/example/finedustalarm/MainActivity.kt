package com.example.finedustalarm

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.finedustalarm.databinding.ActivityMainBinding
import com.example.finedustalarm.databinding.ActivityMainBinding.inflate
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var API_KEY = "qczxfsoiNYHP%2BcLy4o3caY21YIBvNixg8JPDcvLF3dHfzjxvw9ntSmrV7V0R5ygvQ827zrMPUN39zxxb8075og%3D%3D"
    var currentPosition=0

    private fun getIdolList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.iamge15, R.drawable.image16, R.drawable.image17)
    }

    val handler=Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ko", "KR"))
        val time = dateFormat.format(date)
        binding.mainTxt4.text = time

        binding.mainMap.setOnClickListener()
        {
            var intent = Intent(this, mapactivity::class.java)
            startActivity(intent)
        }

        var DAPI_KEY = URLDecoder.decode(API_KEY, "UTF-8")

        getfinedustdata(
            DAPI_KEY,
            "json",
            "1",
            "1",
            "경기",
            "1.0"
        )

        binding.mainViewpager.adapter = ViewPagerAdapter(getIdolList())
        binding.mainViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val thread=Thread(PagerRunnable())
        thread.start()
    }

    fun setPage(){
        if(currentPosition==3) currentPosition=0
        binding.mainViewpager.setCurrentItem(currentPosition,true)
        currentPosition+=1
    }

    //2초 마다 페이지 넘기기
    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                Thread.sleep(2000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    override fun onStop(){
        super.onStop()
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy(){
        super.onDestroy()

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Toast.makeText(this, "회원탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getfinedustdata(serviceKey1: String, returnType1 : String, numOfRows1 : String , pageNo1 : String, sidoName1: String, ver1:String){
        val finedustInterface = RetrofitClient.sRetrofit.create(finedustInterface::class.java)
        finedustInterface.finedust(serviceKey1, returnType1, numOfRows1, pageNo1, sidoName1, ver1).enqueue(object : Callback<finedustdata>{
            override fun onResponse(
                call: Call<finedustdata>,
                response: Response<finedustdata>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() as finedustdata
                    Log.d("MainActivity", "complete")
                    binding.mainTxt8.text = result.response.body.items[0].pm10Value + "㎍/㎥  "
                    binding.mainTxt11.text = result.response.body.items[0].pm25Value + "㎍/㎥  "
                    binding.mainTxt14.text = result.response.body.items[0].o3Value + "ppm  "
                    binding.mainTxt17.text = result.response.body.items[0].coValue + "ppm  "

                    if (result.response.body.items[0].pm10Grade == "1") {
                        binding.mainCons.setBackgroundResource(R.drawable.maincolor2)
                        binding.mainTxt1.text = "좋음"
                        binding.mainTxt2.text = "신선한 공기 많이 마시세요~~"
                        binding.mainImage1.setImageResource(R.drawable.icon_verygood)
                    } else if (result.response.body.items[0].pm10Grade == "2") {
                        binding.mainCons.setBackgroundResource(R.drawable.maincolor3)
                        binding.mainTxt1.text = "보통"
                        binding.mainTxt2.text = "쾌적한 날이네요~"
                        binding.mainImage1.setImageResource(R.drawable.icon__isgood)
                    } else if (result.response.body.items[0].pm10Grade == "3") {
                        binding.mainCons.setBackgroundResource(R.drawable.maincolor4)
                        binding.mainTxt1.text = "나쁨"
                        binding.mainTxt2.text = "마스크 필수 착용!"
                        binding.mainImage1.setImageResource(R.drawable.icon_bad)
                    } else if (result.response.body.items[0].pm10Grade == "4") {
                        binding.mainCons.setBackgroundResource(R.drawable.maincolor1)
                        binding.mainTxt1.text = "최악"
                        binding.mainTxt2.text = "외출을 삼가하세요!!"
                        binding.mainImage1.setImageResource(R.drawable.image7)
                    } else {
                        Log.d(
                            "MainActivity",
                            "getfinedustdata = onResponse : error code ${response.code()}"
                        )
                    }
                }
            }
            override fun onFailure(call: Call<finedustdata>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })
    }
}
