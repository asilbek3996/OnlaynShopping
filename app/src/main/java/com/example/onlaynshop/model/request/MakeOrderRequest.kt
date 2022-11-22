package com.example.onlaynshop.model.request

import com.example.onlaynshop.model.CartModel

data class MakeOrderRequest(
    val products: List<CartModel>,
    val order_type: String,
    val adress: String,
    val lat: Double,
    val lon: Double,
    val comment: String
)