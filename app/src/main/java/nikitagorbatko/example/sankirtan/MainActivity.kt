package nikitagorbatko.example.sankirtan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.ui.theme.SankirtanTheme


class MainActivity : ComponentActivity() {

    val lambda = { x: Int, y: Int -> x + y}

    @ExperimentalMaterialApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = BookRoomDatabase.getDatabase(applicationContext)
        val dao = database.bookDao()
        val books = dao.getBooks()

        setContent {
            SankirtanTheme {
                MainScaffold(dao)
            }
        }
    }



//    @ExperimentalUnitApi
//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        SankirtanTheme {
//            //BookCard(Book(1, "Бхагавад-гита как она есть",1, 100))
//        }
//    }
}

