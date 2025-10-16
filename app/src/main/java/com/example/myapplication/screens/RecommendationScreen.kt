package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.components.MovieCard
import com.example.myapplication.viewmodel.MovieViewModel
import com.example.myapplication.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationScreen(
    viewModel: MovieViewModel = viewModel(),
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val watchlist by viewModel.watchlist.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var recommendedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    // 🔹 Generate recommendations based on watchlist
    LaunchedEffect(watchlist) {
        if (watchlist.isNotEmpty()) {
            val recommendations = mutableListOf<Movie>()
            for (movie in watchlist) {
                val similar = viewModel.getRecommendationsForMovie(movie.id)
                recommendations.addAll(similar)
            }
            recommendedMovies = recommendations.distinctBy { it.id }
        } else {
            recommendedMovies = emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommended Movies") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                recommendedMovies.isEmpty() -> {
                    Text(
                        text = "No recommendations yet.\nAdd some movies to your watchlist!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(recommendedMovies) { movie ->
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
}
