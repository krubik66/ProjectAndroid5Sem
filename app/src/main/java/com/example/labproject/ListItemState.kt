package com.example.labproject

data class ListItemState (
    var listItems: List<DatabaseItem> = emptyList(),

    var text_name : String = "",
    var text_spec : String = "",
    var item_strength : Float = -1f,
    var item_type : String = "",
    var dangerous : Boolean = false,
    var isChecked: Boolean = false,
    var isNew: Boolean = false,
    var isBeingEdited: Boolean = false
)