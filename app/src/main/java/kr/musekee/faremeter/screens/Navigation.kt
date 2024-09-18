package kr.musekee.faremeter.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kr.musekee.faremeter.R
import kr.musekee.faremeter.RESULTS
import kr.musekee.faremeter.SELECT_TRANSPORTATION
import kr.musekee.faremeter.SETTINGS

@Composable
fun BottomNavigation(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val items = listOf(
        BottomNavItem.Settings,
        BottomNavItem.SelectTransportation,
        BottomNavItem.Results
    )
    NavigationBar(
        containerColor = Color(0xFF333333),
        contentColor = Color.White,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screenRoute,
                label = {
                    Text(
                        text = stringResource(id = item.title),
                        style = TextStyle(
                            fontSize = 12.sp
                        )
                    )
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .size(25.dp),
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.title),
                        tint = Color.White
                    )
                },
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

sealed class BottomNavItem(
    val title: Int, val icon: Int, val screenRoute: String
) {
    data object Settings : BottomNavItem(R.string.Settings, R.drawable.ic_spanner, SETTINGS)
    data object SelectTransportation : BottomNavItem(R.string.SelectTransportation, R.drawable.ic_taxi, SELECT_TRANSPORTATION)
    data object Results : BottomNavItem(R.string.Results, R.drawable.ic_history, RESULTS)
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.SelectTransportation.screenRoute) {
        composable(BottomNavItem.Settings.screenRoute) {
            Settings()
        }
        composable(BottomNavItem.SelectTransportation.screenRoute) {
            SelectTransportation()
        }
        composable(BottomNavItem.Results.screenRoute) {
            Records()
        }
    }
}