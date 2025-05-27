package com.example.concertfinder.presentation.app.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.concertfinder.R

@Composable
fun FloatingAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
) {

    val context = LocalContext.current

    Card(
        onClick = {
            onClick()
            // show toast if event is saved or unsaved
            // TODO: make these snackbars
            if (filled == false) {
                Toast.makeText(context, context.getString(R.string.event_saved), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context.getString(R.string.event_unsaved), Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier
            .size(dimensionResource(id = R.dimen.fab_size))
            .clip(MaterialTheme.shapes.large)
    ) {
        if (!filled) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = stringResource(id = R.string.save_event),
                modifier = modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = stringResource(id = R.string.save_event),
                modifier = modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxSize()
            )
        }
    }
}