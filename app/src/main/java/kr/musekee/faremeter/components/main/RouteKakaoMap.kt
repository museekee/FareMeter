package kr.musekee.faremeter.components.main

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
import com.kakao.vectormap.route.RouteLinePattern
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import kr.musekee.faremeter.R
import kr.musekee.faremeter.libs.LatLngData
import kr.musekee.faremeter.libs.makeToast

@Composable
fun RouteKakaoMap(
    modifier: Modifier = Modifier,
    latLng: List<LatLngData>
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) } // KakaoMapView를 기억하여 재사용할 수 있도록 설정
    val locationX = latLng[0].longitude
    val locationY = latLng[0].latitude

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

                            val noGPSTimePos = mutableListOf<Pair<LatLngData, LatLngData>>()
                            for (i in 1 until latLng.size) {
                                if (latLng[i].time - latLng[i - 1].time > 5000L) {
                                    noGPSTimePos.add(Pair(latLng[i - 1], latLng[i]))
                                }
                            }
                            drawRoute(
                                kakaoMap = kakaoMap,
                                latLngs = latLng,
                                width = 16f,
                                color = Color.Blue
                            )
                            noGPSTimePos.map {
                                drawRoute(
                                    kakaoMap = kakaoMap,
                                    latLngs = listOf(it.first, it.second),
                                    width = 8f,
                                    color = Color.Red
                                )
                            }
                        }

                        fun drawRoute(
                            kakaoMap: KakaoMap,
                            latLngs: List<LatLngData>,
                            width: Float = 16f,
                            color: Color
                        ) {
                            val layer = kakaoMap.routeLineManager?.layer

                            val style = RouteLineStyles.from(
                                RouteLineStyle.from(width, color.toArgb())
                                    .setPattern(RouteLinePattern.from(
                                        R.drawable.route_arrow, 12f
                                    ))
                            )
                            val segment = RouteLineSegment.from(
                                latLngs.map {
//                                    Log.d("좌표", "${it.latitude}, ${it.longitude}")
                                    LatLng.from(it.latitude, it.longitude)
                                }
                            ).setStyles(style)

                            val options = RouteLineOptions.from(segment)
                                .setStylesSet(RouteLineStylesSet.from(style))
                            layer?.addRouteLine(options)
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