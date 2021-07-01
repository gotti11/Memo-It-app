package com.gotti.memoit.others

data class PhotoTaken(private val photo: Int, private val name: String, private val dateTaken: String) {

    val getPhoto: Int
        get() = photo

    val getName: String
        get() = name

    val getDateTaken: String
        get() = dateTaken
}