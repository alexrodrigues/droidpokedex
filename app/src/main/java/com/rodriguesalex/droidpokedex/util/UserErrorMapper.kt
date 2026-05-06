package com.rodriguesalex.droidpokedex.util

import androidx.annotation.StringRes
import com.rodriguesalex.droidpokedex.R
import retrofit2.HttpException
import java.io.IOException

data class ErrorInfo(
    @StringRes val titleRes: Int,
    @StringRes val messageRes: Int,
    @StringRes val detailRes: Int? = null,
    val detailArg: Any? = null,
)

object UserErrorMapper {
    fun from(throwable: Throwable): ErrorInfo =
        when (throwable) {
            is IOException ->
                ErrorInfo(
                    titleRes = R.string.error_network_title,
                    messageRes = R.string.error_network_message,
                )
            is HttpException ->
                ErrorInfo(
                    titleRes = R.string.error_server_title,
                    messageRes = R.string.error_server_message,
                    detailRes = R.string.error_server_detail,
                    detailArg = throwable.code(),
                )
            else ->
                ErrorInfo(
                    titleRes = R.string.error_unknown_title,
                    messageRes = R.string.error_unknown_message,
                )
        }

    fun invalidPokemonId(): ErrorInfo =
        ErrorInfo(
            titleRes = R.string.error_invalid_id_title,
            messageRes = R.string.error_invalid_id_message,
        )
}
