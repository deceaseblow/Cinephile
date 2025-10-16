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
import com.example.myapplication.ui.theme.MovieAppTheme
import com.example.myapplication.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "loading") {

                    // Loading Screen
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

                    // Home Screen
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onSearch = { query ->
                                navController.navigate("results/$query")
                            },
                            onMovieClick = { movieId ->
                                navController.navigate("details/$movieId")
                            },
                            onWatchlistClick = {
                                navController.navigate("watchlist")
                            },
                            onRecommendationsClick = { // 👈 Navigate to Recommendations
                                navController.navigate("recommendations")
                            }
                        )
                    }

                    // Results Screen
                    composable("results/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        ResultScreen(
                            query = query,
                            viewModel = viewModel,
                            onMovieClick = { movieId ->
                                navController.navigate("details/$movieId")
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    //  Movie Details
                    composable("details/{movieId}") { backStackEntry ->
                        val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
                        val movie = viewModel.selectedMovie.collectAsState().value

                        LaunchedEffect(movieId) {
                            if (movieId != null) {
                                viewModel.fetchMovieDetails(movieId)
                            }
                        }

                        movie?.let {
                            val isInWatchlist = viewModel.isInWatchlist(it.id)
                            DetailsScreen(
                                movie = it, // ✅ use the movie from ViewModel
                                inWatchlist = isInWatchlist,
                                onBack = { navController.popBackStack() },
                                onAddToWatchlist = { movieToAdd -> viewModel.addToWatchlist(movieToAdd) },
                                onRemoveFromWatchlist = { movieIdToRemove -> viewModel.removeFromWatchlist(movieIdToRemove) },
                                onToggleFavorite = { viewModel.toggleFavorite(it.id) },
                                onRateMovie = { rating -> viewModel.rateMovie(it.id, rating) }
                            )
                        } ?: LoadingScreen()
                    }

                    composable("watchlist") {
                        WatchlistScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onMovieClick = { movieId ->
                                navController.navigate("details/$movieId")
                            }
                        )
                    }

                    //  Recommendations
                    composable("recommendations") {
                        RecommendationScreen(
                            viewModel = viewModel,
                            onMovieClick = { movieId ->
                                navController.navigate("details/$movieId")
                            },
                            onBack = { navController.popBackStack() } // ✅ Added back button handler
                        )
                    }
                }
            }
        }
    }
}
