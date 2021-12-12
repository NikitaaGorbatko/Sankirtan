package nikitagorbatko.example.sankirtan

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.*

@Entity(tableName = "book")
data class Book(@PrimaryKey val id: Int, val name: String, val cost: Int, val amount: Int)

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): MutableList<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(book: Book)
}

//enum class Route(val string: String) {
//    BOOKS("Все книги"),
//    BRIEFCASE("Портфель"),
//    STATISTIC("Статистика")
//}


sealed class BottomScreens(val title: String, val icon: Int) {
    object Books : BottomScreens("Книги", R.drawable.ic_books)
    object Briefcase : BottomScreens("Портфель", R.drawable.ic_briefcase)
    object Statistic : BottomScreens("Статистика", R.drawable.ic_statistic)
}