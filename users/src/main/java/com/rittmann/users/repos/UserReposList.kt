package com.rittmann.users.repos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.rittmann.components.ui.TextBodyBold
import com.rittmann.datasource.network.data.RepositoryResult
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserReposList(
    modifier: Modifier,
    reposState: StateFlow<PagingData<RepositoryResult>>,
) {
    val repos = reposState.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        items(
            lazyPagingItems = repos,
        ) { repo ->
            repo?.also {
                TextBodyBold(text = repo.id)
            }
        }
    }
}