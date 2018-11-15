package com.butul0ve.urbanslang.data.db

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.butul0ve.urbanslang.bean.Definition
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UrbanDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: UrbanDatabase
    private lateinit var definition: Definition

    @Before
    fun initializeDefinition() {
        definition = Definition(
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

    @Before
    fun initDb() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            UrbanDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testWriteAndReadDefinition() {
        val definitionId = db.definitionDataDao().insert(definition)

        db.definitionDataDao().getDefinitionById(definitionId)
            .test()
            .assertNoErrors()

        assertEquals(definitionId, 1L)
    }

    @Test
    fun deleteDefinition() {
        val definitionId = db.definitionDataDao().insert(definition)
        definition.id = definitionId
        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.isNotEmpty() }

        db.definitionDataDao().delete(definition)
        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun deleteAllDefinitions() {
        val definitionId = db.definitionDataDao().insert(definition)
        definition.id = definitionId + 1
        db.definitionDataDao().insert(definition)

        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue {
                it.size == 2
            }

        db.definitionDataDao().deleteAll()

        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.isEmpty() }
    }

    @Test
    fun deleteOnlyCachedDefinitions() {
        db.definitionDataDao().insert(definition)
        db.definitionDataDao().insert(definition)
        db.definitionDataDao().insert(definition)
        db.definitionDataDao().insert(definition)

        definition.favorite = 1
        db.definitionDataDao().insert(definition)

        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 5 }

        db.definitionDataDao().deleteCachedDefinitions()

        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 1 }
    }

    @Test
    fun deleteOnlyFavoriteDefinitions() {
        db.definitionDataDao().insert(definition)

        definition.favorite = 1
        db.definitionDataDao().insert(definition)
        db.definitionDataDao().insert(definition)
        db.definitionDataDao().insert(definition)
        db.definitionDataDao().insert(definition)


        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 5 }

        db.definitionDataDao().deleteAllFavorites()

        db.definitionDataDao().getAll()
            .test()
            .assertNoErrors()
            .assertValue { it.size == 1 }
    }
}