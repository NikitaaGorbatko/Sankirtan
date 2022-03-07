package nikitagorbatko.example.sankirtan


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.twotone.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp

val screens = listOf(BottomScreens.Books, BottomScreens.Briefcase, BottomScreens.Statistic)

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun MainScaffold(dao: BookDao) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var onCreateBookDialog by remember { mutableStateOf(false) }
    var onEditBookDialog by remember { mutableStateOf(false) }
    var onCreateItemDialog by remember { mutableStateOf(false) }
    var onEditItemDialog by remember { mutableStateOf(false) }
    var onDeleteItemDialog by remember { mutableStateOf(false) }

    var route by remember { mutableStateOf(screens[0]) }
    var books by remember { mutableStateOf(dao.getBooks()) }
    var items by remember { mutableStateOf(dao.getItems()) }
    var distributedItems by remember { mutableStateOf(dao.getDistributedItems()) }

    lateinit var bookForDialog: Book
    lateinit var itemForDialog: Item
    var totalCost = 0

    items.forEach { totalCost += it.cost * it.amount }

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = {
            TopAppBar(
                title = { Text(route.title + if(route == BottomScreens.Briefcase) {" ($totalCost руб)"} else "") },
                elevation = 12.dp,
                actions = {
                    when (route) {
                        BottomScreens.Books -> {
                            IconButton(
                                onClick = { onCreateBookDialog = true },
                                content = { Icon(Icons.TwoTone.Add, contentDescription = null) },
                            )
                        }
                        BottomScreens.Briefcase -> {
                            IconButton(
                                onClick = { onCreateItemDialog = true },
                                content = { Icon(Icons.Filled.Add, contentDescription = null) },
                            )
                        }
                        BottomScreens.Statistic -> {  }
                    }
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
                    addItemLambda = {
                        itemForDialog = it
                        onEditItemDialog = !onEditItemDialog
                    },
                    deleteItemLambda = {
                        //dao.deleteItem(it)
                        itemForDialog = it
                        onDeleteItemDialog = !onDeleteItemDialog
                    }
                )
                BottomScreens.Statistic -> StatisticScreen(distributedItems, coroutineScope)
            }

            if (onCreateBookDialog) {
                CreateBookDialog(dao, books, coroutineScope, snackbarHostState) {
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
                EditItemDialog(item = itemForDialog, dao = dao, coroutineScope = coroutineScope, snackbarHostState = snackbarHostState) {
                    onEditItemDialog = !onEditItemDialog
                    items = dao.getItems()
                    distributedItems = dao.getDistributedItems()
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
                        selected = screen == route,
                        onClick = { route = screen }
                    )
                }
            }
        },
    )
}

