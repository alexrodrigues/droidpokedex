package com.rodriguesalex.details.domain.model

enum class DetailsRemoteSource {
    NETWORK,
    CACHE,
}

data class DetailsFetchOutcome<T>(
    val value: T,
    val source: DetailsRemoteSource,
)
