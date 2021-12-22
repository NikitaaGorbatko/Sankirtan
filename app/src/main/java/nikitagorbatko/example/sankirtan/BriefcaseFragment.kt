package nikitagorbatko.example.sankirtan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseScreen(
    items: List<Item>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    addBookLambda: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items) { item: Item -> BriefcaseBookCard(item, coroutineScope, snackbarHostState) }
    }
}

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseBookCard(
    item: Item,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                item.name,
                fontSize = TextUnit(20f, TextUnitType.Sp),
                modifier = Modifier
                    .padding(16.dp, 16.dp, 16.dp, 8.dp)
                    .fillMaxWidth(0.7f),
                //color = Color.Blue
            )
            Text(
                "${item.cost} руб",
                fontSize = TextUnit(16f, TextUnitType.Sp),
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)
            )
        }
        Text(
            "${item.amount} шт",
            fontSize = TextUnit(20f, TextUnitType.Sp),
            modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
            maxLines = 1
        )
    }
    Divider()
}
