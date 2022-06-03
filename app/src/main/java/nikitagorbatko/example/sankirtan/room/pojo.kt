package nikitagorbatko.example.sankirtan.room

import androidx.room.*

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
    var date: Int
)

@Entity(tableName = "day")
data class Day(
    @PrimaryKey val id: Int,
    val day: Int,
    val donation: Int,
)

data class DateHolder(val day: Int, val month: Int, val year: Int) {
    val intDate: Int
        get() = year * 10000 + month * 100 + day

    constructor(date: Int) : this(date % 100, date % 10000, date / 10000)
}

