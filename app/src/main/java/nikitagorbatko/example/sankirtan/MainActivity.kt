package nikitagorbatko.example.sankirtan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.ui.theme.SankirtanTheme


class MainActivity : ComponentActivity() {

    //with lambdas...
    val lambda = { x: Int, y: Int -> x + y}

    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = BookRoomDatabase.getDatabase(applicationContext).bookDao()

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
//            BuildDialog(mode = Mode.EDITION, dao = null, book = , books = ) {
//
//            }
//        }
//    }
}

