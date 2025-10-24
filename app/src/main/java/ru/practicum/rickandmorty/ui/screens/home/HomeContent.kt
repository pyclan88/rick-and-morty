package ru.practicum.rickandmorty.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.ui.components.LoadingScreen
import ru.practicum.rickandmorty.utils.ConnectivityObserver

@Composable
fun HomeContent(
    modifier: Modifier,
    characters: LazyPagingItems<Character>,
    onCharacterClick: (Int) -> Unit,
    networkStatus: ConnectivityObserver.Status
) {
    val isRefreshing = characters.loadState.refresh is LoadState.Loading

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { characters.refresh() },
        modifier = modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                count = characters.itemCount,
//                key = { index -> characters.peek(index)?.id ?: index }
            ) { index ->
//                val character = characters[index]
//                if (character != null) {
                characters[index]?.let { character ->
                    CharacterItem(
                        character = character,
                        onCharacterClick = onCharacterClick,
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                val appendState = characters.loadState.append

                when {
                    appendState is LoadState.Loading -> {
                        LoadingScreen(modifier = Modifier.padding(16.dp))
                    }

                    appendState is LoadState.Error -> {
                        if (networkStatus == ConnectivityObserver.Status.Available) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(onClick = { characters.retry() }) {
                                    Text(stringResource(R.string.action_retry))
                                }
                            }
                        }
                    }

                    appendState is LoadState.NotLoading && appendState.endOfPaginationReached && networkStatus == ConnectivityObserver.Status.Available -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = { characters.retry() }) {
                                Text(stringResource(R.string.action_retry))
                            }
                        }
                    }
                }
            }
        }
    }
}