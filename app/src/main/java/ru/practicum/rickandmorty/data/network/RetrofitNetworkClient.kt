package ru.practicum.rickandmorty.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.rickandmorty.data.NetworkClient
import ru.practicum.rickandmorty.data.network.response.ErrorResponse
import ru.practicum.rickandmorty.data.network.response.Response
import ru.practicum.rickandmorty.utils.isInternetAvailable
import javax.net.ssl.HttpsURLConnection

class RetrofitNetworkClient(
    private val context: Context,
    private val apiService: ApiService,
) : NetworkClient {
    override suspend fun doRequest(dto: ApiRequest): Response {
        if (!isInternetAvailable(context)) {
            return Response(HttpsURLConnection.HTTP_UNAVAILABLE)
        }

        return withContext(Dispatchers.IO) {
            try {
                dto.execute(apiService)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                when (e.code()) {
                    400 -> ErrorResponse(HttpsURLConnection.HTTP_BAD_REQUEST, errorBody)
                    404 -> Response(HttpsURLConnection.HTTP_NOT_FOUND)
                    else -> Response(HttpsURLConnection.HTTP_INTERNAL_ERROR)
                }
            } catch (e: Exception) {
                Response(HttpsURLConnection.HTTP_UNAVAILABLE)
            }
        }
    }
}