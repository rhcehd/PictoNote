package com.lhs94.pictonote.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    @PrimaryKey(true) val uid: Int?,
    val title: String,
    val text: String,
    val image: String,
): Serializable {
    constructor(title: String, text: String, image: String): this(null, title, text, image)
    constructor(title: String, text: String): this(title, text, image = "")
    constructor(text: String): this(title = "", text)
}
