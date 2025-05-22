package com.example.concertfinder.presentation.common_ui.preference_menus

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.example.concertfinder.R
import com.example.concertfinder.common.Constants.radiusOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadiusDropdown(
    radius: String,
    isRadiusPreferencesExpanded: Boolean,
    onExposeRadiusDropdownChange: (Boolean) -> Unit,
    onRadiusOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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