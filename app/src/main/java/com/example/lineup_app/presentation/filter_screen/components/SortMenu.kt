package com.example.lineup_app.presentation.filter_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.lineup_app.R
import com.example.lineup_app.common.Constants.sortOptions
import com.example.lineup_app.presentation.ui.theme.MyIcons

@Composable
fun SortMenu(
    currentSortOption: String,
    onSortOptionSelected: (String) -> Unit,
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
                text = stringResource(R.string.sort_by),
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = modifier.weight(1f)
            )

            Icon(
                imageVector = if (isSortMenuExpanded) MyIcons.arrowUp else MyIcons.arrowDown,
                contentDescription = stringResource(id = R.string.drop_down_arrow),
                modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
            )
        }

        if (isSortMenuExpanded) {
            Spacer(modifier = modifier.padding(dimensionResource(R.dimen.padding_small)))

            // ----------------------------------------------------------------------------------------- Sort options (name, date, distance, relevance)
            for (sortOption in sortOptions) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .fillMaxWidth()
                        .clickable {
                            onSortOptionSelected(sortOption)
                        }
                ) {
                    RadioButton(
                        selected = sortOption.lowercase() == currentSortOption.lowercase(),
                        onClick = {
                            onSortOptionSelected(sortOption)
                        }
                    )
                    Text(
                        text = sortOption,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier
                    )
                }
            }
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
        onExpandSortMenuDropdown = {}
    )
}