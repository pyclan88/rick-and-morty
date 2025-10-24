package ru.practicum.rickandmorty.ui.screens.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.CharacterFilters
import ru.practicum.rickandmorty.utils.getTranslatedSpecies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    onBackClick: () -> Unit,
    onApplyClick: (CharacterFilters) -> Unit,
    initialFilters: CharacterFilters
) {
    val initialCharacterFilters = initialFilters

    var status by remember { mutableStateOf(initialCharacterFilters.status) }
    var gender by remember { mutableStateOf(initialCharacterFilters.gender) }
    var species by remember { mutableStateOf(initialCharacterFilters.species) }
    var type by remember { mutableStateOf(initialCharacterFilters.type) }
    var isFavoritesOnly by remember { mutableStateOf(initialCharacterFilters.isFavoritesOnly) }

    LaunchedEffect(initialFilters) {
        status = initialCharacterFilters.status
        gender = initialCharacterFilters.gender
        species = initialCharacterFilters.species
        type = initialCharacterFilters.type
        isFavoritesOnly = initialCharacterFilters.isFavoritesOnly
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.filters_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.filters_back_description)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                onClick = {
                    val newFilters = CharacterFilters(
                        status = status,
                        gender = gender,
                        species = species,
                        type = type,
                        isFavoritesOnly = isFavoritesOnly
                    )
                    onApplyClick(newFilters)
                },
            ) {
                Text(stringResource(R.string.filters_apply))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.filters_favorites_only_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Switch(
                    checked = isFavoritesOnly,
                    onCheckedChange = { isFavoritesOnly = it }
                )
            }

            FilterSection(
                title = stringResource(R.string.filters_status_title),
                options = FilterOptions.statuses,
                selectedOption = status,
                onOptionSelected = { status = it },
                labelForOption = { option -> statusLabel(option) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilterSection(
                title = stringResource(R.string.filters_gender_title),
                options = FilterOptions.genders,
                selectedOption = gender,
                onOptionSelected = { gender = it },
                labelForOption = { option -> genderLabel(option) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilterSection(
                title = stringResource(R.string.filters_species_title),
                options = FilterOptions.species,
                selectedOption = species,
                onOptionSelected = { species = it },
                labelForOption = { option -> getTranslatedSpecies(option) }
            )
            OutlinedTextField(
                value = species ?: "",
                onValueChange = { species = it.ifBlank { null } },
                label = { Text(stringResource(R.string.filters_species_custom_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.filters_type_label),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = type ?: "",
                onValueChange = { type = it.ifBlank { null } },
                label = { Text(stringResource(R.string.filters_type_label)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}

@Composable
private fun statusLabel(status: String?): String {
    return when (status) {
        "Alive" -> stringResource(R.string.filter_option_alive)
        "Dead" -> stringResource(R.string.filter_option_dead)
        "unknown" -> stringResource(R.string.filter_option_unknown)
        else -> status ?: stringResource(R.string.filters_any_option)
    }
}

@Composable
private fun genderLabel(gender: String?): String {
    return when (gender) {
        "Female" -> stringResource(R.string.filter_option_female)
        "Male" -> stringResource(R.string.filter_option_male)
        "Genderless" -> stringResource(R.string.filter_option_genderless)
        "unknown" -> stringResource(R.string.filter_option_unknown)
        else -> gender ?: stringResource(R.string.filters_any_option)
    }
}

@Composable
private fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    labelForOption: @Composable (String?) -> String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        val allOptions = listOf<String?>(null) + options

        allOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = null
                )
                Text(
                    text = labelForOption(option),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun FiltersScreenPreview() {
    val characterFilters = CharacterFilters(status = "Alive", gender = "Male")
    FiltersScreen(
        onBackClick = {},
        onApplyClick = {},
        initialFilters = characterFilters
    )
}

@Preview
@Composable
private fun FilterSectionPreview() {
    FilterSection(
        title = "Status",
        options = FilterOptions.statuses,
        selectedOption = "Alive",
        onOptionSelected = {},
        labelForOption = { it ?: "Any" }
    )
}