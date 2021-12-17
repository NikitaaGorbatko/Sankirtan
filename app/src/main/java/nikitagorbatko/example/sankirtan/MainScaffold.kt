package nikitagorbatko.example.sankirtan


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun MainScaffold(dao: BookDao) {
    val screens = listOf(BottomScreens.Books, BottomScreens.Briefcase, BottomScreens.Statistic)
    var onDialog by remember { mutableStateOf(false) }
    val route = remember { mutableStateOf(screens[0]) }
    var books by remember { mutableStateOf(dao.getBooks()) }
    var localBook: Book? = null
    var animate: () -> Unit = {}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(route.value.title) },
                elevation = 12.dp,
                actions = {
                    when (route.value) {
                        BottomScreens.Books -> {
                            IconButton(
                                onClick = {
                                    onDialog = true
                                },
                                content = { Icon(Icons.Filled.Add, contentDescription = null) },
                            )
                        }
                        BottomScreens.Briefcase -> {
                            IconButton(
                                onClick = { onDialog = true },
                                content = { Icon(Icons.Filled.Add, contentDescription = null) },
                            )
                        }
                        BottomScreens.Statistic -> {}
                    }
                }
            )
        },
        content = {
            when (route.value) {
                BottomScreens.Books -> BooksScreen(
                    books,
                    true,
                    { book ->//lam: () -> Unit ->
                        localBook = book
                        onDialog = !onDialog
                        //animate = lam
                    },

                )
                BottomScreens.Briefcase -> BriefcaseScreen(books, true)
                BottomScreens.Statistic -> StatisticScreen()
            }

            if (onDialog) {
                CreateBookDialog(
                    dao,
                    books,
                    closeLambda = {
                        onDialog = !onDialog
                        books = dao.getBooks()
                    },
                )
            }
            localBook = null
        },
        bottomBar = {
            BottomNavigation(Modifier.height(60.dp)) {
                screens.forEach {
                        screen ->
                    BottomNavigationItem(
                        icon = { Icon(painterResource(id = screen.icon) , contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = screen == route.value,
                        onClick = { route.value = screen }
                    )
                }
            }
        }
    )
}

