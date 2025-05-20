package com.example.concertfinder.presentation.common_ui

import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants.radiusOptions
import com.example.concertfinder.domain.model.LoadingStatus
import com.example.concertfinder.presentation.search_screen.SearchScreenUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationMenu(
    address: String,
    radius: String,
    isLocationPreferencesMenuExpanded: Boolean,
    locationSearchQuery: String,
    isRadiusPreferencesExpanded: Boolean,
    locationLoadingStatus: LoadingStatus,
    onExpandLocationDropdown: (Boolean) -> Unit,
    onGetLocation: () -> Unit,
    onExposeRadiusDropdownChange: (Boolean) -> Unit,
    onRadiusOptionSelected: (String) -> Unit,
    onLocationQueryUpdate: (String) -> Unit,
    onLocationSearch: (String) -> Unit,
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
                        onExpandLocationDropdown(!isLocationPreferencesMenuExpanded)
                    }
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                        vertical = dimensionResource(R.dimen.padding_small)
                    )
            ) {
                Text(
                    text = if (locationLoadingStatus == LoadingStatus.Loading) {
                        stringResource(R.string.getting_location)
                    } else {
                        stringResource(R.string.searching_in) + address
                    },
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    modifier = modifier.weight(1f)
                )
                if (isLocationPreferencesMenuExpanded) {
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

            if (isLocationPreferencesMenuExpanded) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        .padding(bottom = dimensionResource(R.dimen.padding_small), top = dimensionResource(R.dimen.padding_small))
                ) {
                    OutlinedTextField(
                        value = locationSearchQuery,
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
                                    onGetLocation()
                                },
                                modifier = Modifier
                                    .padding(dimensionResource(R.dimen.padding_small))
                                    .clip(MaterialTheme.shapes.small)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn, // TODO: Replace with location icon
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
                                onLocationSearch(locationSearchQuery)
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
                    expanded = isRadiusPreferencesExpanded,
                    onExpandedChange = {
                        onExposeRadiusDropdownChange(!isRadiusPreferencesExpanded)
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
                        value = "$radius Miles",
                        onValueChange = {}, // No-op for read-only
                        readOnly = true,
                        label = { Text("Select a radius to search") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRadiusPreferencesExpanded)
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable) // Important: Links TextField to the DropdownMenu
                    )

                    val scope = rememberCoroutineScope()

                    // The actual dropdown menu
                    ExposedDropdownMenu(
                        expanded = isRadiusPreferencesExpanded,
                        onDismissRequest = { onExposeRadiusDropdownChange(false) }
                    ) {
                        radiusOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.radius + " " + selectionOption.unit) },
                                onClick = {
                                    onRadiusOptionSelected(selectionOption.radius)
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
        address = "123 Main St",
        radius = "10",
        locationLoadingStatus = LoadingStatus.Success,
        isLocationPreferencesMenuExpanded = true,
        locationSearchQuery = "New York",
        isRadiusPreferencesExpanded = true,
        onExpandLocationDropdown = {},
        onRadiusOptionSelected = {},
        onExposeRadiusDropdownChange = {},
        onGetLocation = {},
        onLocationQueryUpdate = {},
        onLocationSearch = {},
    )
}