package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding
import com.example.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw NullPointerException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding.buttonRetry.setOnClickListener { retryGame() }

    }

    private fun bindViews() {
        binding.apply {
            val requiredPercentage = gameResult.gameSettings.minPercentOfRightAnswers.toString()
            tvRequiredPercentage.text =
                requireActivity().application.getString(
                    R.string.required_percentage,
                    requiredPercentage
                )

            val scoreAnswers = gameResult.countOfRightAnswers.toString()
            tvScoreAnswers.text =
                requireActivity().application.getString(R.string.score_answers, scoreAnswers)

            val requiredAnswers = gameResult.gameSettings.minCountOfRightAnswers.toString()
            tvRequiredAnswers.text =
                requireActivity().application.getString(R.string.required_score, requiredAnswers)

           tvScorePercentage.text =
                requireActivity().application.getString(R.string.score_percentage, getScorePercentage().toString())

            emojiResult.setImageResource(getSmileResId())

        }
    }

    private fun getScorePercentage(): Int {
        return with(gameResult) {
            if (countOfRightAnswers == 0) {
                0
            } else {
                ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
            }
        }
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().getParcelable(GAME_RESULT, GameResult::class.java)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.apply {
            popBackStack(GameFragment.NAME, POP_BACK_STACK_INCLUSIVE)
        }
    }

    companion object {

        private const val GAME_RESULT = "game result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }
}