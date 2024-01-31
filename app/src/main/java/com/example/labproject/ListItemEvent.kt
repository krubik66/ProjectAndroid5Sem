package com.example.labproject


sealed interface ListItemEvent {
    object SaveItem: ListItemEvent
    data class SetName(val name: String): ListItemEvent
    data class SetSpec(val spec: String): ListItemEvent
    data class SetStrength(val strength: Float): ListItemEvent
    data class SetType(val type: String): ListItemEvent
    data class SetDanger(val danger: Boolean): ListItemEvent

    object ShowDialog: ListItemEvent
    object HideDialog: ListItemEvent

    data class EditItem(val item: DatabaseItem): ListItemEvent

    object HideEditDialog: ListItemEvent

    object ShowEditDialog: ListItemEvent

    data class DeleteItem(val item: DatabaseItem): ListItemEvent
}