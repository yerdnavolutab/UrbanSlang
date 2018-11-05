package com.butul0ve.urbanslang.network

import com.butul0ve.urbanslang.bean.BaseResponse
import com.butul0ve.urbanslang.bean.BaseResponseTest
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class NetworkHelperTest {

    private lateinit var single: Single<BaseResponse>
    private lateinit var randomSingle: Single<BaseResponse>
    private lateinit var actualResponse: BaseResponse
    private lateinit var expectedResponse: BaseResponse

    private lateinit var actualRandomResponse: BaseResponse
    private lateinit var expectedRandomResponse: BaseResponse

    @Mock
    private val networkHelper : NetworkHelper = mock(AppNetworkHelper::class.java)

    @Before
    fun setUp() {
        val baseResponseTest = BaseResponseTest()
        actualResponse = baseResponseTest.getActual()
        expectedResponse = baseResponseTest.getExpected()

        actualRandomResponse = BaseResponse(listOf(), "random", listOf())
        expectedRandomResponse = BaseResponse(listOf(), "random", listOf())

        single = Single.just(actualResponse)
        randomSingle = Single.just(actualRandomResponse)
    }

    @Test
    fun getDataTest() {
        `when`(networkHelper.getData("specific")).thenReturn(single)
        networkHelper.getData("specific")
            .test()
            .assertNoErrors()
            .assertValue(expectedResponse)

        `when`(networkHelper.getData()).thenReturn(randomSingle)
        networkHelper.getData()
            .test()
            .assertNoErrors()
            .assertValue(expectedRandomResponse)
    }
}