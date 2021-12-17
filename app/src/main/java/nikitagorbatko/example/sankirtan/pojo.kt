package nikitagorbatko.example.sankirtan

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.*

@Entity(tableName = "book")
data class Book(@PrimaryKey(autoGenerate = true) val id: Int, val name: String, val cost: Int, val amount: Int)

data class NameAndCostAndAmount(val name: String, val cost: Int, val amount: Int = 10)

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): MutableList<Book>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertBook(book: Book)

    @Query("INSERT INTO book (name, cost, amount) VALUES(:name, :cost, :amount)")
    fun insertBook(name: String, cost: Int, amount: Int)

    @Query("UPDATE book SET name = :newName, cost = :newCost WHERE id = :bookId")
    fun updateBook(newName: String, newCost: Int, bookId: Int)

    @Delete
    fun deleteBook(book: Book)
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