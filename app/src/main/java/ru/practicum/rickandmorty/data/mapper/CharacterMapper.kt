package ru.practicum.rickandmorty.data.mapper

import ru.practicum.rickandmorty.data.dto.CharacterDto
import ru.practicum.rickandmorty.data.dto.LocationDto
import ru.practicum.rickandmorty.data.dto.OriginDto
import ru.practicum.rickandmorty.data.local.CharacterEntity
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.Location
import ru.practicum.rickandmorty.domain.models.Origin

fun CharacterDto.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin.toDomain(),
    location = location.toDomain(),
    image = image,
    episode = episode,
    url = url,
    created = created,
)

fun OriginDto.toDomain() = Origin(
    name = name,
    url = url
)

fun LocationDto.toDomain() = Location(
    name = name,
    url = url
)

fun CharacterEntity.toDomain() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    origin = origin,
    location = location,
    image = image,
    episode = episode,
    url = url,
    created = created,
    isFavorite = isFavorite
)
