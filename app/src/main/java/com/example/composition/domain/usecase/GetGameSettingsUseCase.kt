package com.example.composition.domain.usecase

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    val repository: GameRepository
) {

    operator fun invoke(): GameSettings {
        return repository.getGameSettings()
    }
}