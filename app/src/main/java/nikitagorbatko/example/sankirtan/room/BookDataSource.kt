package nikitagorbatko.example.sankirtan.room

import kotlinx.coroutines.Dispatchers

class BookDataSource(private val bookDao: BookDao) {
    //var book by remember { mutableStateOf(bookDao.getBooks()) }
    val books = bookDao.getBooks()
    val items = bookDao.getBriefcaseItems()
    val distributedItems = bookDao.getDistributedItems()
    val days = bookDao.getDays()


    suspend fun insertBook(name: String, cost: Int) {
        Dispatchers.IO.apply {
            bookDao.insertBook(name, cost)
        }
    }

}