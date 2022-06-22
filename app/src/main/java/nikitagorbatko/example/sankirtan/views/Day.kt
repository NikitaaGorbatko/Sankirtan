package nikitagorbatko.example.sankirtan.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.CalendarProvider
import nikitagorbatko.example.sankirtan.room.DateHolder
import nikitagorbatko.example.sankirtan.room.DistributedItem

enum class Style { BODY1, BODY2 }

@Composable
fun Day(
    num: String = "",
    style: Style = Style.BODY1,
    clickable: Boolean = false,
    onClick: (date: Int, list: List<DistributedItem>) -> Unit = { _, _ -> },
    list: List<DistributedItem> = listOf(),
    clicked: Boolean = false
) {
    val modifier: Modifier = if (clickable) {
        val date = DateHolder(num.toInt(), CalendarProvider.monthNum, CalendarProvider.year)
        Modifier
            .size(38.dp)
            .padding(1.dp)
            .clip(CircleShape)
            .background(if (clicked) Color.LightGray else Color.Unspecified)
            .clickable { onClick(date.intDate, list) }
            .border(
                if (list.isNotEmpty()) BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onPrimary
                ) else BorderStroke(
                    0.dp,
                    Color.Unspecified
                ), shape = CircleShape
            )
    } else {
        Modifier
            .size(38.dp)
            .padding(1.dp)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = num,
            style = if (style == Style.BODY1) {
                MaterialTheme.typography.body1
            } else {
                MaterialTheme.typography.body2
            },
            textAlign = TextAlign.Center
        )
    }
}