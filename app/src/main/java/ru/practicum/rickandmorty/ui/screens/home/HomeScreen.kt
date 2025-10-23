package ru.practicum.rickandmorty.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import org.koin.androidx.compose.koinViewModel
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.ui.components.ErrorScreen
import ru.practicum.rickandmorty.ui.components.LoadingScreen
import ru.practicum.rickandmorty.utils.ConnectivityObserver

@Composable
fun HomeScreen(
    onCharacterClick: (Int) -> Unit,
    onFilterClick: () -> Unit = {},
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val characters: LazyPagingItems<Character> = viewModel.characters.collectAsLazyPagingItems()
    val networkStatus by viewModel.networkStatus.collectAsState()

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(networkStatus) {
        if (networkStatus != ConnectivityObserver.Status.Available) {
            snackbarHostState.showSnackbar("You are offline")
        }

        snapshotFlow { networkStatus }
            .drop(1)
            .collect { status ->
                when (status) {
                    ConnectivityObserver.Status.Available -> {
                        snackbarHostState.showSnackbar("You are online")
                    }

                    ConnectivityObserver.Status.Lost,
                    ConnectivityObserver.Status.Unavailable -> {
                        snackbarHostState.showSnackbar("You are offline")
                    }

                    else -> {}
                }
            }
    }



    Scaffold(
        topBar = {
            HomeTopBar(
                isSearchActive = isSearchActive,
                searchQuery = searchQuery,
                onQueryChange = { query ->
                    searchQuery = query
                    viewModel.onSearchQueryChanged(query)
                },
                onSearchClick = { isSearchActive = true },
                onCloseClick = {
                    isSearchActive = false
                    viewModel.onSearchQueryChanged("")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFilterClick) {
                Icon(
                   imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter_list),
                   contentDescription = "Show filters"
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { innerPadding ->
            val isInitialLoad = characters.loadState.refresh is LoadState.Loading
            val isInitialError =
                characters.loadState.refresh is LoadState.Error && characters.itemCount == 0
            when {
                isInitialLoad -> LoadingScreen(modifier = Modifier.padding(innerPadding))

                isInitialError -> ErrorScreen(
                    modifier = Modifier.padding(innerPadding),
                    error = (characters.loadState.refresh as LoadState.Error).error,
                    onRetry = { characters.retry() }
                )

                else -> HomeContent(
                    modifier = Modifier.padding(innerPadding),
                    characters = characters,
                    onCharacterClick = onCharacterClick,
                )
            }
        }
    )
}
