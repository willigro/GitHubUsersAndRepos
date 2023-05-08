package com.rittmann.users.repos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.rittmann.components.R
import com.rittmann.components.theme.AppTheme
import com.rittmann.components.ui.ProgressScreen
import com.rittmann.components.ui.TextBodyBold
import com.rittmann.components.ui.TextH2
import com.rittmann.components.ui.error
import com.rittmann.datasource.network.data.RepositoryResult
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserReposList(
    modifier: Modifier,
    reposState: StateFlow<PagingData<RepositoryResult>>,
) {
    val repos = reposState.collectAsLazyPagingItems()

    if (repos.loadState.append == LoadState.Loading || repos.loadState.refresh == LoadState.Loading) {
        ProgressScreen(modifier = Modifier)
    }

    Column(
        modifier = modifier
    ) {
        TextH2(text = stringResource(id = R.string.user_details_section_repositories))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.dimensions.paddingTopBetweenComponentsMedium)
        ) {
            item {
                if (repos.loadState.error() != null) {
                    TextBodyBold(
                        text = "Error",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }

            items(
                lazyPagingItems = repos,
            ) { repo ->
                repo?.also {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppTheme.dimensions.paddingSmall),
                    ) {
                        TextBodyBold(
                            text = repo.name ?: repo.id,
                            modifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
                            textAlign = TextAlign.Start,
                        )

                        Divider(
                            modifier = Modifier
                                .background(AppTheme.colors.primary)
                                .fillMaxWidth()
                                .height(AppTheme.dimensions.divisor)
                        )
                    }
                }
            }
        }
    }
}