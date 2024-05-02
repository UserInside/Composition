package com.example.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.composition.R
import com.example.composition.databinding.FragmentGameBinding
import com.example.composition.domain.entity.GameResult
import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import kotlin.concurrent.timer

class GameFragment : Fragment() {

    private lateinit var level: Level
    private lateinit var viewModel: GameFragmentViewModel
    private var rightAnswer: Int = 0

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw NullPointerException("FragmentGameBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameFragmentViewModel::class.java]

        binding.apply{
            viewModel.question.observe(viewLifecycleOwner){
                tvSum.text = it.sum.toString()
                tvLeftNumber.text = it.visibleNumber.toString()
                tvOption1.text = it.options[0].toString()
                tvOption2.text = it.options[1].toString()
                tvOption3.text = it.options[2].toString()
                tvOption5.text = it.options[3].toString()
                tvOption5.text = it.options[4].toString()
                tvOption6.text = it.options[5].toString()

                rightAnswer = it.rightAnswer
            }

            viewModel.gameSettings.observe(viewLifecycleOwner){gameSettings ->
                //todo tvTimer = it.gameTimeInSeconds
                viewModel.rightAnswersCount.observe(viewLifecycleOwner){
                    tvAnswersProgress.text = getString(R.string.progress_answers, it.toString(), gameSettings.minCountOfRightAnswers.toString())

                }
                //todo progressBar
//                progressBar.min = 0
//                progressBar.max = gameSettings.minCountOfRightAnswers
            }

            tvOption1.setOnClickListener {viewModel.checkAnswer(0)}
            tvOption2.setOnClickListener {viewModel.checkAnswer(1)}
            tvOption3.setOnClickListener {viewModel.checkAnswer(2)}
            tvOption4.setOnClickListener {viewModel.checkAnswer(3)}
            tvOption5.setOnClickListener {viewModel.checkAnswer(4)}
            tvOption6.setOnClickListener {viewModel.checkAnswer(5)}
        }


    }

    private fun parseArgs() {
        requireArguments().getParcelable(KEY_LEVEL, Level::class.java)?.let{
            level = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    companion object {

        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}