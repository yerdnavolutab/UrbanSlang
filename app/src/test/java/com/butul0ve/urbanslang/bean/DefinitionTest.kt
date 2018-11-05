package com.butul0ve.urbanslang.bean

import com.google.gson.Gson
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.io.FileReader

private const val DIRECTORY = "src/test/resources"
private const val FILE_NAME = "definition_response.json"

class DefinitionTest {

    private lateinit var expectedDefinition: Definition
    private lateinit var actualDefinition: Definition

    @Before
    fun setUp() {
        expectedDefinition = getExpectedDefinition()
        actualDefinition = getDefinition()
    }

    @Test
    fun parseObjectTest() {
        assertEquals(expectedDefinition, actualDefinition)
    }

    private fun getDefinition(): Definition {
        val file = DIRECTORY + File.separator + FILE_NAME
        return Gson().fromJson(FileReader(file), Definition::class.java)
    }

    private fun getExpectedDefinition(): Definition {
        return Definition(
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
    }
}