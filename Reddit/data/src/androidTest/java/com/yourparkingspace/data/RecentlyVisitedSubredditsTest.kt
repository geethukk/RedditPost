package com.yourparkingspace.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.yourparkingspace.database.AppDatabase
import com.yourparkingspace.data.repositories.RecentSubredditsCache
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class RecentlyVisitedSubredditsTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    lateinit var recentSubsCache: RecentSubredditsCache

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = AppDatabase.getInMemoryDatabase(appContext)
        recentSubsCache = RecentSubredditsCache(db.visitedSubredditsDao())
    }

    @Test
    fun should_be_empty_on_first_launch() {
        runBlocking {
            recentSubsCache.observeRecentlyVisited()
                .take(1)
                .collect {
                    assertThat(it).isEmpty()
                }
        }
    }

    @Test
    fun should_add_and_delete_successfully() {
        runBlocking {
            recentSubsCache.addToVisited("name")
            recentSubsCache.addToVisited("name2")
            recentSubsCache.observeRecentlyVisited()
                .take(1)
                .collect {
                    assertThat(it).isEqualTo(listOf("name2", "name"))
                }

            recentSubsCache.deleteFromVisited("name")
            recentSubsCache.observeRecentlyVisited()
                .take(1)
                .collect {
                    assertThat(it).isEqualTo(listOf("name2"))
                }
        }
    }

    @Test
    fun should_not_exceed_the_limit_and_delete_oldest() {
        val count = RecentSubredditsCache.RECENT_SUBREDDITS_LIMIT + 1

        fun generateName(index: Int) = "name$index"

        runBlocking {
            repeat(count) {
                delay(5)
                recentSubsCache.addToVisited(generateName(it))
            }
            recentSubsCache.observeRecentlyVisited()
                .take(1)
                .collect {
                    assertThat(it.size).isEqualTo(RecentSubredditsCache.RECENT_SUBREDDITS_LIMIT)
                    assertThat(it).contains(generateName(count - 1))
                    assertThat(it).doesNotContain(generateName(0))
                }
        }
    }
}

