package com.example.labproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.labproject.ui.theme.LabProjectTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint


data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
)


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels<MainViewModel>()
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "units.db"
        ).build()
    }

    private val dbViewModel by viewModels<ListViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ListViewModel(db.lstDao()) as T
                }
            }
        }
    )

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabProjectTheme {
                val state by dbViewModel.state.collectAsState()
                val navController = rememberNavController()
                val items = listOf<NavigationItem>(
                    NavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Filled.Home,
                        route = Screen.Home.route,
                    ),
                    NavigationItem(
                        title = "List",
                        selectedIcon = Icons.Filled.Menu,
                        unselectedIcon = Icons.Filled.Menu,
                        route = Screen.List.route,
                    ),
                    NavigationItem(
                        title = "Swipe",
                        selectedIcon = Icons.Filled.AccountBox,
                        unselectedIcon = Icons.Filled.AccountBox,
                        route = Screen.Swipe.route,
                    ),
                )
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                var selectedIdemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(
                                "Projekt Android\nJan Staręga",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(top = 20.dp)
                                    .align(
                                        Alignment.CenterHorizontally
                                    )
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            items.forEachIndexed { index, item ->
                                NavigationDrawerItem(
                                    label = { Text(item.title) },
                                    selected = index == selectedIdemIndex,
                                    onClick = {
                                        navController.navigate(item.route)
                                        selectedIdemIndex = index
                                        scope.launch { drawerState.close() }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedIdemIndex) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }
                    },
                    drawerState = drawerState
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                title = {
                                    Text(
                                        "Projekt Android",
                                        maxLines = 1
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = "description"
                                        )
                                    }
                                },
                            )
                        },
                    )
                    {
                        NavHost(navController = navController, startDestination = "home") {
                            composable(Screen.Home.route) { Home(navController, mainViewModel) }
                            composable(Screen.List.route) {
                                ItemList(
                                    navController,
                                    state = state,
                                    onEvent = dbViewModel::onEvent
                                )
                            }
                            composable(Screen.Swipe.route) {
                                Swipe(
                                    navController,
                                    mainViewModel
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}
