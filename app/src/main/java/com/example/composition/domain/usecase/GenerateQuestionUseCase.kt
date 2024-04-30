package com.example.composition.domain.usecase

import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(
    val repository: GameRepository
){
    operator fun invoke(maxSumValue: Int) : Question {
        return repository.generateQuestion(maxSumValue, OPTIONS_NUMBER)
    }

    private companion object {
        const val OPTIONS_NUMBER: Int = 6
    }
}