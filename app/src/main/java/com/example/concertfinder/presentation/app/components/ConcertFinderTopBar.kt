package com.example.concertfinder.presentation.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.FilterSortButton
import com.example.concertfinder.presentation.ui.theme.MyIcons
import com.example.concertfinder.presentation.utils.AppDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertFinderTopBar(
    onBackPressed: () -> Unit,
    route: String,
    showBackButton: Boolean,
    showFilterButton: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onFilterSortClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = modifier
                        .size(dimensionResource(id = R.dimen.app_icon_size))
                        .padding(end = 8.dp)
                )
                Text(
                    text = if (route.startsWith(AppDestinations.EVENT_LIST) == true) {
                        stringResource(id = R.string.results)
                    } else if (route == AppDestinations.EVENT_DETAILS) {
                        stringResource(id = R.string.event_details)
                    } else if (route == AppDestinations.FILTER) {
                        stringResource(id = R.string.filter)
                    } else {
                        stringResource(id = R.string.app_name)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = if (showFilterButton ) { modifier.widthIn(max = 160.dp) } else { modifier }
                )
                Spacer(modifier = Modifier.weight(1f))
                FilterSortButton(
                    onFilterSortClicked = { onFilterSortClicked() },
                    visible = showFilterButton
                )
            }
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = { onBackPressed() }
                ) {
                    Icon(
                        imageVector = MyIcons.navigateBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}