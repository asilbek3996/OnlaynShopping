package com.example.onlaynshop.model

import java.io.Serializable

data class AdaressModel(
    val address:String,
    val latitude:Double,
    val longitude:Double
): Serializable