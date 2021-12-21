package nikitagorbatko.example.sankirtan


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp

val screens = listOf(BottomScreens.Books, BottomScreens.Briefcase, BottomScreens.Statistic)

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun MainScaffold(dao: BookDao) {
    var coroutineScope = rememberCoroutineScope()
    var snackbarHostState = remember { SnackbarHostState() }
    var onCreateBookDialog by remember { mutableStateOf(false) }
    var onEditBookDialog by remember { mutableStateOf(false) }
    var onAddBookDialog by remember { mutableStateOf(false) }
    val route = remember { mutableStateOf(screens[0]) }
    var books by remember { mutableStateOf(dao.getBooks()) }
    lateinit var bookForDialog: Book

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text(route.value.title) },
                elevation = 12.dp,
                actions = {
                    when (route.value) {
                        BottomScreens.Books -> {
                            IconButton(
                                onClick = { onCreateBookDialog = true },
                                content = { Icon(Icons.Filled.Add, contentDescription = null) },
                            )
                        }
                        BottomScreens.Briefcase -> {
                            IconButton(
                                onClick = { onAddBookDialog = true },
                                content = { Icon(Icons.Filled.Add, contentDescription = null) },
                            )
                        }
                        BottomScreens.Statistic -> {  }
                    }
                }
            )
        },
        content = {
            when (route.value) {
                BottomScreens.Books -> BooksScreen(books,true,) { book ->
                    onEditBookDialog = !onEditBookDialog
                    bookForDialog = book
                }
                BottomScreens.Briefcase -> BriefcaseScreen(books, coroutineScope, snackbarHostState) {
                    onAddBookDialog = !onAddBookDialog
                }
                BottomScreens.Statistic -> StatisticScreen()
            }

            if (onCreateBookDialog) {
                CreateBookDialog(dao, books, coroutineScope, snackbarHostState) {
                    onCreateBookDialog = !onCreateBookDialog
                    books = dao.getBooks()
                }
            }

            if (onEditBookDialog) {
                EditBookDialog(dao, bookForDialog, coroutineScope, snackbarHostState) {
                    onEditBookDialog = !onEditBookDialog
                    books = dao.getBooks()
                }
            }

            if (onAddBookDialog) {
                AddBookDialog(dao, books, coroutineScope, snackbarHostState) {
                    onAddBookDialog = !onAddBookDialog
                }
            }
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
        },
    )
}

