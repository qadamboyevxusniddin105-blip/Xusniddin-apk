package com.example.data.repository

import com.example.data.local.OrderDao
import com.example.data.local.OrderEntity
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    suspend fun getOrderById(id: Long): OrderEntity? = orderDao.getOrderById(id)

    suspend fun createOrder(order: OrderEntity): Long = orderDao.insertOrder(order)

    suspend fun deleteOrder(id: Long) = orderDao.deleteOrder(id)
}
