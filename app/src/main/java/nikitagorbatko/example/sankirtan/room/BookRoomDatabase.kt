package nikitagorbatko.example.sankirtan.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import nikitagorbatko.example.sankirtan.room.Book
import nikitagorbatko.example.sankirtan.room.DistributedItem
import nikitagorbatko.example.sankirtan.room.Item

@Database(entities = [Book::class, Item::class, DistributedItem::class, Day::class], version = 1, exportSchema = false)
abstract class BookRoomDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookRoomDatabase? = null

        fun getDatabase(context: Context): BookRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookRoomDatabase::class.java,
                    "books.db"
                ).allowMainThreadQueries()
                    .createFromAsset("books.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}