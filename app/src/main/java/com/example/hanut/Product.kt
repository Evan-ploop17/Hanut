package com.example.hanut

import java.io.Serializable

class Product(val image: Int, val name: String, val price: Double, val description: String) : Serializable {
}