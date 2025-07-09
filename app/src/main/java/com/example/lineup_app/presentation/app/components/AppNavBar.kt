package com.example.lineup_app.presentation.app.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.lineup_app.presentation.utils.TopLevelRoute
import com.example.lineup_app.presentation.utils.topLevelRoutes

@Composable
fun AppNavBar(
    backStackEntry: NavBackStackEntry?,
    onClick: (TopLevelRoute<String>) -> Unit,
) {
    NavigationBar {
        val currentDestination = backStackEntry?.destination

        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    if (currentDestination?.hierarchy?.any {it.route == topLevelRoute.route} == true) {
                        Icon(
                            topLevelRoute.icon,
                            contentDescription = stringResource(topLevelRoute.title)
                        )
                    } else {
                        Icon(
                            topLevelRoute.iconOutlined,
                            contentDescription = stringResource(topLevelRoute.title)
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(topLevelRoute.title)
                    )
                },
                selected = currentDestination?.hierarchy?.any {it.route == topLevelRoute.route} == true,
                onClick = { onClick(topLevelRoute) },
            )
        }
    }
}