package ru.practicum.rickandmorty.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.androidx.compose.koinViewModel
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.ui.components.ErrorScreen
import ru.practicum.rickandmorty.ui.components.LoadingScreen
import ru.practicum.rickandmorty.ui.components.NothingFoundScreen
import ru.practicum.rickandmorty.utils.ConnectivityObserver

@Composable
fun HomeScreen(
    onCharacterClick: (Int) -> Unit,
    onFilterClick: () -> Unit = {},
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val characters: LazyPagingItems<Character> = viewModel.characters.collectAsLazyPagingItems()

    Log.d("CharactersDebug", "Loaded characters: ${characters.itemSnapshotList.items.joinToString { it.species }}")

    val filters by viewModel.filters.collectAsState()
    val networkStatus by viewModel.networkStatus.collectAsState()

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier.background(
                    if (networkStatus != ConnectivityObserver.Status.Available) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        Color.Transparent
                    }
                )
            ) {
                Column(
                    modifier = Modifier.statusBarsPadding()
                ) {
                    if (networkStatus != ConnectivityObserver.Status.Available) {
                        OfflineBanner()
                    }
                    HomeTopBar(
                        searchQuery = searchQuery,
                        onQueryChange = { query ->
                            searchQuery = query
                            viewModel.onSearchQueryChanged(query)
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFilterClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_filter_list),
                    contentDescription = stringResource(R.string.filters_fab_description),
                )
            }
        },
        content = { innerPadding ->
            val loadState = characters.loadState
            val isListEmpty = characters.itemCount == 0

            when {
                loadState.refresh is LoadState.Loading -> {
                    LoadingScreen(modifier = Modifier.padding(innerPadding))
                }

                loadState.refresh is LoadState.Error && isListEmpty -> {
                    ErrorScreen(
                        modifier = Modifier.padding(innerPadding),
                        error = (loadState.refresh as LoadState.Error).error,
                        onRetry = { characters.retry() }
                    )
                }

                loadState.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        isListEmpty &&
                        (searchQuery.isNotBlank() || filters.areActive())
                     -> {
                    NothingFoundScreen(
                        modifier = Modifier.padding(innerPadding),
                        query = searchQuery,
                        filters = filters
                    )
                }

                else -> {
                    HomeContent(
                        modifier = Modifier.padding(innerPadding),
                        characters = characters,
                        onCharacterClick = onCharacterClick,
                        networkStatus = networkStatus
                    )
                }
            }
        }
    )
}

@Composable
private fun OfflineBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_no_cloud),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.snackbar_offline),
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}