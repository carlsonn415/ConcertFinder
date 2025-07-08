package com.example.concertfinder.presentation.app.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.concertfinder.R
import com.example.concertfinder.presentation.common_ui.elements.FilterSortButton
import com.example.concertfinder.presentation.ui.theme.MyIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConcertFinderTopBar(
    onBackPressed: () -> Unit,
    showBackButton: Boolean,
    showFilterButton: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onFilterSortClicked: () -> Unit,
    titleId: Int,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = titleId),
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