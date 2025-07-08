package com.example.concertfinder.presentation.common_ui.location_menu.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.concertfinder.R
import kotlin.math.roundToInt

val radiusValues = listOf(
    "10", "20", "30", "40", "50", "60", "70", "80", "90", "100",
    "150", "200", "250", "300",
    "500",
    "1000",
    ""
)

val radiusDisplayValues = listOf(
    "10", "20", "30", "40", "50", "60", "70", "80", "90", "100",
    "150", "200", "250", "300",
    "500",
    "1000",
    "Unlimited"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadiusSlider(
    modifier: Modifier = Modifier,
    initialRadius: String = radiusValues.last(), // Default to unlimited
    onRadiusChange: (String) -> Unit
) {
    // Find the initial index, default to 0 if not found (though it should be)
    val initialIndex = radiusValues.indexOf(initialRadius).coerceAtLeast(0)
    var sliderPositionIndex by remember { mutableFloatStateOf(initialIndex.toFloat()) }

    // Derived state for the actual selected radius based on the slider's index
    val selectedRadius = radiusValues[sliderPositionIndex.roundToInt().coerceIn(0, radiusValues.lastIndex)]
    val selectedRadiusDisplay = radiusDisplayValues[sliderPositionIndex.roundToInt().coerceIn(0, radiusValues.lastIndex)]

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_small)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Radius:")
            Text(selectedRadiusDisplay + if (selectedRadius != "") " mi" else "") // Add units if not "Unlimited"
        }
        Slider(
            value = sliderPositionIndex,
            onValueChange = { newIndex ->
                sliderPositionIndex = newIndex
            },
            valueRange = 0f..(radiusValues.size - 1).toFloat(),
            steps = radiusValues.size - 2, // Number of gaps between steps
            onValueChangeFinished = {
                // This is called when the user finishes dragging the slider
                val finalIndex = sliderPositionIndex.roundToInt().coerceIn(0, radiusValues.lastIndex)
                sliderPositionIndex = finalIndex.toFloat() // Snap to the discrete value
                onRadiusChange(radiusValues[finalIndex])
            },
            thumb = {
                // You can customize the thumb if needed
                SliderDefaults.Thumb(interactionSource = remember { MutableInteractionSource() })
            },
            // You can also customize track, ticks etc.
        )
    }
}
