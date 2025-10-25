package com.example.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.components.MovieCard
import com.example.myapplication.viewmodel.MovieViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.List

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    onSearch: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    onWatchlistClick: () -> Unit,
    onRecommendationsClick: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val movies by viewModel.homeMovies.collectAsState()
    val watchlist by viewModel.watchlist.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val buttonFont = FontFamily(Font(R.font.valiny))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Cinephile",
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.cinemafont)),
                            fontSize = 40.sp
                        )
                    )
                },
                actions = {
                    IconButton(onClick = onWatchlistClick) {
                        Icon(Icons.Filled.List, contentDescription = "Watchlist")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search movies...") },
                trailingIcon = {
                    IconButton(onClick = { if (searchQuery.isNotBlank()) onSearch(searchQuery) }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // See Recommendations Button
            Button(
                onClick = onRecommendationsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    "See Recommendations",
                    fontFamily = buttonFont,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            //  Take a Quiz Button
            Button(
                onClick = {
                    if (watchlist.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Add a movie into your watchlist to take a quiz!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        navController.navigate("quiz")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    "Take a Quiz Now!",
                    fontFamily = buttonFont,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  Movie List
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
