package com.example.lineup_app.presentation.common_ui.elements

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
import com.example.lineup_app.R

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
    showValue: Boolean = true,
    enabled: Boolean = true
) {
    ExposedDropdownMenuBox(
        expanded = isPreferencesExpanded,
        onExpandedChange = {
            if (enabled) {
                onPreferencesExpandedChange(!isPreferencesExpanded)
            }
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
        if (showValue) {
            TextField(
                value = if (preferenceLabel != null) {
                    if (currentPreference == "") {
                        "Unlimited"
                    } else {
                        "$currentPreference $preferenceLabel"
                    }
                } else {
                    currentPreference
                },
                onValueChange = {}, // No-op for read-only
                readOnly = true,
                label = {Text(dropdownLabel)},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPreferencesExpanded)
                },
                enabled = enabled,
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable) // Important: Links TextField to the DropdownMenu
            )
        } else {
            TextField(
                value = dropdownLabel,
                onValueChange = {}, // No-op for read-only
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPreferencesExpanded)
                },
                enabled = enabled,
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable) // Important: Links TextField to the DropdownMenu
            )
        }

        // The actual dropdown menu
        ExposedDropdownMenu(
            expanded = if (enabled) isPreferencesExpanded else false,
            onDismissRequest = { onPreferencesExpandedChange(false) }
        ) {
            preferenceOptions.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        if (preferenceLabel != null) {
                            if (selectionOption == "") {
                                Text("Unlimited")
                            } else {
                                Text("$selectionOption $preferenceLabel")
                            }
                        } else {
                            Text(selectionOption)
                        }
                    },
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