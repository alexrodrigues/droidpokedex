package com.rodriguesalex.domain.model

enum class RemoteDataSource {
    NETWORK,
    CACHE,
}

data class FetchOutcome<T>(
    val value: T,
    val source: RemoteDataSource,
)
