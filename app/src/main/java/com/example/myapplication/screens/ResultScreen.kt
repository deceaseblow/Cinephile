package com.example.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.components.MovieCard
import com.example.myapplication.viewmodel.MovieViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    query: String,
    viewModel: MovieViewModel,
    onSearch: (String) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf(query) }
    val movies by viewModel.movies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(query) {
        viewModel.fetchMovies(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF212121))
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Movies", color = Color.Gray) },
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF1DB954),
                unfocusedBorderColor = Color.DarkGray,
                cursorColor = Color(0xFF1DB954)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { if (searchQuery.isNotBlank()) onSearch(searchQuery) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954))
        ) {
            Text("Search", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Results for \"$query\"",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            LoadingScreen()
        } else {
            if (movies.isEmpty()) {
                Text(
                    "No results found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(movies) { movie ->
                        MovieCard(
                            title = movie.title,
                            imageUrl = movie.posterUrl,
                            rating = movie.rating,
                            onClick = { onMovieClick(movie.id) }
                        )
                    }
                }
            }
        }
    }
}
