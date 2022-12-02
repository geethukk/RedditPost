package com.yourparkingspace.network

import com.google.common.truth.Truth.assertThat
import com.yourparkingspace.common_utils.AppResult
import com.yourparkingspace.network.endpoints.PostsEndpointImpl
import com.yourparkingspace.network.models.CommentDTO
import com.yourparkingspace.network.models.MoreDTO
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class PostsEndpointTest {

    private lateinit var server: MockWebServer
    private lateinit var endpoint: PostsEndpointImpl

    @Before
    fun setup() {
        server = MockWebServer()
        val retrofit = TestNetworkModule.provideRetrofit(server)
        val api = TestNetworkModule.provideRedditApi(retrofit)
        endpoint = PostsEndpointImpl(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    /**
     * JSON has the following comment structure:
     *
     * - comment1
     * - comment2
     *    |- reply1
     *       |- reply1
     *       |- more (2)
     *    |- reply2
     *    |- more (2)
     * - more (100)
     */

    @Test
    fun test() {
        server.enqueueResponse("response_post_comments.json", 200)

        runBlocking {

            val result = endpoint.getComments("", "")
            if (result !is AppResult.Success) {
                fail("result is not a success")
                return@runBlocking
            }

            val comments = result.data.children

            // top level comments
            assertThat(comments.size).isEqualTo(3)
            assertThat(comments[0].data).isInstanceOf(CommentDTO::class.java)
            assertThat(comments[1].data).isInstanceOf(CommentDTO::class.java)
            assertThat(comments[2].data).isInstanceOf(MoreDTO::class.java)

            // first level of replies
            val replies1 = (comments[1].data as? CommentDTO)?.childComments?.children
            assertThat(replies1?.size).isEqualTo(3)
            assertThat(replies1?.get(0)?.data).isInstanceOf(CommentDTO::class.java)
            assertThat(replies1?.get(1)?.data).isInstanceOf(CommentDTO::class.java)
            assertThat(replies1?.get(2)?.data).isInstanceOf(MoreDTO::class.java)

            // second level of replies
            val replies2 = (replies1?.first()?.data as? CommentDTO)?.childComments?.children
            assertThat(replies2?.size).isEqualTo(2)
            assertThat(replies2?.get(0)?.data).isInstanceOf(CommentDTO::class.java)
            assertThat(replies2?.get(1)?.data).isInstanceOf(MoreDTO::class.java)
        }
    }
}
