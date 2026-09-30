package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.ReceiptDialog
import com.example.ui.screens.CartScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.viewmodel.FoodOrderViewModel

enum class AppTab(val title: String) {
    MENU("Menyu"),
    CART("Savat"),
    HISTORY("Cheklar")
}

@Composable
fun MainApp(
    viewModel: FoodOrderViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(AppTab.MENU) }
    val cartCount by viewModel.cartTotalCount.collectAsState()
    val activeReceipt by viewModel.activeReceipt.collectAsState()

    // Handle back button when not on MENU tab
    BackHandler(enabled = currentTab != AppTab.MENU) {
        currentTab = AppTab.MENU
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_navigation_bar")
            ) {
                // Menu tab
                NavigationBarItem(
                    selected = currentTab == AppTab.MENU,
                    onClick = { currentTab = AppTab.MENU },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.MENU) Icons.Filled.RestaurantMenu else Icons.Outlined.RestaurantMenu,
                            contentDescription = "Menyu"
                        )
                    },
                    label = {
                        Text(
                            text = "Menyu",
                            fontWeight = if (currentTab == AppTab.MENU) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("tab_menu")
                )

                // Cart tab with Badge
                NavigationBarItem(
                    selected = currentTab == AppTab.CART,
                    onClick = { currentTab = AppTab.CART },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ) {
                                        Text("$cartCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (currentTab == AppTab.CART) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                                contentDescription = "Savat"
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "Savat",
                            fontWeight = if (currentTab == AppTab.CART) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("tab_cart")
                )

                // History / Receipts tab
                NavigationBarItem(
                    selected = currentTab == AppTab.HISTORY,
                    onClick = { currentTab = AppTab.HISTORY },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == AppTab.HISTORY) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                            contentDescription = "Cheklar"
                        )
                    },
                    label = {
                        Text(
                            text = "Cheklar",
                            fontWeight = if (currentTab == AppTab.HISTORY) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("tab_history")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.MENU -> MenuScreen(
                    viewModel = viewModel,
                    onNavigateToCart = { currentTab = AppTab.CART }
                )
                AppTab.CART -> CartScreen(
                    viewModel = viewModel,
                    onNavigateToMenu = { currentTab = AppTab.MENU }
                )
                AppTab.HISTORY -> HistoryScreen(
                    viewModel = viewModel,
                    onNavigateToMenu = { currentTab = AppTab.MENU }
                )
            }
        }
    }

    // Active Receipt Dialog (Appears immediately after submitting order or from history)
    activeReceipt?.let { order ->
        ReceiptDialog(
            order = order,
            onDismiss = { viewModel.dismissReceipt() }
        )
    }
}
