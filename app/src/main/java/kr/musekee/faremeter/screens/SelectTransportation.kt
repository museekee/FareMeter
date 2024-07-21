package kr.musekee.faremeter.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kr.musekee.faremeter.BUS
import kr.musekee.faremeter.R
import kr.musekee.faremeter.TAXI
import kr.musekee.faremeter.components.main.MainButton
import kr.musekee.faremeter.libs.SetLandscape
import kr.musekee.faremeter.libs.SetPortrait

@Composable
fun SelectTransportation(navController: NavController) {
    val context = LocalContext.current
    SetPortrait(context)
    val items = listOf(TransportationItem.Taxi, TransportationItem.Bus)
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            text = "교통수단 선택"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach{ item ->
                MainButton(
                    label = stringResource(id = item.title),
                    icon = item.icon,
                    color = item.color,
                    onClick = {
                        SetLandscape(context)
                        navController.navigate(item.screenRoute)
                    }
                )
            }
        }   
    }
}

sealed class TransportationItem(
    val title: Int, val icon: Int, val color: Color, val screenRoute: String
) {
    data object Taxi : TransportationItem(R.string.Taxi, R.drawable.ic_taxi, Color.Yellow, TAXI)
    data object Bus : TransportationItem(R.string.Bus, R.drawable.ic_bus, Color.Green, BUS)
}