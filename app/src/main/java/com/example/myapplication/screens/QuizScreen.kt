package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.utils.QuizQuestion


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    questions: List<QuizQuestion>,
    onSubmit: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Quiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (questions.isEmpty()) {
                Text("No questions available. Add more movies to your watchlist.")
                return@Column
            }

            questions.forEachIndexed { index, question ->
                Text(
                    text = "Q${index + 1}. ${question.question}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 🖼️ Display poster image if available
                question.imageUrl?.let { url ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AsyncImage(
                            model = url,
                            contentDescription = "Movie poster",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                question.options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {
                        RadioButton(
                            selected = selectedAnswers[index] == option,
                            onClick = { selectedAnswers[index] = option }
                        )
                        Text(option)
                    }
                }

                Divider(Modifier.padding(vertical = 8.dp))
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val correctCount = questions.countIndexed { i, q ->
                        selectedAnswers[i] == q.correctAnswer
                    }
                    onSubmit(correctCount, questions.size)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Check Answers")
            }
        }
    }
}

private inline fun <T> Iterable<T>.countIndexed(predicate: (index: Int, T) -> Boolean): Int {
    var count = 0
    var i = 0
    for (item in this) {
        if (predicate(i++, item)) count++
    }
    return count
}