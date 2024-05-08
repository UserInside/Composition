package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.composition.R
import com.example.composition.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private val args by navArgs<GameFinishedFragmentArgs>()

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw NullPointerException("FragmentGameFinishedBinding == null")

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
        binding.buttonRetry.setOnClickListener { retryGame() }
    }

    private fun bindViews() {
        binding.apply {
            val requiredPercentage =
                args.gameResult.gameSettings.minPercentOfRightAnswers.toString()
            tvRequiredPercentage.text =
                requireActivity().application.getString(
                    R.string.required_percentage,
                    requiredPercentage
                )

            val scoreAnswers = args.gameResult.countOfRightAnswers.toString()
            tvScoreAnswers.text =
                requireActivity().application.getString(R.string.score_answers, scoreAnswers)

            val requiredAnswers = args.gameResult.gameSettings.minCountOfRightAnswers.toString()
            tvRequiredAnswers.text =
                requireActivity().application.getString(R.string.required_score, requiredAnswers)

            tvScorePercentage.text =
                requireActivity().application.getString(
                    R.string.score_percentage,
                    getScorePercentage().toString()
                )

            emojiResult.setImageResource(getSmileResId())

        }
    }

    private fun getScorePercentage(): Int {
        return with(args.gameResult) {
            if (countOfRightAnswers == 0) {
                0
            } else {
                ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
            }
        }
    }

    private fun getSmileResId(): Int {
        return if (args.gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun retryGame() {
        findNavController().popBackStack()
    }

}