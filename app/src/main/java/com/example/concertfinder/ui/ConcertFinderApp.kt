package com.example.concertfinder.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.concertfinder.R
import com.example.concertfinder.model.LoadingStatus
import com.example.concertfinder.ui.screens.CalendarScreen
import com.example.concertfinder.ui.screens.DayDetailsScreen
import com.example.concertfinder.ui.commonui.ErrorScreen
import com.example.concertfinder.ui.commonui.EventDetailsScreen
import com.example.concertfinder.ui.screens.EventsScreen
import com.example.concertfinder.ui.commonui.LoadingScreen
import com.example.concertfinder.ui.screens.SearchBarScreen
import com.example.concertfinder.ui.screens.SearchResultsScreen
import com.example.concertfinder.ui.utils.ConcertFinderContentType
import com.example.concertfinder.ui.utils.ConcertFinderScreen
import com.example.concertfinder.ui.utils.TopLevelRoute
import com.example.concertfinder.ui.utils.topLevelRoutes


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ConcertFinderApp(
    modifier: Modifier = Modifier,
    viewModel: ConcertFinderViewModel = viewModel(factory = ConcertFinderViewModel.Factory),
    navController: NavHostController = rememberNavController(),
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
) {
    // get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()

    // collect ui state from view model
    val uiState = viewModel.uiState.collectAsState()

    /* TODO: add support for different screen sizes */
    val contentType = when(windowSize) {
        WindowWidthSizeClass.Compact -> ConcertFinderContentType.Compact
        WindowWidthSizeClass.Medium -> ConcertFinderContentType.Medium
        WindowWidthSizeClass.Expanded -> ConcertFinderContentType.Expanded
        else -> ConcertFinderContentType.Compact
    }

    // update show bottom bar based on current destination
    if (backStackEntry?.destination?.route in topLevelRoutes.map { it.route }) {
        viewModel.updateShowBottomBar(true)
    } else {
        viewModel.updateShowBottomBar(false)
    }

    Scaffold(
        // add top bar and bottom bar to scaffold
        topBar = {
            ConcertFinderTopBar(
                onBackPressed = {
                    // navigate back
                    navController.navigateUp()
                },
                showBackButton = !uiState.value.showBottomBar
            )
        },
        bottomBar = {
            if (uiState.value.showBottomBar) {
                ConcertFinderNavigationBar(
                    backStackEntry = backStackEntry,
                    onClick = {
                        navController.navigate(it.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = topLevelRoutes[0].route,
            modifier = modifier
        ) {
            // my events screen composable
            composable(route = ConcertFinderScreen.MyEvents.name) {
                EventsScreen(
                    onClick = {
                        navController.navigate(ConcertFinderScreen.EventDetails.name)
                    },
                    modifier = modifier
                )
            }

            // search screen composable
            composable(route = ConcertFinderScreen.Search.name) {
                SearchBarScreen(
                    uiState = uiState.value,
                    onExpandedChange = {
                        viewModel.updateSearchExpanded(it)
                    },
                    onQueryChange = {
                        viewModel.updateSearchText(it)
                    },
                    onSearch = {
                        viewModel.getEvents(
                            keyWord = it
                        )
                        navController.navigate(ConcertFinderScreen.SearchResults.name)
                    },
                    onDispose = {
                        viewModel.resetSearchBar()
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }

            // calendar screen composable
            composable(route = ConcertFinderScreen.Calendar.name) {
                if (uiState.value.loadingStatus is LoadingStatus.Loading) {
                    LoadingScreen()
                } else if (uiState.value.loadingStatus is LoadingStatus.Error) {
                    ErrorScreen()
                } else {
                    CalendarScreen(
                        onClick = {
                            navController.navigate(ConcertFinderScreen.DayDetails.name)
                        },
                        modifier = modifier
                    )
                }
            }

            // search results screen composable
            composable(route = ConcertFinderScreen.SearchResults.name) {
                if (uiState.value.loadingStatus is LoadingStatus.Loading) {
                    LoadingScreen()
                } else if (uiState.value.loadingStatus is LoadingStatus.Error) {
                    ErrorScreen()
                } else {
                    SearchResultsScreen(
                        eventList = uiState.value.searchResults,
                        onClick = {
                            navController.navigate(ConcertFinderScreen.EventDetails.name)
                        },
                        modifier = modifier,
                        innerPadding = innerPadding
                    )
                }
            }

            // day details screen composable
            composable(route = ConcertFinderScreen.DayDetails.name) {
                DayDetailsScreen(
                    onClick = {
                        navController.navigate(ConcertFinderScreen.EventDetails.name)
                    },
                    modifier = modifier
                )
            }

            // event details screen composable
            composable(route = ConcertFinderScreen.EventDetails.name) {
                EventDetailsScreen(
                    modifier = modifier
                )
            }
        }
    }
}



@Composable
fun ConcertFinderNavigationBar(
    backStackEntry: NavBackStackEntry?,
    onClick: (TopLevelRoute<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar {
        val currentDestination = backStackEntry?.destination

        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    Icon(
                        topLevelRoute.icon,
                        contentDescription = stringResource(topLevelRoute.title)
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertFinderTopBar(
    onBackPressed: () -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = modifier
                        .size(dimensionResource(id = R.dimen.app_icon_size))
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { onBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        }
    )
}
