package ru.practicum.rickandmorty.ui.screens.filters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.rickandmorty.R
import ru.practicum.rickandmorty.domain.models.Filters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    onBackClick: () -> Unit,
    onApplyClick: (Filters) -> Unit,
    initialFilters: Filters
) {
    var status by remember { mutableStateOf(initialFilters.status) }
    var gender by remember { mutableStateOf(initialFilters.gender) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Filters") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    val newFilter = Filters(status = status, gender = gender)
                    onApplyClick(newFilter)
                },
            ) {
                Text("Apply Filters")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            FilterSection(
                title = "Status",
                options = listOf("Alive", "Dead", "unknown"),
                selectedOption = status,
                onOptionSelected = { newStatus -> status = newStatus }
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilterSection(
                title = "Gender",
                options = listOf("Female", "Male", "Genderless", "unknown"),
                selectedOption = gender,
                onOptionSelected = { newGender -> gender = newGender }
            )
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit
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
                    text = option ?: "Any",
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
    val filters = Filters(status = "Alive", gender = "Male")
    FiltersScreen(
        onBackClick = {},
        onApplyClick = {},
        initialFilters = filters
    )
}

@Preview
@Composable
private fun FilterSectionPreview() {
    FilterSection(
        title = "Status",
        options = listOf("Alive", "Dead", "Unknown"),
        selectedOption = "Alive",
        onOptionSelected = {}
    )
}