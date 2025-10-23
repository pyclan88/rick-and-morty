package ru.practicum.rickandmorty.data.mappers

import ru.practicum.rickandmorty.data.dto.CharacterDto
import ru.practicum.rickandmorty.data.dto.InfoDto
import ru.practicum.rickandmorty.data.dto.LocationDto
import ru.practicum.rickandmorty.data.dto.OriginDto
import ru.practicum.rickandmorty.data.local.CharacterEntity
import ru.practicum.rickandmorty.data.network.response.CharactersResponse
import ru.practicum.rickandmorty.domain.models.Character
import ru.practicum.rickandmorty.domain.models.CharactersPage
import ru.practicum.rickandmorty.domain.models.Info
import ru.practicum.rickandmorty.domain.models.Location
import ru.practicum.rickandmorty.domain.models.Origin

fun CharactersResponse.toDomain(): CharactersPage = CharactersPage(
    info = info.toDomain(),
    results = results.map { it.toDomain() }
)

fun InfoDto.toDomain(): Info = Info(
    pages = pages,
    next = next,
)

fun CharacterDto.toDomain(): Character = Character(
    id = this.id,
    name = this.name,
    status = this.status,
    species = this.species,
    type = this.type,
    gender = this.gender,
    origin = this.origin.toDomain(),
    location = this.location.toDomain(),
    image = this.image,
    episode = this.episode,
    url = this.url,
    created = this.created
)

fun OriginDto.toDomain(): Origin = Origin(
    name = name,
    url = url
)

fun LocationDto.toDomain(): Location = Location(
    name = name,
    url = url
)

fun CharacterEntity.toDomain(): Character = Character(
    id = this.id,
    name = this.name,
    status = this.status,
    species = this.species,
    type = this.type,
    gender = this.gender,
    origin = this.origin,
    location = this.location,
    image = this.image,
    episode = this.episode,
    url = this.url,
    created = this.created
)

fun Character.toEntity(): CharacterEntity = CharacterEntity(
    id = this.id,
    name = this.name,
    status = this.status,
    species = this.species,
    type = this.type,
    gender = this.gender,
    origin = this.origin,
    location = this.location,
    image = this.image,
    episode = this.episode,
    url = this.url,
    created = this.created
)

fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
    id = this.id,
    name = this.name,
    status = this.status,
    species = this.species,
    type = this.type,
    gender = this.gender,
    origin = this.origin.toDomain(),
    location = this.location.toDomain(),
    image = this.image,
    episode = this.episode,
    url = this.url,
    created = this.created
)