package com.table

class Table(private val columns : Int, private var data : List<RowData> = mutableListOf()) {
    enum class DisplayType{
        ImageView,
        TextView
    }

    fun setData(data : List<RowData>) {
        this.data = data
    }
}


class RowData(vararg data : Any)