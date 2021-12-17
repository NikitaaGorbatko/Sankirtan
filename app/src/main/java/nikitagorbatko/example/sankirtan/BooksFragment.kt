package nikitagorbatko.example.sankirtan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun BooksScreen(books: List<Book>, a: Boolean, onClickDialog: (book: Book) -> Unit ){// onDelete: () -> Unit) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 60.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (a) { items(books) { item: Book -> if (item.amount > -1) BookCard(item, onClickDialog) } }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
private fun BookCard(book: Book, onClickDialog: (book: Book) -> Unit) {// onDeleteLambda: () -> Unit) -> Unit) {
    var visible by remember { mutableStateOf(true) }
    //AnimatedVisibility(visible = visible, exit = fadeOut()) {
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(16.dp),
        contentColor = Color.Black,
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
        onClick = { onClickDialog(book)} //{ visible = !visible } }
        //border = BorderStroke(0.dp, Color.Black),
    ) {
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
                book.cost.toString() + "₽",
                fontSize = TextUnit(22f, TextUnitType.Sp),
                modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp)
            )
        }
    //}
}
}

