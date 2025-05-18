package com.example.concertfinder.ui.commonui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R
import com.example.concertfinder.model.uistate.ConcertFinderUiState
import com.example.concertfinder.model.Radius
import com.example.concertfinder.model.uistate.SearchScreenUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMenu(
    uiState: ConcertFinderUiState,
    searchScreenUiState: SearchScreenUiState,
    onExpandLocationDropdown: (Boolean) -> Unit,
    onGetCurrentLocation: () -> Unit,
    onExposeRadiusDropdownChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit,
    onLocationQueryUpdate: (String) -> Unit,
    onLocationSearch: (String) -> Unit,
    radiusOptions: List<Radius>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .clickable {
                        onExpandLocationDropdown(!searchScreenUiState.isLocationPreferencesMenuExpanded)
                    }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                        vertical = dimensionResource(R.dimen.padding_small)
                    )
            ) {
                Text(
                    text = uiState.currentAddress,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    modifier = modifier.weight(1f)
                )
                if (searchScreenUiState.isLocationPreferencesMenuExpanded) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowUp,
                        contentDescription = stringResource(id = R.string.drop_down_arrow),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        contentDescription = stringResource(id = R.string.drop_down_arrow),
                        modifier = modifier.padding(start = dimensionResource(R.dimen.padding_small))
                    )
                }
            }

            if (searchScreenUiState.isLocationPreferencesMenuExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .padding(bottom = dimensionResource(R.dimen.padding_small))
                ) {
                    // TODO: add support for location search
                    OutlinedTextField(
                        value = searchScreenUiState.locationSearchQuery,
                        onValueChange = { onLocationQueryUpdate(it) },
                        label = {
                            Text(
                                text = stringResource(R.string.search_for_city_state_or_zip_code),
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onGetCurrentLocation()
                                },
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.padding_small))
                                    .clip(MaterialTheme.shapes.small)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = stringResource(id = R.string.update_location),
                                    modifier = Modifier
                                        .size(dimensionResource(R.dimen.icon_button_size))
                                        .fillMaxSize()
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                onLocationSearch(searchScreenUiState.locationSearchQuery)
                            }
                        ),
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        modifier = modifier
                            .weight(1f)
                            .height(dimensionResource(R.dimen.location_textfield_height))
                            .fillMaxHeight()
                    )
                }

                Spacer(modifier = modifier.height(dimensionResource(R.dimen.padding_medium)))

                ExposedDropdownMenuBox(
                    expanded = searchScreenUiState.isRadiusPreferencesExpanded,
                    onExpandedChange = {
                        onExposeRadiusDropdownChange(!searchScreenUiState.isRadiusPreferencesExpanded)
                    },
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = dimensionResource(R.dimen.padding_medium)
                        )
                        .clip(MaterialTheme.shapes.small)
                ) {
                    // TextField that displays the selected option
                    // Make it read-only to prevent manual text input
                    // Use .menuAnchor() to connect it to the dropdown menu
                    TextField(
                        value = uiState.searchRadius + " Miles",
                        onValueChange = {}, // No-op for read-only
                        readOnly = true,
                        label = { Text("Select a radius to search") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = searchScreenUiState.isRadiusPreferencesExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable) // Important: Links TextField to the DropdownMenu
                    )

                    // The actual dropdown menu
                    ExposedDropdownMenu(
                        expanded = searchScreenUiState.isRadiusPreferencesExpanded,
                        onDismissRequest = { onExposeRadiusDropdownChange(false) }
                    ) {
                        radiusOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.radius + " " + selectionOption.unit) },
                                onClick = {
                                    onOptionSelected(selectionOption.radius)
                                    onExposeRadiusDropdownChange(false) // Close the menu after selection
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationMenuPreview() {
    LocationMenu(
        uiState = ConcertFinderUiState(
            currentAddress = "123 Main St, Anytown, USA",
            searchRadius = "50"
        ),
        searchScreenUiState = SearchScreenUiState(
            isLocationPreferencesMenuExpanded = true,
            isRadiusPreferencesExpanded = false,
        ),
        onExpandLocationDropdown = {},
        onOptionSelected = {},
        onExposeRadiusDropdownChange = {},
        onGetCurrentLocation = {},
        onLocationQueryUpdate = {},
        onLocationSearch = {},
        radiusOptions = listOf<Radius>(
            Radius("10", "Miles"),
            Radius("25", "Miles"),
            Radius("50", "Miles"),
            Radius("75", "Miles"),
            Radius("100", "Miles"),
        )
    )
}