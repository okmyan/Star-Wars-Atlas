package com.okmyan.starwarsatlas.core.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.okmyan.starwarsatlas.core.datastore.LastRefreshDataStore
import com.okmyan.starwarsatlas.utils.mediatorResultOf
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalPagingApi::class)
abstract class CursorRemoteMediator<Entity : Any>(
    private val lastRefreshDataStore: LastRefreshDataStore,
    private val featureKey: String,
) : RemoteMediator<Int, Entity>() {

    override suspend fun initialize(): InitializeAction {
        val lastRefresh = lastRefreshDataStore.getTimestamp(featureKey)
            ?: return InitializeAction.LAUNCH_INITIAL_REFRESH

        return if (System.currentTimeMillis() - lastRefresh < CACHE_TIMEOUT) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Entity>,
    ): MediatorResult = mediatorResultOf {
        val cursor: String? = when (loadType) {
            LoadType.REFRESH -> null

            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = getCount() == 0)

                getNextCursor(lastItem) ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        val page = try {
            fetch(pageSize = state.config.pageSize, cursor = cursor)
        } catch (c: CancellationException) {
            throw c
        } catch (e: Exception) {
            // Refresh failed but cached data exists. Show it instead of surfacing the error
            if (loadType == LoadType.REFRESH && getCount() > 0) {
                Timber.w(e, "[$featureKey] Refresh failed, falling back to cache")
                return MediatorResult.Success(endOfPaginationReached = false)
            }
            throw e
        }
        save(items = page.items, nextCursor = page.nextCursor, clearFirst = loadType == LoadType.REFRESH)

        if (loadType == LoadType.REFRESH) {
            lastRefreshDataStore.setTimestamp(featureKey, System.currentTimeMillis())
        }

        MediatorResult.Success(endOfPaginationReached = page.nextCursor == null)
    }

    abstract suspend fun getCount(): Int
    abstract suspend fun getNextCursor(lastItem: Entity): String?
    abstract suspend fun fetch(pageSize: Int, cursor: String?): CursorPage<Entity>
    abstract suspend fun save(items: List<Entity>, nextCursor: String?, clearFirst: Boolean)

    companion object {
        private val CACHE_TIMEOUT = TimeUnit.HOURS.toMillis(1)
    }

}
