package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {
    const val MIN_SUM_VALUE = 2
    const val MIN_VISIBLE_NUMBER = 1

    override fun generateQuestion(maxSumValue: Int, optionsCount: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_VISIBLE_NUMBER, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - optionsCount, MIN_VISIBLE_NUMBER)
        val to = min(maxSumValue, rightAnswer + optionsCount)
        while (options.size < optionsCount) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> {
                GameSettings(
                    maxSumValue = 10,
                    minCountOfRightAnswers = 3,
                    minPercentOfRightAnswers = 20,
                    gameTimeInSeconds = 4,
                )
            }

            Level.EASY -> {
                GameSettings(
                    maxSumValue = 10,
                    minCountOfRightAnswers = 5,
                    minPercentOfRightAnswers = 50,
                    gameTimeInSeconds = 60,
                )
            }

            Level.NORMAL -> {
                GameSettings(
                    maxSumValue = 20,
                    minCountOfRightAnswers = 10,
                    minPercentOfRightAnswers = 70,
                    gameTimeInSeconds = 40,
                )
            }

            Level.HARD -> {
                GameSettings(
                    maxSumValue = 100,
                    minCountOfRightAnswers = 20,
                    minPercentOfRightAnswers = 85,
                    gameTimeInSeconds = 40,
                )
            }
        }
    }
}