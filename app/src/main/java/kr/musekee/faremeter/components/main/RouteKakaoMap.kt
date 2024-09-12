package kr.musekee.faremeter.components.main

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import kr.musekee.faremeter.libs.makeToast

enum class RouteKakaoMapMode {
    Normal, NoGPS
}
@Composable
fun RouteKakaoMap(
    modifier: Modifier = Modifier,
    latitudes: List<Double>, // 위도
    longitudes: List<Double>, // 경도
    noGPSLatitudes: List<Pair<Double, Double>>, // GPS 끊김 위도
    noGPSLongitudes: List<Pair<Double, Double>>, // GPS 끊김 경도
    mode: RouteKakaoMapMode
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) } // KakaoMapView를 기억하여 재사용할 수 있도록 설정
    val locationX = if (mode == RouteKakaoMapMode.NoGPS && noGPSLongitudes.isNotEmpty()) noGPSLongitudes[0].first else longitudes[0]
    val locationY = if (mode == RouteKakaoMapMode.NoGPS && noGPSLatitudes.isNotEmpty()) noGPSLatitudes[0].first else latitudes[0]

    AndroidView(
        modifier = modifier.height(200.dp),
        factory = { viewContext ->
            mapView.apply {
                mapView.start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            makeToast(context = viewContext, message = "지도를 불러오는데 실패했습니다.")
                        }

                        override fun onMapError(exception: Exception?) {
                            makeToast(context = viewContext, message = "지도를 불러오는 중 알 수 없는 에러가 발생했습니다.\n onMapError: $exception")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(locationY, locationX))

                            kakaoMap.moveCamera(cameraUpdate)

                            if (mode == RouteKakaoMapMode.Normal)
                                drawRoute(kakaoMap, Color.Cyan)
                            else if (mode == RouteKakaoMapMode.NoGPS)
                                drawNoGPSRoute(kakaoMap, Color.Red)

                        }

                        fun drawRoute(kakaoMap: KakaoMap, color: Color) {
                            val layer = kakaoMap.routeLineManager?.layer
                            val style = RouteLineStyles.from(RouteLineStyle.from(16f, color.toArgb()))
                            val segment = RouteLineSegment.from(
                                latitudes.mapIndexed { index, latitude ->
                                    Log.d("좌표", "$latitude, ${longitudes[index]}")
                                    LatLng.from(latitude, longitudes[index])
                                }
                            ).setStyles(style)

                            val options = RouteLineOptions.from(segment)
                                .setStylesSet(RouteLineStylesSet.from(style))
                            layer?.addRouteLine(options)
                        }

                        fun drawNoGPSRoute(kakaoMap: KakaoMap, color: Color) {
                            val layer = kakaoMap.routeLineManager?.layer
                            val style = RouteLineStyles.from(RouteLineStyle.from(16f, color.toArgb()))
                            noGPSLatitudes.mapIndexed { idx, latitude ->
                                val segment = RouteLineSegment.from(
                                    listOf(
                                        LatLng.from(latitude.first, noGPSLongitudes[idx].first),
                                        LatLng.from(latitude.second, noGPSLongitudes[idx].second)
                                    )
                                ).setStyles(style)

                                val options = RouteLineOptions.from(segment)
                                    .setStylesSet(RouteLineStylesSet.from(style))
                                layer?.addRouteLine(options)
                            }
                        }

                        override fun getPosition(): LatLng {
                            return LatLng.from(locationY, locationX)
                        }
                    },
                )
            }
        }
    )
}