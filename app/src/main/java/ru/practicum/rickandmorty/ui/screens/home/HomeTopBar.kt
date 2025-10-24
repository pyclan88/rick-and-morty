package ru.practicum.rickandmorty.ui.screens.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import ru.practicum.rickandmorty.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        modifier = modifier,
        title = {
            TextField(
                modifier = modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = { Text(stringResource(R.string.search_characters)) },
                trailingIcon = {
                    if (searchQuery.isBlank()) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(R.string.search_characters)
                        )

                    } else {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
                                contentDescription = stringResource(R.string.close_search_icon_description)
                            )
                        }

                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

        }
    )
}
