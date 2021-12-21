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
    books: List<Book>,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    addBookLambda: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(books) { item: Book -> BriefcaseBookCard(item, coroutineScope, snackbarHostState) }
    }
}

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BriefcaseBookCard(
    book: Book,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
//    Card(
//        elevation = 10.dp,
//        shape = RoundedCornerShape(16.dp),
//        contentColor = Color.Black,
//        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
//        onClick = {
//            coroutineScope.launch {
//                snackbarHostState.showSnackbar(book.name, "Label", SnackbarDuration.Short)
//            }
//        }
//        //border = BorderStroke(0.dp, Color.Black),
//    ) {
        //Spacer(modifier = Modifier.width(1000.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            book.name,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 16.dp)
                .fillMaxWidth(0.7f),
            //color = Color.Blue
        )
        Text(
            "шт",
            fontSize = TextUnit(22f, TextUnitType.Sp),
            modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp)
        )
    }
    Divider()
    //}
}
