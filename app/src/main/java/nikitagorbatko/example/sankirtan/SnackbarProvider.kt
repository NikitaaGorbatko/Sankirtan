package nikitagorbatko.example.sankirtan

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackbarProvider(
    private val coroutineScope: CoroutineScope,
    private val snackbarHostState: SnackbarHostState) {

    fun show(message: String, label: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message, label, SnackbarDuration.Short)
        }
    }
}

//open class SingletonHolder<out T: Any, in A>(creator: (A) -> T) {
//    private var creator: ((A) -> T)? = creator
//    @Volatile private var instance: T? = null
//
//    fun getInstance(arg: A): T {
//        val checkInstance = instance
//        if (checkInstance != null) {
//            return checkInstance
//        }
//
//        return synchronized(this) {
//            val checkInstanceAgain = instance
//            if (checkInstanceAgain != null) {
//                checkInstanceAgain
//            } else {
//                val created = creator!!(arg)
//                instance = created
//                creator = null
//                created
//            }
//        }
//    }
//}