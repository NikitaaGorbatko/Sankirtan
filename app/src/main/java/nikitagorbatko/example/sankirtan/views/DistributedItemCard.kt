package nikitagorbatko.example.sankirtan.views

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.room.DistributedItem
import nikitagorbatko.example.sankirtan.ui.theme.Gray

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun DistributedItemCard(
    item: DistributedItem,
    deleteDistributedItem: (item: DistributedItem) -> Unit,
) {
    var linesCount = remember { mutableStateOf(1) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .height(if (linesCount.value == 1) 72.dp else 88.dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 3.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            IconButton(onClick = {
                deleteDistributedItem(item)
            }) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp, 18.dp)
                        .padding(0.dp),
                    tint = Gray,
                )
            }
        }
    }
    Divider(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
}

@ExperimentalUnitApi
@Composable
fun FakeItem(title: String, body: String, onClick: () -> Unit, expanded: Boolean) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .paddingFrom(alignmentLine = FirstBaseline, 40.dp)
            .height(72.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                maxLines = 1,
                style = MaterialTheme.typography.h6,
                //fontSize = TextUnit(16f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(24.dp, 0.dp, 0.dp, 0.dp)
            )
            IconButton(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .size(40.dp),
                onClick = { onClick() }
            ) {
                if (expanded) {
                    Icon(Icons.Rounded.ExpandLess, "", modifier = Modifier.size(30.dp))
                } else {
                    Icon(Icons.Rounded.ExpandMore, "", modifier = Modifier.size(30.dp))
                }
            }
        }
    }
}