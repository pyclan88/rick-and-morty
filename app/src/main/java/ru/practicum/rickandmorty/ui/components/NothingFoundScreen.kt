package ru.practicum.rickandmorty.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.CharacterFilters

@Composable
fun NothingFoundScreen(
    modifier: Modifier = Modifier,
    query: String,
    characterFilters: CharacterFilters
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.nothing_found_wubba),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = when {
                query.isNotBlank() && characterFilters.areActive() ->
                    stringResource(R.string.nothing_found_query_and_filters, query)

                query.isNotBlank() ->
                    stringResource(R.string.nothing_found_query_only, query)

                characterFilters.areActive() ->
                    stringResource(R.string.nothing_found_filters_only)

                else ->
                    stringResource(R.string.nothing_found_fallback)
            },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}