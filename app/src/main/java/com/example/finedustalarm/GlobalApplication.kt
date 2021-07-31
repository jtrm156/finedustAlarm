package com.example.finedustalarm

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "20442513b0533fa7c912f5000a48ec9d")
    }
}