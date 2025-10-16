package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.MovieCard
import com.example.myapplication.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    onSearch: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onWatchlistClick: () -> Unit,
    onRecommendationsClick: () -> Unit
) {
    val movies by viewModel.homeMovies.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie App") },
                actions = {
                    IconButton(onClick = onWatchlistClick) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Watchlist")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 🔍 Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search movies...") },
                trailingIcon = {
                    IconButton(onClick = { if (searchQuery.isNotBlank()) onSearch(searchQuery) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🌟 See Recommendations Button
            Button(
                onClick = onRecommendationsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("See Recommendations")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🎬 Movie List
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
