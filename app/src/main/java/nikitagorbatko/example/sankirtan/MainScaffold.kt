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
import kotlinx.coroutines.launch
import nikitagorbatko.example.sankirtan.room.*
import nikitagorbatko.example.sankirtan.views.StatisticScreen
import nikitagorbatko.example.sankirtan.views.selectBooks

val screens = listOf(BottomScreens.Books, BottomScreens.Briefcase, BottomScreens.Statistic)

enum class Dialogs {
    NONE,
    CREATE_BOOK_DIALOG,
    EDIT_BOOK_DIALOG,
    CREATE_ITEM_DIALOG,
    EDIT_ITEM_DIALOG,
    DELETE_ITEM_DIALOG,
    ADD_DISTRIBUTED_ITEM_DIALOG,
    DELETE_DISTRIBUTED_ITEM_DIALOG,
    INFORM_THAT_DATE_UNDEFINED
}

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
    var whichDialog by remember { mutableStateOf(Dialogs.NONE) }

    var route by remember { mutableStateOf(screens[0]) }
    var books by remember { mutableStateOf(bookDataSource.books) }
    var briefcaseBooks by remember { mutableStateOf(bookDataSource.items) }
    var distributedBooks by remember { mutableStateOf(bookDataSource.distributedItems) }
    var distributedDayList by remember { mutableStateOf(listOf<DistributedItem>()) }
    var days by remember { mutableStateOf(bookDataSource.days) }
    var clickedDate by remember { mutableStateOf(0) }
    var donation_param by remember { mutableStateOf("0") }

    lateinit var bookForDialog: Book
    lateinit var itemForDialog: Item
    lateinit var distributedItem: DistributedItem
    var lastDay: Day? = null
    var totalCost = 0
    var totalAmount = 0

    briefcaseBooks.forEach {
        totalCost += it.cost * it.amount
        totalAmount += it.amount
    }

    fun showSnackbar(message: String, actionLabel: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message, actionLabel, SnackbarDuration.Short)
        }
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = route.title + if (route == BottomScreens.Briefcase) {
                            " (${totalAmount}шт ${totalCost}руб)"
                        } else ""
                    )
                },
                elevation = 12.dp,
                actions = {
//                    IconButton(
//                        onClick = { onQuestionDialog = true },
//                        content = {
//                            Icon(
//                                imageVector = Icons.Filled.QuestionAnswer,
//                                contentDescription = null,
//                                tint = Color.White
//                            )
//                        },
//                    )
                }
            )
        },
        content = {
            when (route) {
                BottomScreens.Books -> {
                    distributedDayList = listOf()
                    BooksScreen(books) {
                        bookForDialog = it
                        whichDialog = Dialogs.EDIT_BOOK_DIALOG
                    }
                }
                BottomScreens.Briefcase -> {
                    distributedDayList = listOf()
                    BriefcaseScreen(
                        briefcaseBooks,
                        distributeItemLambda = {
                            itemForDialog = it
                            whichDialog = Dialogs.EDIT_ITEM_DIALOG
                        },
                        deleteItemLambda = {
                            //dao.deleteItem(it)
                            itemForDialog = it
                            whichDialog = Dialogs.DELETE_ITEM_DIALOG
                        },
                        //model
                    )
                }
                BottomScreens.Statistic -> StatisticScreen(
                    distributedItems = distributedBooks,
                    days = days,
                    showSnackbar = { message, actionLabel -> showSnackbar(message, actionLabel) },
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
                    deleteDistributedItem = { item, day ->
                        distributedItem = item
                        lastDay = day
                        whichDialog = Dialogs.DELETE_DISTRIBUTED_ITEM_DIALOG
                    },
                    distributedDayList = distributedDayList,
                    onDistributedDayListChange = {
                        distributedDayList = it
                    },
                    donation_param = donation_param,
                )
            }



            when (whichDialog) {
                Dialogs.NONE -> {}
                Dialogs.CREATE_BOOK_DIALOG -> {
                    CreateBookDialog(
                        dao,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = {
                            whichDialog = Dialogs.NONE
                            books = dao.getBooks()
                        }
                    )
                }
                Dialogs.EDIT_BOOK_DIALOG -> {
                    EditBookDialog(
                        dao,
                        bookForDialog,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = {
                            whichDialog = Dialogs.NONE
                            books = dao.getBooks()
                        }
                    )
                }
                Dialogs.CREATE_ITEM_DIALOG -> {
                    CreateBriefcaseItemDialog(
                        dao,
                        books,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = {
                            whichDialog = Dialogs.NONE
                            briefcaseBooks = dao.getBriefcaseItems()
                            //model.notificationChange()
                            distributedBooks = dao.getDistributedItems()
                        }
                    )
                }
                Dialogs.EDIT_ITEM_DIALOG -> {
                    EditBriefcaseItemDialog(
                        item = itemForDialog,
                        dao = dao,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = {
                            //model.notificationChange()
                            whichDialog = Dialogs.NONE
                            briefcaseBooks = dao.getBriefcaseItems()
                            distributedBooks = dao.getDistributedItems()
                        }
                    )
                }
                Dialogs.DELETE_ITEM_DIALOG -> {
                    DeleteBriefcaseItemDialog(
                        dao,
                        itemForDialog,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = {
                            whichDialog = Dialogs.NONE
                            //model.notificationChange()
                            briefcaseBooks = dao.getBriefcaseItems()
                        }
                    )
                }
                Dialogs.ADD_DISTRIBUTED_ITEM_DIALOG -> {
                    AddDistributedItemDialog(
                        dao = dao,
                        books = books,
                        date = clickedDate,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = {
                            whichDialog = Dialogs.NONE
                            distributedBooks = dao.getDistributedItems()
                            distributedDayList =
                                selectBooks(DateHolder(clickedDate).day, distributedBooks)
                        }
                    )
                }
                Dialogs.DELETE_DISTRIBUTED_ITEM_DIALOG -> {
                    DeleteDistributedItemDialog(
                        dao = dao,
                        item = distributedItem,
                        showSnackbar = { message, actionLabel ->
                            showSnackbar(
                                message,
                                actionLabel
                            )
                        },
                        close = { deleted ->
                            whichDialog = Dialogs.NONE
                            if (deleted) {
                                distributedBooks = dao.getDistributedItems()
                                distributedDayList =
                                    selectBooks(DateHolder(clickedDate).day, distributedBooks)
                                lastDay?.let { it1 ->
                                    dao.deleteDay(it1)
                                    days = dao.getDays()
                                    lastDay = null
                                    donation_param = "0"
                                }
                            }
                        }
                    )
                }
                Dialogs.INFORM_THAT_DATE_UNDEFINED -> {
                    InformThatDateUndefined {
                        whichDialog = Dialogs.NONE
                    }
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
            FloatingActionButton(
                onClick = {
                    when (route) {
                        BottomScreens.Books -> {
                            whichDialog = Dialogs.CREATE_BOOK_DIALOG
                        }
                        BottomScreens.Briefcase -> {
                            whichDialog = Dialogs.CREATE_ITEM_DIALOG
                        }
                        BottomScreens.Statistic -> {
                            if (clickedDate == 0) {
                                whichDialog = Dialogs.INFORM_THAT_DATE_UNDEFINED
                            } else {
                                whichDialog = Dialogs.ADD_DISTRIBUTED_ITEM_DIALOG
                            }
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
    )
}




