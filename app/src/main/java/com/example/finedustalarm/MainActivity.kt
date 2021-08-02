package com.example.finedustalarm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.viewpager2.widget.ViewPager2
import com.example.finedustalarm.databinding.ActivityMainBinding
import com.example.finedustalarm.databinding.ActivityMainBinding.inflate
import com.google.android.gms.location.*
import com.kakao.kakaolink.KakaoLink
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.sdk.user.UserApiClient
import com.kakao.util.helper.log.Logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.DoublePredicate
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var API_KEY = "qczxfsoiNYHP%2BcLy4o3caY21YIBvNixg8JPDcvLF3dHfzjxvw9ntSmrV7V0R5ygvQ827zrMPUN39zxxb8075og%3D%3D"
    var currentPosition=0
    val TAG: String = "로그"

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체

    companion object{
        const val Music = "app_preferences"
    }

    private lateinit var mPreferences: SharedPreferences

    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    private fun getIdolList(): ArrayList<Int> {
        return arrayListOf<Int>(R.drawable.iamge15, R.drawable.image16, R.drawable.image17)
    }

    val handler=Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreferences = getSharedPreferences(Music, MODE_PRIVATE);
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()

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

        binding.mainImage6.setOnClickListener()
        {
            var intent = Intent(this, stationActivity::class.java)
            startActivity(intent)
        }

        binding.mainLink.setOnClickListener(){
            kakaoLink()
        }

        var DAPI_KEY = URLDecoder.decode(API_KEY, "UTF-8")

        binding.mainViewpager.adapter = ViewPagerAdapter(getIdolList())
        binding.mainViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val thread=Thread(PagerRunnable())
        thread.start()

        mLocationRequest =  LocationRequest.create().apply {
            interval = 2000 // 업데이트 간격 단위(밀리초)
            fastestInterval = 1000 // 가장 빠른 업데이트 간격 단위(밀리초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
            maxWaitTime= 2000 // 위치 갱신 요청 최대 대기 시간 (밀리초)
        }

        startLocationUpdates()
        var x1 = mPreferences.getString("latitude", null)!!.toDouble()
        var y1 = mPreferences.getString("longitude", null)!!.toDouble()
        var x2 = mPreferences.getString("tmX", null)!!.toDouble()
        var y2 = mPreferences.getString("tmY", null)!!.toDouble()
        binding.mainTxt3.text = x1.toString() + y1.toString()
        callKakaoKeyword(y1, x1,"WGS84","TM")
        callKakaoKeyword2(y1,x1,"WGS84","WGS84")
        var stationName1 = mPreferences.getString("stationName", null)!!
        getfinedustdata2(DAPI_KEY, "json", x2, y2)
        //getfinedustdata(DAPI_KEY, "json", "1", "1", "경기", "1.0")
        getfinedustdata3(DAPI_KEY, "json", "1", "1", stationName1, "DAILY", "1.0")
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

    fun kakaoLink(){
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    "오늘의 미세먼지 정보",
                    "http://k.kakaocdn.net/dn/dRHGmW/btraXSxGQ0C/8YYUMbDO5NYq5qdl2YTh40/kakaolink40_original.jpg",
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build()
                )
                    .setDescrption("미세먼지 앱을 통해서 확인하세요")
                    .build()
            )
            .setSocial(
                SocialObject.newBuilder().setLikeCount(286).setCommentCount(45)
                    .setSharedCount(845).setViewCount(40).build()
            )
            .addButton(
                ButtonObject(
                    "자세히 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()
                )
            )
            .build()

        val serverCallbackArgs: MutableMap<String, String> =
            HashMap()
        serverCallbackArgs["user_id"] = "\${current_user_id}"
        serverCallbackArgs["product_id"] = "\${shared_product_id}"

        KakaoLinkService.getInstance().sendDefault(
            this,
            params,
            object : ResponseCallback<KakaoLinkResponse?>() {
                override fun onFailure(errorResult: ErrorResult) {
                    Logger.e(errorResult.toString())
                }

                override fun onSuccess(result: KakaoLinkResponse?) { // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                }
            })
    }
    protected fun startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates()")

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "startLocationUpdates() 두 위치 권한중 하나라도 없는 경우 ")
            return
        }
        Log.d(TAG, "startLocationUpdates() 위치 권한이 하나라도 존재하는 경우")
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청합니다.
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(TAG, "onLocationResult()")
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mPreferences = getSharedPreferences(Music, MODE_PRIVATE);
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()

        Log.d(TAG, "onLocationChanged()")
        mLastLocation = location
        Log.d("checkcurrentposition", "LATITUDE :  " + mLastLocation.latitude)
        Log.d("checkcurrentposition", "longitude :  " + mLastLocation.longitude)

        preferencesEditor.putString("latitude", mLastLocation.latitude.toString())
        preferencesEditor.apply()
        preferencesEditor.putString("longitude", mLastLocation.longitude.toString())
        preferencesEditor.apply()
    }

    // 위치 업데이터를 제거 하는 메서드
    private fun stoplocationUpdates() {
        Log.d(TAG, "stoplocationUpdates()")
        // 지정된 위치 결과 리스너에 대한 모든 위치 업데이트를 제거
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }

    // 위치 권한이 있는지 확인하는 메서드
    fun checkPermissionForLocation(context: Context): Boolean {
        Log.d(TAG, "checkPermissionForLocation()")
        // Android 6.0 Marshmallow 이상에서는 지리 확보(위치) 권한에 추가 런타임 권한이 필요합니다.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPermissionForLocation() 권한 상태 : O")
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                Log.d(TAG, "checkPermissionForLocation() 권한 상태 : X")
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult()")
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 클릭")
                startLocationUpdates()
                // View Button 활성화 상태 변경
            } else {
                Log.d(TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this@MainActivity, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
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
                    //binding.mainTxt8.text = result.response.body.items[0].pm10Value + "㎍/㎥  "
                    //binding.mainTxt11.text = result.response.body.items[0].pm25Value + "㎍/㎥  "
                    //binding.mainTxt14.text = result.response.body.items[0].o3Value + "ppm  "
                    //binding.mainTxt17.text = result.response.body.items[0].coValue + "ppm  "
                }
                   else {
                        Log.d(
                            "MainActivity",
                            "getfinedustdata = onResponse : error code ${response.code()}"
                        )
                    }
                }
            override fun onFailure(call: Call<finedustdata>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })
    }

    private fun callKakaoKeyword(x: Double, y : Double, input_coord : String , output_coord : String){
        val kakaoInterface = KakaoApiRetrofitclient.sRetrofit2.create(KakaoApiService::class.java)
        kakaoInterface.transKakaoAddress("KakaoAK 9ad3ef00455b86a0a9670df7bce917cd",x, y, input_coord, output_coord).enqueue(object : Callback<kakaodata1>{
            override fun onResponse(
                call: Call<kakaodata1>,
                response: Response<kakaodata1>
            ) {
                if (response.isSuccessful) {
                    mPreferences = getSharedPreferences(Music, MODE_PRIVATE);
                    val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()

                    val result = response.body() as kakaodata1
                    Log.d("MainActivity", "complete")
                    preferencesEditor.putString("tmX",result.documents[0].x.toString())
                    preferencesEditor.putString("tmY",result.documents[0].y.toString())
                    preferencesEditor.apply()
                }
                else
                {
                    Log.d("MainActivity", "getkakaodata1 = onResponse : error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<kakaodata1>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })
    }

    private fun callKakaoKeyword2(x: Double, y : Double, input_coord : String , output_coord : String){
        val kakaoInterface = KakaoApiRetrofitclient.sRetrofit2.create(KakaoApiService2::class.java)
        kakaoInterface.transKakaoAddress("KakaoAK 9ad3ef00455b86a0a9670df7bce917cd",x, y, input_coord, output_coord).enqueue(object : Callback<kakaodata2>{
            override fun onResponse(
                call: Call<kakaodata2>,
                response: Response<kakaodata2>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() as kakaodata2
                    Log.d("MainActivity", "complete")
                    binding.mainTxt3.text = result.documents[0].region_2depth_name + " " + result.documents[0].region_3depth_name
                }
                else
                {
                    Log.d("MainActivity", "getkakaodata2 = onResponse : error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<kakaodata2>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })
    }

    private fun getfinedustdata2(serviceKey1: String, returnType1 : String, tmX: Double , tmY : Double){
        val kakaoInterface = RetrofitClient2.sRetrofit3.create(finedustInterface2::class.java)
        kakaoInterface.NearbyMsrstnList(serviceKey1, returnType1, tmX, tmY).enqueue(object : Callback<finedustdata2>{
            override fun onResponse(
                call: Call<finedustdata2>,
                response: Response<finedustdata2>
            ) {
                if (response.isSuccessful) {
                    mPreferences = getSharedPreferences(Music, MODE_PRIVATE);
                    val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
                    val result = response.body() as finedustdata2
                    Log.d("MainActivity", "complete")

                    for(i in 0 until(result.response.body.totalCount)) {
                        preferencesEditor.putString(
                            "stationName",
                            result.response.body.items[0].stationName
                        )
                        preferencesEditor.apply()

                        preferencesEditor.putInt(
                            "stationCount",
                            result.response.body.totalCount
                        )
                        preferencesEditor.apply()

                        preferencesEditor.putString(
                            "addr${i}", result.response.body.items[i].addr
                        )
                        preferencesEditor.apply()
                    }
                }
                else
                {
                    Log.d("MainActivity", "getkakaodata2 = onResponse : error code ${response.code()}")
                }
            }
            override fun onFailure(call: Call<finedustdata2>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })
    }

    private fun getfinedustdata3(serviceKey1: String, returnType1 : String, numOfRows1 : String , pageNo1 : String, stationName1: String, dataTerm1:String, ver1: String){
        val finedustInterface = RetrofitClient3.sRetrofit3.create(finedustInterface3::class.java)
        finedustInterface.MsrstnAcctoRltmMesureDnsty(serviceKey1, returnType1, numOfRows1, pageNo1, stationName1, dataTerm1, ver1).enqueue(object : Callback<finedustdata3X>{
            override fun onResponse(
                call: Call<finedustdata3X>,
                response: Response<finedustdata3X>
            ) {
                if (response.isSuccessful) {
                    mPreferences = getSharedPreferences(Music, MODE_PRIVATE);
                    val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()
                    val result = response.body() as finedustdata3X
                    Log.d("MainActivity", "complete")
                    binding.mainTxt8.text = result.response.body.items[0].pm10Value + "㎍/㎥  "
                    binding.mainTxt11.text = result.response.body.items[0].pm25Value + "㎍/㎥  "
                    binding.mainTxt14.text = result.response.body.items[0].o3Value + "ppm  "
                    binding.mainTxt17.text = result.response.body.items[0].coValue + "ppm  "
                    preferencesEditor.putString("dataTime",result.response.body.items[0].dataTime)
                    preferencesEditor.apply()
                    if (result.response.body.items[0].pm10Grade == "1") {
                        binding.mainCons.setBackgroundResource(R.drawable.maincolor2)
                        binding.mainTxt1.text = "최고 좋음"
                        binding.mainTxt2.text = "공기 상태 최고! 건강하세요!"
                        binding.mainImage1.setImageResource(R.drawable.icon_verygood)
                    } else if (result.response.body.items[0].pm10Grade == "2") {
                        binding.mainCons.setBackgroundResource(R.drawable.maincolor3)
                        binding.mainTxt1.text = "좋음"
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
            override fun onFailure(call: Call<finedustdata3X>, t: Throwable) {
                Log.d("MainActivity", t.message ?: "통신오류")
            }
        })
    }

    override fun onResume(){
        super.onResume()

        binding.swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                if(binding.swipeRefresh.isRefreshing) {
                    binding.swipeRefresh.isRefreshing = false
                    val now: Long = System.currentTimeMillis()
                    val date = Date(now)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ko", "KR"))
                    val time = dateFormat.format(date)
                    binding.mainTxt4.text = time

                    mLocationRequest =  LocationRequest.create().apply {
                        interval = 2000 // 업데이트 간격 단위(밀리초)
                        fastestInterval = 1000 // 가장 빠른 업데이트 간격 단위(밀리초)
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
                        maxWaitTime= 2000 // 위치 갱신 요청 최대 대기 시간 (밀리초)
                    }
                }
            }, 1500)

        }
    }

    override fun onDestroy(){
        super.onDestroy()

        UserApiClient.instance.logout { error ->
            if (error != null) {
                Toast.makeText(this, "로그아웃 실패 $error", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            }
        }

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Toast.makeText(this, "회원탈퇴 실패 $error", Toast.LENGTH_SHORT).show()
            }else {
                Toast.makeText(this, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
