package ru.practicum.rickandmorty.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.practicum.rickandmorty.R

@Composable
fun getTranslatedStatus(status: String?, gender: String): String {
    return when (status) {
        "Alive" -> when (gender) {
            "Female" -> stringResource(R.string.status_alive_female)
            "Male" -> stringResource(R.string.status_alive_male)
            else -> stringResource(R.string.status_alive_genderless)
        }
        "Dead" -> when (gender) {
            "Female" -> stringResource(R.string.status_dead_female)
            "Male" -> stringResource(R.string.status_dead_male)
            else -> stringResource(R.string.status_dead_genderless)
        }
        else -> stringResource(R.string.filter_option_unknown)
    }
}

@Composable
fun getTranslatedGender(gender: String?): String {
    return when (gender) {
        "Female" -> stringResource(R.string.filter_option_female)
        "Male" -> stringResource(R.string.filter_option_male)
        "Genderless" -> stringResource(R.string.filter_option_genderless)
        else -> stringResource(R.string.filter_option_unknown)
    }
}

@Composable
fun getTranslatedSpecies(species: String?): String {
    return when (species) {
        "Human" -> stringResource(R.string.species_human)
        "Alien" -> stringResource(R.string.species_alien)
        "Humanoid" -> stringResource(R.string.species_humanoid)
        "Poopybutthole" -> stringResource(R.string.species_poopybutthole)
        "Mythological Creature" -> stringResource(R.string.species_mythological)
        "Animal" -> stringResource(R.string.species_animal)
        "Robot" -> stringResource(R.string.species_robot)
        "Cronenberg" -> stringResource(R.string.species_cronenberg)
        "Disease" -> stringResource(R.string.species_disease)
        "unknown" -> stringResource(R.string.filter_option_unknown)
        else -> species ?: stringResource(R.string.filters_any_option)
    }
}