package ru.practicum.rickandmorty.data.network.response

import javax.net.ssl.HttpsURLConnection

open class Response(open val resultCode: Int = HttpsURLConnection.HTTP_OK) {
}