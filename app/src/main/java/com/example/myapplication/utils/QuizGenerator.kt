package com.example.myapplication.utils

import com.example.myapplication.local.MovieEntity

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val imageUrl: String? = null
)

object QuizGenerator {

    fun generateQuizQuestions(watchlist: List<MovieEntity>): List<QuizQuestion> {
        if (watchlist.size < 3) return emptyList()

        val questions = mutableListOf<QuizQuestion>()

        fun <T> randomOptions(correct: T, pool: List<T>, count: Int = 3): List<T> {
            val others = pool.filter { it != correct }.shuffled().take(count)
            return (others + correct).shuffled()
        }

        val questionTypes = listOf("director", "poster", "rating", "overview", "release")

        var attempts = 0
        val maxAttempts = 50 // Prevent infinite loops

        while (questions.size < 5 && attempts < maxAttempts) {
            attempts++

            val type = questionTypes.random()
            val movie = watchlist.random()

            when (type) {

                // 🎬 Director Question
                "director" -> {
                    val correct = movie.director
                    if (correct != null && correct.isNotBlank()) {
                        val pool = watchlist.mapNotNull { it.director }.filter { it.isNotBlank() }
                        if (pool.size > 1) {
                            questions += QuizQuestion(
                                question = "Who directed '${movie.title}'?",
                                options = randomOptions(correct, pool),
                                correctAnswer = correct
                            )
                        }
                    }
                }

                // 🖼️ Poster Question
                "poster" -> {
                    val poster = movie.posterUrl
                    if (poster != null && poster.isNotBlank()) {
                        val correct = movie.title
                        val pool = watchlist.map { it.title }
                        questions += QuizQuestion(
                            question = "Which movie does this poster belong to?",
                            options = randomOptions(correct, pool),
                            correctAnswer = correct,
                            imageUrl = poster
                        )
                    }
                }

                // ⭐ Rating Question
                "rating" -> {
                    val pair = watchlist.shuffled().take(2)
                    if (pair.size == 2) {
                        val (m1, m2) = pair
                        val correct = if (m1.rating >= m2.rating) m1.title else m2.title
                        questions += QuizQuestion(
                            question = "Which movie has a higher rating?",
                            options = listOf(m1.title, m2.title).shuffled(),
                            correctAnswer = correct
                        )
                    }
                }

                // 📖 Overview Question
                "overview" -> {
                    val overview = movie.overview
                    if (overview != null && overview.isNotBlank() && overview.length > 20) {
                        val correct = movie.title
                        val titles = watchlist.map { it.title }
                        questions += QuizQuestion(
                            question = "Which movie fits this plot?\n\n\"${overview.take(120)}...\"",
                            options = randomOptions(correct, titles),
                            correctAnswer = correct
                        )
                    }
                }

                // 📅 Release Date Question
                "release" -> {
                    val correct = movie.releaseDate
                    if (correct != null && correct.isNotBlank()) {
                        val pool = watchlist.mapNotNull { it.releaseDate }.filter { it.isNotBlank() }
                        if (pool.size > 1) {
                            questions += QuizQuestion(
                                question = "When was '${movie.title}' released?",
                                options = randomOptions(correct, pool),
                                correctAnswer = correct
                            )
                        }
                    }
                }
            }
        }

        return questions.take(5)
    }
}