package nikitagorbatko.example.sankirtan.views

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.room.DistributedItem

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun DistributedItemCard(book: DistributedItem) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
        //.clickable { onEditDialog(book) }
    ) {
        Text(
            book.name,
            maxLines = 1,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 2.dp)
        )
        Text(
            "${book.amount} по ${book.cost}р = ${book.amount * book.cost}р",
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
        )
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@ExperimentalUnitApi
@Composable
fun FakeItem(title: String, body: String, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() }
        //.clickable { onEditDialog(book) }
    ) {
        Text(
            text = title,
            maxLines = 1,
            fontSize = TextUnit(16f, TextUnitType.Sp),
            modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 2.dp)
        )
        Text(
            text = body,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
        )
    }
}