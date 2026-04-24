package com.okmyan.starwarsatlas.core.paging

data class CursorPage<Value : Any>(
    val items: List<Value>,
    val nextCursor: String?,
)
