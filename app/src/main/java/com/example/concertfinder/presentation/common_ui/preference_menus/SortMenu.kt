package com.example.concertfinder.presentation.common_ui.preference_menus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants.sortOptions
import com.example.concertfinder.common.Constants.sortTypes

@Composable
fun SortMenu(
    currentSortOption: String,
    isSortOptionsExpanded: Boolean,
    onSortOptionSelected: (String) -> Unit,
    onExpandSortOptionsDropdown: (Boolean) -> Unit,
    currentSortType: String,
    isSortTypeExpanded: Boolean,
    onSortTypeSelected: (String) -> Unit,
    onExpandSortTypeDropdown: (Boolean) -> Unit,
    isSortMenuExpanded: Boolean,
    onExpandSortMenuDropdown: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //------------------------------------------------------------------------------------------ Sort text and arrow row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clickable {
                    onExpandSortMenuDropdown(!isSortMenuExpanded)
                }
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_medium),
                    vertical = dimensionResource(R.dimen.padding_small)
                )
        ) {
            Text(
                text = stringResource(R.string.sort),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = modifier.weight(1f)
            )

            Icon(
                imageVector = if (isSortMenuExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow),
                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
            )
        }

        if (isSortMenuExpanded) {
            Spacer(modifier = modifier.padding(dimensionResource(R.dimen.padding_small)))

            // ----------------------------------------------------------------------------------------- Sort options (name, date, distance, relevance)
            PreferencesDropdown(
                currentPreference = currentSortOption,
                dropdownLabel = stringResource(R.string.sort_by),
                preferenceOptions = sortOptions,
                isPreferencesExpanded = isSortOptionsExpanded,
                onPreferencesExpandedChange = onExpandSortOptionsDropdown,
                onPreferenceSelected = onSortOptionSelected,
                modifier = modifier
            )

            Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

            // ----------------------------------------------------------------------------------------- Sort type (ascending or descending)
            PreferencesDropdown(
                currentPreference = currentSortType,
                dropdownLabel = stringResource(R.string.sort_type),
                preferenceOptions = sortTypes,
                isPreferencesExpanded = isSortTypeExpanded,
                onPreferencesExpandedChange = onExpandSortTypeDropdown,
                onPreferenceSelected = onSortTypeSelected,
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortMenuPreview() {
    SortMenu(
        isSortMenuExpanded = true,
        currentSortOption = "name",
        onSortOptionSelected = {},
        onExpandSortOptionsDropdown = {},
        currentSortType = "asc",
        isSortTypeExpanded = false,
        onSortTypeSelected = {},
        onExpandSortTypeDropdown = {},
        isSortOptionsExpanded = false,
        onExpandSortMenuDropdown = {}
    )
}