package com.rittmann.components.ui

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

fun CombinedLoadStates.isError(): Throwable? = when {
    refresh is LoadState.Error -> (refresh as LoadState.Error).error
    append is LoadState.Error -> (append as LoadState.Error).error
    prepend is LoadState.Error -> (prepend as LoadState.Error).error
    else -> null
}