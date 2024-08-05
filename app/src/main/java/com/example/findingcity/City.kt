package com.example.findingcity

data class City(
    val country: String,
    val name: String,
    val _id: Long,
    val coord: Coord
)

data class Coord(
    val lon: Double,
    val lat: Double
)
