package com.butul0ve.urbanslang.bean

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "definitions")
data class Definition(@PrimaryKey(autoGenerate = true) var id: Long?,
                      @ColumnInfo(name = "definition") var definition: String,
                      @ColumnInfo(name = "permalink") var permalink: String,
                      @ColumnInfo(name = "thumbs_up") var thumbs_up: Int,
                      @ColumnInfo(name = "thumbs_down") var thumbs_down: Int,
                      @ColumnInfo(name = "author") var author: String,
                      @ColumnInfo(name = "word") var word: String,
                      @ColumnInfo(name = "example") var example: String,
                      @ColumnInfo(name = "favorite") var favorite: Int = 0) {

    override fun hashCode(): Int {
        var hash = 17

        hash *= 31 * definition.hashCode()
        hash *= 31 * permalink.hashCode()
        hash *= 31 * thumbs_up
        hash *= 31 * thumbs_down
        hash += 31 * author.hashCode()
        hash *= 31 * word.hashCode()
        hash *= 31 * example.hashCode()
        hash += 31 * favorite

        return hash
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as Definition

        return definition == other.definition &&
                permalink == other.permalink &&
                thumbs_up == other.thumbs_up &&
                thumbs_down == other.thumbs_down &&
                author == other.author &&
                word == other.word &&
                example == other.example
    }
}