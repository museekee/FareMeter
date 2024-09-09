package kr.musekee.faremeter.components.main

import android.content.Context
import android.util.Log
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
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet

@Composable
fun MyKakaoMap(
    modifier: Modifier = Modifier,
    latitudes: List<Double>, // 위도
    longitudes: List<Double> // 경도
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) } // KakaoMapView를 기억하여 재사용할 수 있도록 설정
    val locationX = longitudes[0]
    val locationY = latitudes[0]

    AndroidView(
        modifier = modifier.height(200.dp), // AndroidView의 높이 임의 설정
        factory = { viewContext ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        // 지도 생명 주기 콜백: 지도가 파괴될 때 호출
                        override fun onMapDestroy() {
                            // 필자가 직접 만든 Toast생성 함수
                            makeToast(context = viewContext, message = "지도를 불러오는데 실패했습니다.")
                        }

                        // 지도 생명 주기 콜백: 지도 로딩 중 에러가 발생했을 때 호출
                        override fun onMapError(exception: Exception?) {
                            // 필자가 직접 만든 Toast생성 함수
                            makeToast(context = viewContext, message = "지도를 불러오는 중 알 수 없는 에러가 발생했습니다.\n onMapError: $exception")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        // KakaoMap이 준비되었을 때 호출
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(locationY, locationX))

                            kakaoMap.moveCamera(cameraUpdate)

                            val layer = kakaoMap.routeLineManager?.layer
                            val stylesSet = RouteLineStylesSet.from(
                                "blueStyles",
                                RouteLineStyles.from(RouteLineStyle.from(16f, android.graphics.Color.BLUE))
                            )

                            val segment = RouteLineSegment.from(
                                latitudes.mapIndexed { index, latitude ->
                                    Log.d("좌표", "$latitude, ${longitudes[index]}")
                                    LatLng.from(latitude, longitudes[index])
                                }
                            ).setStyles(stylesSet.getStyles(0))

                            val options = RouteLineOptions.from(segment)
                                .setStylesSet(stylesSet)
                            layer?.addRouteLine(options)
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