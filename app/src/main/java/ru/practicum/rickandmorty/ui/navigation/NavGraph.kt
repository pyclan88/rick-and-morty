package ru.practicum.rickandmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import ru.practicum.rickandmorty.domain.models.Filters
import ru.practicum.rickandmorty.ui.screens.details.CharacterDetailsScreen
import ru.practicum.rickandmorty.ui.screens.filters.FiltersScreen
import ru.practicum.rickandmorty.ui.screens.home.HomeScreen
import ru.practicum.rickandmorty.ui.screens.home.HomeScreenViewModel

const val CHARACTER_ID = "characterId"
const val FILTERS_RESULT_KEY = "filters_result"

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    val viewModel: HomeScreenViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            val newFilters = navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Filters>(FILTERS_RESULT_KEY)

            if (newFilters != null) {
                viewModel.onFiltersChanged(newFilters)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Filters>(FILTERS_RESULT_KEY)
            }

            HomeScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.Details.createRoute(characterId))
                },
                onFilterClick = {
                    navController.navigate(Screen.Filters.route)
                },
                viewModel = viewModel
            )
        }

        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument(CHARACTER_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt(CHARACTER_ID)
                ?: throw IllegalStateException("Character ID is required")

            CharacterDetailsScreen(
                characterId = characterId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Filters.route) {
            val currentFilters by viewModel.filters.collectAsState()

            FiltersScreen(
                onBackClick = { navController.popBackStack() },
                onApplyClick = { appliedFilters ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(FILTERS_RESULT_KEY, appliedFilters)
                    navController.popBackStack()

                },
                initialFilters = currentFilters
            )
        }
    }
}