package nikitagorbatko.example.sankirtan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.room.Book
import nikitagorbatko.example.sankirtan.room.BookDao
import nikitagorbatko.example.sankirtan.room.BookDataSource
import nikitagorbatko.example.sankirtan.room.Item
import nikitagorbatko.example.sankirtan.views.StatisticScreen

val screens = listOf(BottomScreens.Books, BottomScreens.Briefcase, BottomScreens.Statistic)

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun MainScaffold(
    bookDataSource: BookDataSource,
    dao: BookDao,
    intentLambda: (text: String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var onCreateBookDialog by remember { mutableStateOf(false) }
    var onEditBookDialog by remember { mutableStateOf(false) }
    var onCreateItemDialog by remember { mutableStateOf(false) }
    var onEditItemDialog by remember { mutableStateOf(false) }
    var onDeleteItemDialog by remember { mutableStateOf(false) }
    var onAddDistributedItemDialog by remember { mutableStateOf(false) }

    var route by remember { mutableStateOf(screens[0]) }
    var books by remember { mutableStateOf(bookDataSource.books) }
    var items by remember { mutableStateOf(bookDataSource.items) }
    var distributedItems by remember { mutableStateOf(bookDataSource.distributedItems) }
    var days by remember { mutableStateOf(bookDataSource.days) }
    var dayForDistribution = 0

    lateinit var bookForDialog: Book
    lateinit var itemForDialog: Item
    var totalCost = 0

    items.forEach { totalCost += it.cost * it.amount }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        route.title + if (route == BottomScreens.Briefcase) {
                            " ($totalCost руб)"
                        } else ""
                    )
                },
                elevation = 12.dp,
                actions = {
                    IconButton(
                        onClick = {
                            when (route) {
                                BottomScreens.Books -> {
                                    onCreateBookDialog = true
                                }
                                BottomScreens.Briefcase -> {
                                    onCreateItemDialog = true
                                }
                                BottomScreens.Statistic -> {
                                    onAddDistributedItemDialog = true
                                    dayForDistribution = 8
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                    )
                }
            )
        },
        content = {
            when (route) {
                BottomScreens.Books -> BooksScreen(books) {
                    bookForDialog = it
                    onEditBookDialog = !onEditBookDialog
                }
                BottomScreens.Briefcase -> BriefcaseScreen(
                    items,
                    distributeItemLambda = {
                        itemForDialog = it
                        onEditItemDialog = !onEditItemDialog
                    },
                    deleteItemLambda = {
                        //dao.deleteItem(it)
                        itemForDialog = it
                        onDeleteItemDialog = !onDeleteItemDialog
                    }
                )

                BottomScreens.Statistic -> StatisticScreen(
                    distributedItems = distributedItems,
                    days = days,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    dao = dao,
                    insertDayLambda = {
                        days = dao.getDays()
                    },
                    liftStringLambda = {
                        intentLambda(it)
                    }
                )
            }

            if (onCreateBookDialog) {
                CreateBookDialog(dao, coroutineScope, snackbarHostState) {
                    onCreateBookDialog = !onCreateBookDialog
                    books = dao.getBooks()
                }
            }

            if (onDeleteItemDialog) {
                DeleteItemDialog(dao, itemForDialog, coroutineScope, snackbarHostState) {
                    onDeleteItemDialog = !onDeleteItemDialog
                    items = dao.getItems()
                }
            }

            if (onEditBookDialog) {
                EditBookDialog(dao, bookForDialog, coroutineScope, snackbarHostState) {
                    onEditBookDialog = !onEditBookDialog
                    books = dao.getBooks()
                }
            }

            if (onCreateItemDialog) {
                CreateItemDialog(dao, books, coroutineScope, snackbarHostState) {
                    onCreateItemDialog = !onCreateItemDialog
                    items = dao.getItems()
                    distributedItems = dao.getDistributedItems()
                }
            }

            if (onEditItemDialog) {
                EditItemDialog(
                    item = itemForDialog,
                    dao = dao,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState
                ) {
                    onEditItemDialog = !onEditItemDialog
                    items = dao.getItems()
                    distributedItems = dao.getDistributedItems()
                }
            }

            if (onAddDistributedItemDialog) {
                AddDistributedItemDialog(
                    dao = dao,
                    books = books,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    day = 1
                ) {
                    onAddDistributedItemDialog = false
                }
            }
        },
        bottomBar = {
            BottomNavigation(Modifier.height(60.dp)) {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painterResource(id = screen.icon),
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = screen == route,
                        onClick = { route = screen }
                    )
                }
            }
        },
    )
}




