package com.app.data

data class FootballTeam(val id : Int, val name : String, val iconUrl : String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FootballTeam

        if (id != other.id) return false
        if (name != other.name) return false
        if (iconUrl != other.iconUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + iconUrl.hashCode()
        return result
    }
}