package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    movie: Movie,
    inWatchlist: Boolean,
    onBack: () -> Unit,
    onAddToWatchlist: (Movie) -> Unit,
    onRemoveFromWatchlist: (Int) -> Unit,
    onToggleFavorite: () -> Unit,
    onRateMovie: (Float) -> Unit
) {
    var isInWatchlist by remember { mutableStateOf(inWatchlist) }

    LaunchedEffect(inWatchlist) {
        isInWatchlist = inWatchlist
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie.title ?: "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🎬 Poster
            Image(
                painter = rememberAsyncImagePainter(model = movie.posterUrl),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = movie.title ?: "Unknown Title",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text("Release: ${movie.releaseDate ?: "N/A"}")
            Text("Director: ${movie.director ?: "N/A"}")
            Text("Cast: ${movie.cast ?: "N/A"}")

            Spacer(Modifier.height(8.dp))
            Text(
                movie.synopsis ?: "No synopsis available.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isInWatchlist) {
                        onRemoveFromWatchlist(movie.id)
                    } else {
                        onAddToWatchlist(movie)
                    }
                    // Update local state immediately for smooth UX
                    isInWatchlist = !isInWatchlist
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isInWatchlist)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (isInWatchlist) "Remove from Watchlist" else "Add to Watchlist",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
