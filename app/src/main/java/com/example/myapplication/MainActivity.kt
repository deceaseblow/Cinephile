package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.*
import com.example.myapplication.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "loading") {

                // 🌀 Loading screen (initial fetch)
                composable("loading") {
                    val isLoading = viewModel.isLoading.collectAsState().value
                    LoadingScreen()

                    LaunchedEffect(Unit) {
                        viewModel.fetchMovies("popular")
                    }

                    if (!isLoading) {
                        LaunchedEffect(Unit) {
                            navController.navigate("home") {
                                popUpTo("loading") { inclusive = true }
                            }
                        }
                    }
                }

                // 🏠 Home screen
                composable("home") {
                    HomeScreen(
                        viewModel = viewModel,
                        onSearch = { query ->
                            navController.navigate("results/$query")
                        },
                        onMovieClick = { movieId ->
                            navController.navigate("details/$movieId")
                        }
                    )
                }

                // 🔍 Search results screen
                composable("results/{query}") { backStackEntry ->
                    val query = backStackEntry.arguments?.getString("query") ?: ""
                    ResultScreen(
                        query = query,
                        viewModel = viewModel,
                        onSearch = { newQuery ->
                            navController.navigate("results/$newQuery")
                        },
                        onMovieClick = { movieId ->
                            navController.navigate("details/$movieId")
                        }
                    )
                }

                // 🎬 Movie details screen
                composable("details/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                    val movie = viewModel.selectedMovie.collectAsState().value

                    // Fetch movie details when navigating here
                    LaunchedEffect(movieId) {
                        if (movieId != null) {
                            viewModel.fetchMovieDetails(movieId)
                        }
                    }

                    movie?.let {
                        DetailsScreen(
                            movie = it,
                            onBack = { navController.popBackStack() },
                            onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                            onToggleWatchlist = { viewModel.toggleWatchlist(it.id) },
                            onRateMovie = { rating -> viewModel.rateMovie(it.id, rating) }
                        )
                    } ?: LoadingScreen()
                }
            }
        }
    }
}
