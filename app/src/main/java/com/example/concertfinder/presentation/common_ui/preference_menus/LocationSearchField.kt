package com.example.concertfinder.presentation.common_ui.preference_menus

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.example.concertfinder.R

@Composable
fun LocationSearchField(
    locationSearchQuery: String,
    onGetLocation: () -> Unit,
    onLocationQueryUpdate: (String) -> Unit,
    onLocationSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
}