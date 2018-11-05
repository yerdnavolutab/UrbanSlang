package com.butul0ve.urbanslang.bean

import com.google.gson.Gson
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.FileReader
import java.util.ArrayList

private const val DIRECTORY = "src/test/resources"
private const val FILE_NAME = "base_definition_response.json"

class BaseResponseTest {

    private lateinit var expectedResponse: BaseResponse
    private lateinit var actualResponse: BaseResponse

    @Before
    fun setUp() {
        expectedResponse = BaseResponse(getTags(), "exact", getDefinitions())
        actualResponse = getBaseResponse()
    }

    @Test
    fun parseObjectTest() {
        assertEquals(expectedResponse, actualResponse)
    }

    private fun getBaseResponse(): BaseResponse {
        val file = DIRECTORY + File.separator + FILE_NAME
        return Gson().fromJson(FileReader(file), BaseResponse::class.java)
    }

    private fun getTags(): List<String> {
        return arrayListOf("wagwan", "crew")
    }

    private fun getDefinitions(): List<Definition> {
        val listOfDefinition = ArrayList<Definition>()
        val definition = Definition(
            null,
            "Means hello, anything new in your life? " +
                    "Identical to [wagwaan] and [wagwan], but is spelt wag1 on chat websites, " +
                    "because it is quicker to type.",
            "http://wag1.urbanup.com/397462",
            618,
            291,
            "Krackpipe",
            "Wag1",
            "Wag1, meh bedrins?",
            0
        )
        listOfDefinition.add(definition)
        return listOfDefinition
    }
}