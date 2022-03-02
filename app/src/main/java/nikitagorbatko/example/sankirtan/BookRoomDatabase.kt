package nikitagorbatko.example.sankirtan

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Book::class, Item::class, DistributedItem::class], version = 1, exportSchema = false)
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