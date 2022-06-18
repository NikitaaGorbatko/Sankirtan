package nikitagorbatko.example.sankirtan

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
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

        setContent {
            SankirtanTheme {
                MainScaffold(bookDataSource, dao, intentLambda = { text ->
                    val sharingIntent = Intent(Intent.ACTION_SEND).also {
                        it.type = "text/plain"
                        it.putExtra(Intent.EXTRA_TEXT, text)
                    }
                    val intent1 = Intent.createChooser(sharingIntent, "Отправить через..")
                    startActivity(intent1)
                })//remove dao
            }
        }

    }
}

