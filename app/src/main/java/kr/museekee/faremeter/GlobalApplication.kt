package kr.museekee.faremeter

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMapSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoMapSdk.init(this, getString(R.string.KAKAO_API_KEY))
        KakaoSdk.init(this, getString(R.string.KAKAO_API_KEY))
    }
}