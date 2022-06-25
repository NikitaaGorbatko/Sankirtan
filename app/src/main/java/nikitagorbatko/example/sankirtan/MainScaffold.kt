package nikitagorbatko.example.sankirtan

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import nikitagorbatko.example.sankirtan.room.*
import nikitagorbatko.example.sankirtan.views.StatisticScreen
import nikitagorbatko.example.sankirtan.views.selectBooks

val screens = listOf(BottomScreens.Books, BottomScreens.Briefcase, BottomScreens.Statistic)

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun MainScaffold(
    //model: BriefcaseViewModel,
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
    var briefcaseBooks by remember { mutableStateOf(bookDataSource.items) }
    var distributedBooks by remember { mutableStateOf(bookDataSource.distributedItems) }
    var distributedDayList by remember { mutableStateOf(listOf<DistributedItem>()) }
    var days by remember { mutableStateOf(bookDataSource.days) }
    var clickedDate by remember { mutableStateOf(0) }

    lateinit var bookForDialog: Book
    lateinit var itemForDialog: Item
    var totalCost = 0
    var totalAmount = 0

    briefcaseBooks.forEach {
        totalCost += it.cost * it.amount
        totalAmount += it.amount
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = route.title + if (route == BottomScreens.Briefcase) {
                            " (${totalAmount}шт $totalCost руб)"
                        } else ""
                    )
                },
                elevation = 12.dp,
//                actions = {
//                    IconButton(
//                        onClick = {
//                            when (route) {
//                                BottomScreens.Books -> {
//                                    onCreateBookDialog = true
//                                }
//                                BottomScreens.Briefcase -> {
//                                    onCreateItemDialog = true
//                                }
//                                BottomScreens.Statistic -> {
//                                    onAddDistributedItemDialog = true
//                                    dayForDistribution = 8
//                                }
//                            }
//                        },
//                        content = {
//                            Icon(
//                                imageVector = Icons.Filled.Add,
//                                contentDescription = null,
//                                tint = Color.White
//                            )
//                        },
//                    )
//                }
            )
        },
        content = {
            when (route) {
                BottomScreens.Books -> {
                    distributedDayList = listOf()
                    BooksScreen(books) {
                        bookForDialog = it
                        onEditBookDialog = !onEditBookDialog
                    }
                }
                BottomScreens.Briefcase -> {
                    distributedDayList = listOf()
                    BriefcaseScreen(
                        briefcaseBooks,
                        distributeItemLambda = {
                            itemForDialog = it
                            onEditItemDialog = !onEditItemDialog
                        },
                        deleteItemLambda = {
                            //dao.deleteItem(it)
                            itemForDialog = it
                            onDeleteItemDialog = !onDeleteItemDialog
                        },
                        //model
                    )
                }
                BottomScreens.Statistic -> StatisticScreen(
                    distributedItems = distributedBooks,
                    days = days,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    dao = dao,
                    insertDayLambda = {
                        days = dao.getDays()
                    },
                    liftStringLambda = {
                        intentLambda(it)
                    },
                    clickedDate = clickedDate,
                    onClickedDateChange = {
                        clickedDate = it
                    },
                    distributedDayList,
                    onDistributedDayListChange = {
                        distributedDayList = it
                    }
                )
            }

            if (onCreateBookDialog) {
                CreateBookDialog(dao, coroutineScope, snackbarHostState) {
                    onCreateBookDialog = false
                    books = dao.getBooks()
                }
            }

            if (onDeleteItemDialog) {
                DeleteBriefcaseItemDialog(dao, itemForDialog, coroutineScope, snackbarHostState) {
                    onDeleteItemDialog = false
                    //model.notificationChange()
                    briefcaseBooks = dao.getItems()
                }
            }

            if (onEditBookDialog) {
                EditBookDialog(dao, bookForDialog, coroutineScope, snackbarHostState) {
                    onEditBookDialog = false
                    books = dao.getBooks()
                }
            }

            if (onCreateItemDialog) {
                CreateBriefcaseItemDialog(dao, books, coroutineScope, snackbarHostState) {
                    onCreateItemDialog = false
                    briefcaseBooks = dao.getItems()
                    //model.notificationChange()
                    distributedBooks = dao.getDistributedItems()
                }
            }

            if (onEditItemDialog) {
                EditBriefcaseItemDialog(
                    item = itemForDialog,
                    dao = dao,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState
                ) {
                    //model.notificationChange()
                    onEditItemDialog = false
                    briefcaseBooks = dao.getItems()
                    distributedBooks = dao.getDistributedItems()
                }
            }

            if (onAddDistributedItemDialog) {
                AddDistributedItemDialog(
                    dao = dao,
                    books = books,
                    coroutineScope = coroutineScope,
                    snackbarHostState = snackbarHostState,
                    date = clickedDate
                ) {
                    onAddDistributedItemDialog = false
                    distributedBooks = dao.getDistributedItems()
                    distributedDayList = selectBooks(DateHolder(clickedDate).day, distributedBooks)
                }
            }
        },
        bottomBar = {
            BottomNavigation(Modifier.height(60.dp)) {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = screen.icon),
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(text = screen.title) },
                        selected = screen == route,
                        onClick = {
                            route = screen
                            if (screen != BottomScreens.Statistic) {
                                clickedDate = 0
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            if (route != BottomScreens.Statistic || clickedDate != 0) {
                FloatingActionButton(
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
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

        }
    )
}




