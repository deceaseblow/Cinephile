package com.example.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
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
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleWatchlist: () -> Unit,
    onRateMovie: (Float) -> Unit
) {
    var rating by remember { mutableStateOf(movie.userRating ?: 0f) }
    val isFavorite = remember { mutableStateOf(movie.isFavorite) }
    val inWatchlist = remember { mutableStateOf(movie.inWatchlist) }

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

            // 🎞️ Poster
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

            // 📝 Info
            Text(
                text = movie.title ?: "Unknown Title",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Release: ${movie.releaseDate ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Director: ${movie.director ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Cast: ${movie.cast ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = movie.synopsis ?: "No synopsis available.",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(24.dp))

            // ⭐ Rating Section
            Text("Your Rating", style = MaterialTheme.typography.titleMedium)
            RatingBar(
                rating = rating,
                onRatingChanged = {
                    rating = it
                    onRateMovie(it)
                }
            )

            Spacer(Modifier.height(16.dp))

            // ❤️ Favorite & 📋 Watchlist
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        isFavorite.value = !isFavorite.value
                        onToggleFavorite()
                    }
                ) {
                    Icon(
                        imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (isFavorite.value) "Unfavorite" else "Favorite")
                }

                Button(
                    onClick = {
                        inWatchlist.value = !inWatchlist.value
                        onToggleWatchlist()
                    }
                ) {
                    Text(if (inWatchlist.value) "Remove from Watchlist" else "Add to Watchlist")
                }
            }
        }
    }
}

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxRating * 2) { // 0.5 increments
            val current = i / 2f
            val filled = current <= rating
            IconButton(onClick = { onRatingChanged(current) }) {
                Icon(
                    imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "$current stars",
                    tint = if (filled)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}
