package ru.practicum.rickandmorty.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.ui.MockObjects
import ru.practicum.rickandmorty.utils.getTranslatedGender
import ru.practicum.rickandmorty.utils.getTranslatedSpecies
import ru.practicum.rickandmorty.utils.getTranslatedStatus

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: Character,
    onCharacterClick: (Int) -> Unit,
    onFavoriteClick: (Character) -> Unit
) {
    var isFavorite by remember(character.id) { mutableStateOf(character.isFavorite) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onCharacterClick(character.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = character.image,
                    placeholder = painterResource(R.drawable.placeholder),
                    error = painterResource(R.drawable.placeholder),
                    contentDescription = stringResource(
                        R.string.character_item_image_description,
                        character.name
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = {
                        isFavorite = !isFavorite
                        onFavoriteClick(character)
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) {
                            ImageVector.vectorResource(R.drawable.ic_favorite_on)
                        } else {
                            ImageVector.vectorResource(R.drawable.ic_favorite_off)
                        },
                        contentDescription = stringResource(R.string.favorite_icon_description),
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                            RoundedCornerShape(topStart = 20.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusIndicator(status = character.status)
                    Spacer(modifier = Modifier.width(6.dp))
                    val translatedStatus = getTranslatedStatus(
                        status = character.status,
                        gender = character.gender
                    )
                    Text(
                        text = translatedStatus,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                val translatedGender = getTranslatedGender(gender = character.gender)
                val translatedSpecies = getTranslatedSpecies(species = character.species)
                Text(
                    text = "$translatedGender | $translatedSpecies",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(status: String) {
    val color = when (status) {
        "Alive" -> Color.Green
        "Dead" -> Color.Red
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
    ) {
        Surface(
            color = color,
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterItemPreview() {
    Surface {
        CharacterItem(
            character = MockObjects.rickSanchez,
            onCharacterClick = {},
            onFavoriteClick = {}
        )
    }
}
