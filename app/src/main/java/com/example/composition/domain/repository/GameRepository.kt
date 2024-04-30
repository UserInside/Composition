package com.example.composition.domain.repository

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestion(maxSumValue: Int, optionsCount: Int): Question

    fun getGameSettings(): GameSettings


}