package kr.musekee.faremeter.components.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle
import kr.musekee.faremeter.R

@Composable
fun MyKakaoMap(
    modifier: Modifier = Modifier,
    locationX: Double, // 서버에서 제공하는 X 값 (경도)
    locationY: Double, // 서버에서 제공하는 Y 값 (위도)
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) } // KakaoMapView를 기억하여 재사용할 수 있도록 설정

    AndroidView(
        modifier = modifier.height(200.dp), // AndroidView의 높이 임의 설정
        factory = { context ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        // 지도 생명 주기 콜백: 지도가 파괴될 때 호출
                        override fun onMapDestroy() {
                            // 필자가 직접 만든 Toast생성 함수
                            makeToast(context = context, message = "지도를 불러오는데 실패했습니다.")
                        }

                        // 지도 생명 주기 콜백: 지도 로딩 중 에러가 발생했을 때 호출
                        override fun onMapError(exception: Exception?) {
                            // 필자가 직접 만든 Toast생성 함수
                            makeToast(context = context, message = "지도를 불러오는 중 알 수 없는 에러가 발생했습니다.\n onMapError: $exception")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        // KakaoMap이 준비되었을 때 호출
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            // 카메라를 (locationY, locationX) 위치로 이동시키는 업데이트 생성
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(locationY, locationX))

                            // 지도에 표시할 라벨의 스타일 설정
                            val style = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_taxi)))

                            // 라벨 옵션을 설정하고 위치와 스타일을 적용
                            val options = LabelOptions.from(LatLng.from(locationY, locationX)).setStyles(style)

                            // KakaoMap의 labelManager에서 레이어를 가져옴
                            val layer = kakaoMap.labelManager?.layer

                            // 카메라를 지정된 위치로 이동
                            kakaoMap.moveCamera(cameraUpdate)

                            // 지도에 라벨을 추가
                            layer?.addLabel(options)
                        }

                        override fun getPosition(): LatLng {
                            // 현재 위치를 반환
                            return LatLng.from(locationY, locationX)
                        }
                    },
                )
            }
        },
    )
}

fun makeToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}