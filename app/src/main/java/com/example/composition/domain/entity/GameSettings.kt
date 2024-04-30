package com.example.composition.domain.entity

class GameSettings(
    val timeInSeconds: Int,
    val actualPointsCount: Int,
    val minPointsRequired: Int,
    val level: Level,
    ){
}