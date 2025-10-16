package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.MovieCard
import com.example.myapplication.viewmodel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: MovieViewModel,
    onMovieClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val watchlist by viewModel.watchlist.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Watchlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (watchlist.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your watchlist is empty.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(watchlist) { movieEntity ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 🎬 Movie Card
                        Box(modifier = Modifier.weight(1f)) {
                            MovieCard(
                                title = movieEntity.title,
                                imageUrl = movieEntity.posterUrl,
                                rating = movieEntity.rating,
                                onClick = { onMovieClick(movieEntity.id) }
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // ❌ Remove Button
                        Button(
                            onClick = { viewModel.toggleWatchlist(movieEntity.id) },
                            modifier = Modifier
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
