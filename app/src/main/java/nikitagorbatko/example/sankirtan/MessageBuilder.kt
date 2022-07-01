package nikitagorbatko.example.sankirtan

import nikitagorbatko.example.sankirtan.room.DateHolder
import nikitagorbatko.example.sankirtan.room.DistributedItem

object MessageBuilder {
    private val stringBuilder = StringBuilder()
    fun build(
        distributedDayList: List<DistributedItem>,
        holder: DateHolder,
        totalCost: Int,
        donation: Int,
        clickedDate: Int
    ): String {
        stringBuilder.clear()//StringBuffer in other thread is preffered
        stringBuilder.append("${holder.stringDate()}\n\n")
        distributedDayList.forEach {
            if (it.date == clickedDate) {
                stringBuilder
                    .append("${it.name}:\n${it.amount}шт по ${it.cost}руб = ${it.cost * it.amount}руб.\n")
            }
        }
        stringBuilder.append(
            "\nОптовая цена: ${totalCost}руб\nПожертвование: ${donation}руб\nРезультат: ${
                try {
                    "${donation - totalCost}руб"
                } catch (_: Exception) {
                    "${-totalCost}руб"
                }
            }"
        )
        return stringBuilder.toString()
    }
}