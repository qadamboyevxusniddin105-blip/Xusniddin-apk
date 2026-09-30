package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.local.AppDatabase
import com.example.data.repository.OrderRepository
import com.example.ui.MainApp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FoodOrderViewModel
import com.example.ui.viewmodel.FoodOrderViewModelFactory

class MainActivity : ComponentActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val orderRepository by lazy { OrderRepository(database.orderDao()) }
    private val viewModel: FoodOrderViewModel by viewModels {
        FoodOrderViewModelFactory(orderRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}
