package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.OrderEntity
import com.example.data.model.CartItem
import com.example.data.model.FoodCategory
import com.example.data.model.FoodItem
import com.example.data.repository.FoodRepository
import com.example.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

data class CheckoutFormState(
    val customerName: String = "",
    val phone: String = "+998 ",
    val orderType: String = "Yetkazib berish", // "Yetkazib berish", "Olib ketish", "Restoranda"
    val addressOrTable: String = "",
    val paymentMethod: String = "Naqd pul", // "Naqd pul", "Karta (Click / Payme)"
    val notes: String = "",
    val errorMessage: String? = null
)

class FoodOrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Filter & Search
    private val _selectedCategory = MutableStateFlow(FoodCategory.ALL)
    val selectedCategory: StateFlow<FoodCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered food list
    val filteredFoods: StateFlow<List<FoodItem>> = combine(
        _selectedCategory,
        _searchQuery
    ) { category, query ->
        val list = FoodRepository.foods.filter { item ->
            val matchesCategory = category == FoodCategory.ALL || item.category == category
            val matchesQuery = query.isBlank() ||
                    item.nameUz.contains(query, ignoreCase = true) ||
                    item.description.contains(query, ignoreCase = true) ||
                    item.ingredients.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FoodRepository.foods)

    // Cart State: Map of FoodId -> CartItem
    private val _cartItems = MutableStateFlow<Map<Int, CartItem>>(emptyMap())
    val cartItems: StateFlow<Map<Int, CartItem>> = _cartItems.asStateFlow()

    // Checkout form state
    private val _checkoutForm = MutableStateFlow(CheckoutFormState())
    val checkoutForm: StateFlow<CheckoutFormState> = _checkoutForm.asStateFlow()

    // Active Receipt (Shown after ordering or when clicking from history)
    private val _activeReceipt = MutableStateFlow<OrderEntity?>(null)
    val activeReceipt: StateFlow<OrderEntity?> = _activeReceipt.asStateFlow()

    // Detail item modal
    private val _selectedDetailFood = MutableStateFlow<FoodItem?>(null)
    val selectedDetailFood: StateFlow<FoodItem?> = _selectedDetailFood.asStateFlow()

    // Orders history from Room DB
    val orderHistory: StateFlow<List<OrderEntity>> = orderRepository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calculations
    val cartTotalCount: StateFlow<Int> = _cartItems.combine(_cartItems) { map, _ ->
        map.values.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val cartSubtotal: StateFlow<Long> = _cartItems.combine(_cartItems) { map, _ ->
        map.values.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val serviceFee: StateFlow<Long> = cartSubtotal.combine(cartSubtotal) { sub, _ ->
        (sub * 0.10).toLong() // 10% xizmat haqi
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val deliveryFee: StateFlow<Long> = combine(cartSubtotal, _checkoutForm) { sub, form ->
        if (sub > 0 && form.orderType == "Yetkazib berish") 15000L else 0L
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val grandTotal: StateFlow<Long> = combine(cartSubtotal, serviceFee, deliveryFee) { sub, serv, deliv ->
        if (sub > 0) sub + serv + deliv else 0L
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    // Actions
    fun selectCategory(category: FoodCategory) {
        _selectedCategory.value = category
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun openFoodDetail(food: FoodItem) {
        _selectedDetailFood.value = food
    }

    fun closeFoodDetail() {
        _selectedDetailFood.value = null
    }

    fun addToCart(food: FoodItem) {
        val current = _cartItems.value.toMutableMap()
        val existing = current[food.id]
        if (existing != null) {
            current[food.id] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current[food.id] = CartItem(food = food, quantity = 1)
        }
        _cartItems.value = current
    }

    fun removeFromCart(foodId: Int) {
        val current = _cartItems.value.toMutableMap()
        val existing = current[foodId] ?: return
        if (existing.quantity > 1) {
            current[foodId] = existing.copy(quantity = existing.quantity - 1)
        } else {
            current.remove(foodId)
        }
        _cartItems.value = current
    }

    fun deleteItemFromCart(foodId: Int) {
        val current = _cartItems.value.toMutableMap()
        current.remove(foodId)
        _cartItems.value = current
    }

    fun clearCart() {
        _cartItems.value = emptyMap()
    }

    // Checkout form updates
    fun updateCustomerName(name: String) {
        _checkoutForm.value = _checkoutForm.value.copy(customerName = name, errorMessage = null)
    }

    fun updatePhone(phone: String) {
        _checkoutForm.value = _checkoutForm.value.copy(phone = phone, errorMessage = null)
    }

    fun updateOrderType(type: String) {
        _checkoutForm.value = _checkoutForm.value.copy(orderType = type)
    }

    fun updateAddressOrTable(value: String) {
        _checkoutForm.value = _checkoutForm.value.copy(addressOrTable = value, errorMessage = null)
    }

    fun updatePaymentMethod(method: String) {
        _checkoutForm.value = _checkoutForm.value.copy(paymentMethod = method)
    }

    fun updateNotes(notes: String) {
        _checkoutForm.value = _checkoutForm.value.copy(notes = notes)
    }

    // Submit Order ("Zaqas qilish")
    fun submitOrder(): Boolean {
        val form = _checkoutForm.value
        val items = _cartItems.value.values.toList()

        if (items.isEmpty()) {
            _checkoutForm.value = form.copy(errorMessage = "Savatingiz bo'sh. Iltimos, taom tanlang!")
            return false
        }

        if (form.customerName.trim().length < 2) {
            _checkoutForm.value = form.copy(errorMessage = "Iltimos, ismingizni to'liq kiriting!")
            return false
        }

        if (form.phone.trim().length < 9) {
            _checkoutForm.value = form.copy(errorMessage = "Iltimos, telefon raqamingizni to'g'ri kiriting!")
            return false
        }

        if (form.addressOrTable.trim().isEmpty()) {
            val reqMsg = if (form.orderType == "Restoranda") "Stol raqamini kiriting!" else "Yetkazish manzilini kiriting!"
            _checkoutForm.value = form.copy(errorMessage = reqMsg)
            return false
        }

        val sub = items.sumOf { it.totalPrice }
        val serv = (sub * 0.10).toLong()
        val deliv = if (form.orderType == "Yetkazib berish") 15000L else 0L
        val total = sub + serv + deliv

        // Format items summary for receipt
        val summaryBuilder = StringBuilder()
        items.forEachIndexed { index, item ->
            summaryBuilder.append("${index + 1}. ${item.food.nameUz} x${item.quantity} = ${item.totalPrice} so'm\n")
        }

        val randomNum = Random.nextInt(10000, 99999)
        val orderNum = "#TM-$randomNum"

        val orderEntity = OrderEntity(
            orderNumber = orderNum,
            timestamp = System.currentTimeMillis(),
            customerName = form.customerName.trim(),
            customerPhone = form.phone.trim(),
            orderType = form.orderType,
            deliveryAddressOrTable = form.addressOrTable.trim(),
            paymentMethod = form.paymentMethod,
            orderNotes = form.notes.trim(),
            itemsSummary = summaryBuilder.toString().trim(),
            subtotal = sub,
            serviceFee = serv,
            deliveryFee = deliv,
            grandTotal = total,
            status = "Qabul qilindi"
        )

        viewModelScope.launch {
            val insertedId = orderRepository.createOrder(orderEntity)
            val savedOrder = orderEntity.copy(id = insertedId)
            _activeReceipt.value = savedOrder
            clearCart()
        }

        return true
    }

    fun showReceipt(order: OrderEntity) {
        _activeReceipt.value = order
    }

    fun dismissReceipt() {
        _activeReceipt.value = null
    }

    fun deleteOrderHistory(id: Long) {
        viewModelScope.launch {
            orderRepository.deleteOrder(id)
        }
    }
}

class FoodOrderViewModelFactory(
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodOrderViewModel::class.java)) {
            return FoodOrderViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
