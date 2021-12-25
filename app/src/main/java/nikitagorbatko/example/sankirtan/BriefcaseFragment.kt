package nikitagorbatko.example.sankirtan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseScreen(
    items: List<Item>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    addItemLambda: () -> Unit
) {
    LazyColumn(
        //modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
        //contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items) { item: Item -> BriefcaseBookCard(item, coroutineScope, snackbarHostState, addItemLambda) }
    }
}

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseBookCard(
    item: Item,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    addItemLambda: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(72.dp).clickable {
        addItemLambda()
        coroutineScope.launch {
            snackbarHostState.showSnackbar("", "OK", SnackbarDuration.Short)
        }
        //onClickDialog(book)
    }) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                item.name,
                //color = MaterialTheme.colors.onPrimary,
                fontSize = TextUnit(16f, TextUnitType.Sp),
                modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 2.dp)
            )
            Text(
                item.cost.toString() + "₽",
                color = Color(0x57FFFFFF),
                fontSize = TextUnit(14f, TextUnitType.Sp),
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 10.dp)
            )
        }
        Text(item.amount.toString() + "шт",
            color = Color(0X57FFFFFF),
            fontSize = TextUnit(14f, TextUnitType.Sp),
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 16.dp)
        )
    }
    Divider()
}
