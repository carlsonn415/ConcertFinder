package com.example.lineup_app.presentation.app.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.lineup_app.R
import com.example.lineup_app.presentation.ui.theme.MyIcons

@Composable
fun FloatingAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
) {
    ElevatedCard(
        onClick = {
            onClick()
        },
        modifier = modifier
            .size(dimensionResource(id = R.dimen.fab_size))
            .clip(MaterialTheme.shapes.large)
    ) {
        if (!filled) {
            Icon(
                imageVector = MyIcons.heartOutlined,
                contentDescription = stringResource(id = R.string.save_event),
                modifier = modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxSize()
            )
        } else {
            Icon(
                imageVector = MyIcons.heart,
                contentDescription = stringResource(id = R.string.save_event),
                modifier = modifier
                    .padding(dimensionResource(R.dimen.padding_medium))
                    .fillMaxSize()
            )
        }
    }
}