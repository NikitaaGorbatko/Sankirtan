package nikitagorbatko.example.sankirtan

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import nikitagorbatko.example.sankirtan.room.BookDataSource
import nikitagorbatko.example.sankirtan.room.BookRoomDatabase
import nikitagorbatko.example.sankirtan.ui.theme.SankirtanTheme


class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalComposeUiApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = BookRoomDatabase.getDatabase(applicationContext).bookDao()
        val bookDataSource = BookDataSource(dao)

        val model = ViewModelProvider(this).get(MainViewModel::class.java)

        setContent {
            SankirtanTheme {
                MainScaffold(bookDataSource, dao)//remove dao
            }
        }
    }
}

