package nikitagorbatko.example.sankirtan

import androidx.room.*
import kotlinx.serialization.Serializable
import java.util.*

@Entity(tableName = "book")
data class Book(
    @PrimaryKey val id: Int,
    val name: String,
    val cost: Int
)

@Entity(tableName = "item")
data class Item(
    @PrimaryKey val id: Int,
    val name: String,
    val cost: Int,
    var amount: Int,
)

@Entity(tableName = "distributed")
data class DistributedItem(
    @PrimaryKey val id: Int,
    val name: String,
    val cost: Int,
    var amount: Int,
    var date: Long
)

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getBooks(): MutableList<Book>

    @Query("INSERT INTO book (name, cost) VALUES(:name, :cost)")
    fun insertBook(name: String, cost: Int): Long

    @Query("UPDATE book SET name = :newName, cost = :newCost WHERE id = :bookId")
    fun updateBook(bookId: Int, newName: String, newCost: Int): Int

    @Delete
    fun deleteBook(book: Book): Int

    @Query("SELECT * FROM distributed")
    fun getDistributedItems(): MutableList<DistributedItem>

    @Query("INSERT INTO distributed (name, cost, amount, date) VALUES(:name, :cost, :amount, :date)")
    fun insertDistributedItem(name: String, cost: Int, amount: Int, date: Long): Long

//    @Query("UPDATE distributed_item SET amount = :amount WHERE id = :itemId")
//    fun updateDistributedItem(itemId: Int, amount: Int): Int

    @Delete
    fun deleteDistributedItem(distributed_item: Item): Int

    @Query("SELECT * FROM item")
    fun getItems(): MutableList<Item>

    @Query("INSERT INTO item (name, cost, amount) VALUES(:name, :cost, :amount)")
    fun insertItem(name: String, cost: Int, amount: Int): Long

    @Query("UPDATE item SET amount = :amount WHERE id = :itemId")
    fun updateItem(itemId: Int, amount: Int): Int

    @Delete
    fun deleteItem(item: Item): Int
}

sealed class BottomScreens(val title: String, val icon: Int) {
    object Books : BottomScreens("Книги", R.drawable.ic_books)
    object Briefcase : BottomScreens("Портфель", R.drawable.ic_briefcase)
    object Statistic : BottomScreens("Статистика", R.drawable.ic_statistic)
}