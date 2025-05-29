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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesDropdown(
    currentPreference: String,
    dropdownLabel: String,
    preferenceOptions: List<String>,
    isPreferencesExpanded: Boolean,
    onPreferencesExpandedChange: (Boolean) -> Unit,
    onPreferenceSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    preferenceLabel: String? = null,
) {
    ExposedDropdownMenuBox(
        expanded = isPreferencesExpanded,
        onExpandedChange = {
            onPreferencesExpandedChange(!isPreferencesExpanded)
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
            value = if (preferenceLabel != null) "$currentPreference $preferenceLabel" else currentPreference,
            onValueChange = {}, // No-op for read-only
            readOnly = true,
            label = { Text(dropdownLabel) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPreferencesExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable) // Important: Links TextField to the DropdownMenu
        )

        // The actual dropdown menu
        ExposedDropdownMenu(
            expanded = isPreferencesExpanded,
            onDismissRequest = { onPreferencesExpandedChange(false) }
        ) {
            preferenceOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { if (preferenceLabel != null) Text("$selectionOption $preferenceLabel") else Text(selectionOption) },
                    onClick = {
                        onPreferenceSelected(selectionOption)
                        onPreferencesExpandedChange(false) // Close the menu after selection
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}