package ru.practicum.rickandmorty.ui.screens.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.ui.MockObjects
import ru.practicum.rickandmorty.ui.components.LoadingScreen
import ru.practicum.rickandmorty.utils.getTranslatedGender
import ru.practicum.rickandmorty.utils.getTranslatedSpecies
import ru.practicum.rickandmorty.utils.getTranslatedStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    onBackClick: () -> Unit,
    viewModel: CharacterDetailsViewModel = koinViewModel(parameters = { parametersOf(characterId) })
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.details_back_description)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val currentState = state) {
                is CharacterDetailsState.Loading ->
                    LoadingScreen()

                is CharacterDetailsState.Error ->
                    Text(stringResource(R.string.details_error_message, currentState.message))

                is CharacterDetailsState.Content ->
                    CharacterDetailsContent(currentState.character)
            }
        }
    }
}

@Composable
fun CharacterDetailsContent(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = character.image,
            placeholder = painterResource(R.drawable.placeholder),
            error = painterResource(R.drawable.placeholder),
            contentDescription = "Image of ${character.name}",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            val translatedStatus = getTranslatedStatus(character.status, character.gender)
            val translatedSpecies = getTranslatedSpecies(character.species)
            val translatedGender = getTranslatedGender(character.gender)
            val translatedOrigin = getTranslatedLocation(character.origin.name)
            val translatedLocation = getTranslatedLocation(character.location.name)

            SectionTitle(stringResource(R.string.details_section_properties))
            DetailRow(stringResource(R.string.details_label_status), translatedStatus)
            DetailRow(stringResource(R.string.details_label_species), translatedSpecies)
            DetailRow(stringResource(R.string.details_label_gender), translatedGender)
            if (character.type.isNotBlank()) {
                DetailRow(stringResource(R.string.details_label_type), character.type)
            }

            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(stringResource(R.string.details_section_location))
            DetailRow(stringResource(R.string.details_label_origin), translatedOrigin)
            DetailRow(stringResource(R.string.details_label_last_location), translatedLocation)

            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(stringResource(R.string.details_section_appearances))
            DetailRow(
                stringResource(R.string.details_label_episode_count),
                character.episode.size.toString()
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineBreak = LineBreak.Heading
            ),
            textAlign = TextAlign.End,
            modifier = Modifier
                .weight(0.4f)
                .padding(start = 8.dp)
        )
    }
}

@Composable
private fun getTranslatedLocation(location: String): String {
    return when (location) {
        "Citadel of Ricks" -> stringResource(R.string.location_citadel_of_ricks)
        "Earth (Replacement Dimension)" -> stringResource(R.string.location_earth_replacement_dimension)
        "Abadango" -> stringResource(R.string.location_abadango)
        "Testicle Monster Dimension" -> stringResource(R.string.location_testicle_monster_dimension)
        "Worldender's lair" -> stringResource(R.string.location_worldenders_lair)
        "Anatomy Park" -> stringResource(R.string.location_anatomy_park)
        "Interdimensional Cable" -> stringResource(R.string.location_interdimensional_cable)
        "Immortality Field Resort" -> stringResource(R.string.location_immortality_field_resort)
        "Signus 5 Expanse" -> stringResource(R.string.location_signus_5_expanse)
        "Post-Apocalyptic Earth" -> stringResource(R.string.location_post_apocalyptic_earth)
        "Purge Planet" -> stringResource(R.string.location_purge_planet)
        "Venzenulon 7" -> stringResource(R.string.location_venzenulon_7)
        "Bepis 9" -> stringResource(R.string.location_bepis_9)
        "Earth (C-500A)" -> stringResource(R.string.location_earth_c_500a)
        "Earth (C-137)" -> stringResource(R.string.location_earth_c_137)
        "Earth (Evil Rick's Target Dimension)" -> stringResource(R.string.location_earth_evil_ricks_target_dimension)
        "Nuptia 4" -> stringResource(R.string.location_nuptia_4)
        "Fantasy World" -> stringResource(R.string.location_fantasy_world)
        "Planet Squanch" -> stringResource(R.string.location_planet_squanch)
        "Mr. Goldenfold's dream" -> stringResource(R.string.location_mr_goldenfolds_dream)
        "Rick's Battery Microverse" -> stringResource(R.string.location_ricks_battery_microverse)
        "The Menagerie" -> stringResource(R.string.location_the_menagerie)
        "Hideout Planet" -> stringResource(R.string.location_hideout_planet)
        "Zigerion's Base" -> stringResource(R.string.location_zigerions_base)
        "Giant's Town" -> stringResource(R.string.location_giants_town)
        "Unity's Planet" -> stringResource(R.string.location_unitys_planet)
        "Dorian 5" -> stringResource(R.string.location_dorian_5)
        "Rick's Memories" -> stringResource(R.string.location_ricks_memories)
        "St. Gloopy Noops Hospital" -> stringResource(R.string.location_st_gloopy_noops_hospital)
        "Roy: A Life Well Lived" -> stringResource(R.string.location_roy_a_life_well_lived)
        "Resort Planet" -> stringResource(R.string.location_resort_planet)
        "Interdimensional Customs" -> stringResource(R.string.location_interdimensional_customs)
        "Galactic Federation Prison" -> stringResource(R.string.location_galactic_federation_prison)
        "Hamster in Butt World" -> stringResource(R.string.location_hamster_in_butt_world)
        "Earth (Giant Telepathic Spiders Dimension)" -> stringResource(R.string.location_earth_giant_telepathic_spiders_dimension)
        "Alphabetrium" -> stringResource(R.string.location_alphabetrium)
        "Gazorpazorp" -> stringResource(R.string.location_gazorpazorp)
        "Jerryboree" -> stringResource(R.string.location_jerryboree)
        "Pluto" -> stringResource(R.string.location_pluto)
        "Kyle's Teenyverse" -> stringResource(R.string.location_kyles_teenyverse)
        "Plane" -> stringResource(R.string.location_plane)
        "unknown" -> stringResource(R.string.filter_option_unknown)
        else -> location
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailsContentPreview() {
    MaterialTheme {
        CharacterDetailsContent(character = MockObjects.rickSanchez)
    }
}