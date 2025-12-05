package com.example.higherlower

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random
import android.util.Log

class GameActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var cardImageView: ImageView
    private lateinit var higherButton: Button
    private lateinit var lowerButton: Button
    private lateinit var backButton: Button

    private var currentScore: Int = 0
    private var currentCardValue: Int = 0
    private val MIN_CARD_VALUE = 1
    private val MAX_CARD_VALUE = 13

    private lateinit var cardDrawableIds: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        scoreTextView = findViewById(R.id.textViewScore)
        cardImageView = findViewById(R.id.imageViewCard)
        higherButton = findViewById(R.id.buttonHigher)
        lowerButton = findViewById(R.id.buttonLower)
        backButton = findViewById(R.id.buttonBack)

        cardDrawableIds = resources.obtainTypedArray(R.array.card_drawables).use { array ->
            IntArray(array.length()) { array.getResourceId(it, 0) }
        }

        startGame()

        backButton.setOnClickListener {
            finish()
        }

        higherButton.setOnClickListener {
            checkGuess(Guess.HIGHER)
        }

        lowerButton.setOnClickListener {
            checkGuess(Guess.LOWER)
        }
    }

    enum class Guess {
        HIGHER,
        LOWER
    }

    private fun startGame() {
        currentScore = 0
        updateScoreDisplay()

        currentCardValue = Random.nextInt(MIN_CARD_VALUE, MAX_CARD_VALUE + 1)
        updateCardImage(currentCardValue)
    }

    private fun checkGuess(guess: Guess) {
        val previousCardValue = currentCardValue

        val nextCardValue = Random.nextInt(MIN_CARD_VALUE, MAX_CARD_VALUE + 1)

        Log.d("HigherLowerApp", "Prev: $previousCardValue, Next: $nextCardValue, Guess: $guess")

        updateCardImage(nextCardValue)

        val isCorrect: Boolean = when (guess) {
            Guess.HIGHER -> nextCardValue > previousCardValue
            Guess.LOWER -> nextCardValue < previousCardValue
        }

        Log.d("HigherLowerApp", "Is Correct: $isCorrect")

        val isSame = nextCardValue == previousCardValue

        if (isSame) {
            Toast.makeText(this, "Equal! ${previousCardValue} is the same as ${nextCardValue}. Continue!", Toast.LENGTH_SHORT).show()
        } else if (isCorrect) {
            currentScore++
            Toast.makeText(this, "Correct! New score: $currentScore", Toast.LENGTH_SHORT).show()
        } else {
            val message = "Wrong! End of game. Your score: $currentScore"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            startGame()
            return //
        }

        currentCardValue = nextCardValue
        updateScoreDisplay()
    }

    private fun updateScoreDisplay() {
        scoreTextView.text = "Score: $currentScore"
    }

    private fun updateCardImage(value: Int) {
        val arrayIndex = value - 1

        if (arrayIndex >= 0 && arrayIndex < cardDrawableIds.size) {
            val resourceId = cardDrawableIds[arrayIndex]
            cardImageView.setImageResource(resourceId)
        } else {
            Toast.makeText(this, "Invalid ($value)", Toast.LENGTH_LONG).show()
        }
        }
        }