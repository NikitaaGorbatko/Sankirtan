package nikitagorbatko.example.sankirtan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.room.Item
import nikitagorbatko.example.sankirtan.ui.theme.Gray

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseScreen(
    items: List<Item>,
    distributeItemLambda: (item: Item) -> Unit,
    deleteItemLambda: (item: Item) -> Unit
) {
    if (items.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                "Портфель пуст, нажмите \"+\" для добавления.   ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
            //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            //verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(items) { item: Item ->
                BriefcaseBookCard(
                    item,
                    distributeItemLambda,
                    deleteItemLambda
                )
            }
            if (items.isNotEmpty()) {
                item {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Нажмите на комплект, чтобы распространить.\nРезультат будет сохранен на сегодня.",
                            modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 76.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.body2,
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseBookCard(
    item: Item,
    addItemLambda: (item: Item) -> Unit,
    deleteItemLambda: (item: Item) -> Unit
) {
    var linesCount = remember { mutableStateOf(1) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(if (linesCount.value == 1) 72.dp else 88.dp)
            .clickable { addItemLambda(item) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                onTextLayout = { linesCount.value = it.lineCount },
                maxLines = 2,
                fontSize = TextUnit(16f, TextUnitType.Sp),
                modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 2.dp)
            )
            Text(
                "${item.amount} по ${item.cost}р = ${item.amount * item.cost}р",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
            )
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(top = 3.dp), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End) {
            IconButton(onClick = { deleteItemLambda(item) }) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp, 18.dp)
                        .padding(0.dp),
                    tint = Gray,
                )
            }
//            Text(item.amount.toString() + "шт",
//                style = MaterialTheme.typography.body2,
//                textAlign = TextAlign.End,
//                modifier = Modifier
//                    .padding(16.dp, 0.dp, 16.dp, 10.dp)
//                    .fillMaxWidth()
//            )
        }
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}
