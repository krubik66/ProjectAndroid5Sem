package com.example.labproject

data class ListItemState (
    var listItems: List<DatabaseItem> = emptyList(),

    var text_name : String = "Nameless",
    var text_spec : String = "None",
    var item_strength : Float = 1f,
    var item_type : String = "Lich",
    var dangerous : Boolean = false,
    var isChecked: Boolean = false,
    var isNew: Boolean = false
)