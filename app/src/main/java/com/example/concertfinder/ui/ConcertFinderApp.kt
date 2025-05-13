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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.concertfinder.R
import com.example.concertfinder.ui.screens.ConcertFinderHomeScreen
import com.example.concertfinder.ui.screens.DayDetailsScreen
import com.example.concertfinder.ui.screens.EventDetailsScreen
import com.example.concertfinder.ui.screens.SearchResultsScreen
import com.example.concertfinder.ui.utils.ConcertFinderContentType
import com.example.concertfinder.ui.utils.NavigationBarElement
import com.example.concertfinder.ui.utils.ConcertFinderScreen


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
    val currentScreen = ConcertFinderScreen.valueOf(
        backStackEntry?.destination?.route ?: ConcertFinderScreen.Home.name
    )

    // collect ui state from view model
    val uiState = viewModel.uiState.collectAsState()

    /* TODO: add support for different screen sizes */
    val contentType = when(windowSize) {
        WindowWidthSizeClass.Compact -> ConcertFinderContentType.Compact
        WindowWidthSizeClass.Medium -> ConcertFinderContentType.Medium
        WindowWidthSizeClass.Expanded -> ConcertFinderContentType.Expanded
        else -> ConcertFinderContentType.Compact
    }

    // check if current screen is home screen
    if (currentScreen.name == ConcertFinderScreen.Home.name) {
        // update show bottom bar to true
        viewModel.updateShowBottomBar(true)
        // clear back stack
        navController.clearBackStack<ConcertFinderScreen>()
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
                    currentScreen = uiState.value.currentScreen,
                    onClick = {
                        // update current screen
                        viewModel.updateCurrentScreen(it)

                        // ensures that the back stack won't fill up with screens
                        navController.clearBackStack(ConcertFinderScreen.Home.name)
                    },
                    modifier = modifier
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = ConcertFinderScreen.Home.name,
            modifier = modifier.padding(innerPadding)
        ) {
            // home screen composable
            composable(route = ConcertFinderScreen.Home.name) {
                ConcertFinderHomeScreen(
                    uiState = uiState.value,
                    onClick = {
                        when (it) {
                            NavigationBarElement.Events -> {
                                navController.navigate(ConcertFinderScreen.EventDetails.name)
                                viewModel.updateShowBottomBar(false)
                            }
                            NavigationBarElement.Search -> {
                                navController.navigate(ConcertFinderScreen.Results.name)
                                viewModel.updateShowBottomBar(false)
                            }
                            NavigationBarElement.Calendar -> {
                                navController.navigate(ConcertFinderScreen.DayDetails.name)
                                viewModel.updateShowBottomBar(false)
                            }
                        }
                    },
                    modifier = modifier,
                )
            }
            // search results screen composable
            composable(route = ConcertFinderScreen.Results.name) {
                SearchResultsScreen(
                    onClick = {
                        navController.navigate(ConcertFinderScreen.EventDetails.name)
                    },
                    modifier = modifier
                )
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
    currentScreen: NavigationBarElement,
    onClick: (NavigationBarElement) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (screen in NavigationBarElement.entries) {
            NavigationBarItem(
                icon = {
                    Icon(
                        screen.icon,
                        contentDescription = stringResource(screen.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.title)
                    )
                },
                selected = currentScreen == screen,
                onClick = { onClick(screen) },
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
