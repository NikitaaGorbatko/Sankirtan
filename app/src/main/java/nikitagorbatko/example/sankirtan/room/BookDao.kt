package nikitagorbatko.example.sankirtan.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import nikitagorbatko.example.sankirtan.room.Book
import nikitagorbatko.example.sankirtan.room.DistributedItem
import nikitagorbatko.example.sankirtan.room.Item

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
    fun insertDistributedItem(name: String, cost: Int, amount: Int, date: Int): Long

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

    @Query("SELECT * FROM day")
    fun getDays(): MutableList<Day>

    @Query("INSERT INTO day (day, donation) VALUES(:day, :donation)")
    fun insertDay(day: Int, donation: Int): Long

    @Query("UPDATE day SET donation = :donation WHERE id = :dayId")
    fun updateDay(dayId: Int, donation: Int): Int

    @Delete
    fun deleteDay(item: Day): Int

}