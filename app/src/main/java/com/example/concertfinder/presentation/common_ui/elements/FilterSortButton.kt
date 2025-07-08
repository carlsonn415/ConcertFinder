package com.example.concertfinder.presentation.common_ui.elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.concertfinder.R
import com.example.concertfinder.presentation.ui.theme.MyIcons

@Composable
fun FilterSortButton(
    onFilterSortClicked: () -> Unit,
    visible: Boolean,
    showText: Boolean = true
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(durationMillis = 100)),
        exit = fadeOut(tween(durationMillis = 100))
    ) {
        if (showText) {// You can use a TextButton, Button, ElevatedButton, IconButton etc.
            ElevatedButton( // ElevatedButton gives it a bit of shadow
                onClick = onFilterSortClicked,
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    imageVector = MyIcons.filter,
                    contentDescription = stringResource(R.string.filter),
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.filter))
            }
        } else {
            IconButton(
                onClick = onFilterSortClicked,
                colors = IconButtonDefaults.iconButtonColors(),
            ) {
                Icon(
                    imageVector = MyIcons.filter,
                    contentDescription = stringResource(R.string.filter),
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }
    }
}