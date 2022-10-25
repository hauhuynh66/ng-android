package com.app.data

class Message(val from : String? = null, val content : String? = null){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (from != other.from) return false
        if (content != other.content) return false

        return true
    }

    override fun hashCode(): Int {
        var result = from?.hashCode() ?: 0
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }
}