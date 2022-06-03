package nikitagorbatko.example.sankirtan.room

import kotlinx.coroutines.Dispatchers

class BookDataSource(private val bookDao: BookDao) {
    val books = bookDao.getBooks()
    val items = bookDao.getItems()
    val distributedItems = bookDao.getDistributedItems()
    val days = bookDao.getDays()


    suspend fun insertBook(name: String, cost: Int) {
        Dispatchers.IO.apply {
            bookDao.insertBook(name, cost)
        }
    }

}