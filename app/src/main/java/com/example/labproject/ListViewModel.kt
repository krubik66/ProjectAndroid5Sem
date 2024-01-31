package com.example.labproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(private val dao: ListDao): ViewModel() {

    private val _state = MutableStateFlow(ListItemState())
    private val _listItems = dao.getItems().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    val state = combine(_state, _listItems) { state, listItems ->
        state.copy(listItems = listItems)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListItemState())

    fun onEvent(event: ListItemEvent) {
        when(event) {
            is ListItemEvent.DeleteItem -> {
                viewModelScope.launch {
                    dao.deleteItem(event.item)
                }
            }
            ListItemEvent.HideDialog -> {
                _state.update {
                    it.copy(isNew = false)
                }
            }
            is ListItemEvent.SetName -> {
                _state.update {
                    it.copy(text_name = event.name)
                }
            }
            is ListItemEvent.SetSpec -> {
                _state.update {
                    it.copy(text_spec = event.spec)
                }
            }
            is ListItemEvent.SetType -> {
                _state.update {
                    it.copy(item_type = event.type)
                }

            }
            is ListItemEvent.SetStrength -> {
                _state.update {
                    it.copy(item_strength = event.strength)
                }

            }
            is ListItemEvent.SetDanger -> {
                _state.update {
                    it.copy(dangerous = event.danger)
                }
            }
            ListItemEvent.ShowDialog -> {
                _state.update {
                    it.copy(isNew = true)
                }
            }
            ListItemEvent.ShowEditDialog -> {
                _state.update {
                    it.copy(isChecked = true)
                }
            }
            is ListItemEvent.EditItem -> {
                val selectedItem = event.item

                val existingItem = _listItems.value.find { it == selectedItem }

                if (existingItem != null) {
                    if(state.value.text_name != "")
                        existingItem.text_name = state.value.text_name
                    if(state.value.text_spec != "")
                        existingItem.text_spec = state.value.text_spec
                    if(state.value.item_strength != -1f)
                        existingItem.item_strength = state.value.item_strength
                    if(state.value.item_type != "")
                        existingItem.item_type = state.value.item_type

                    existingItem.dangerous = state.value.dangerous

                    viewModelScope.launch {
                        dao.upsertItem(existingItem)
                    }
                }

                // Reset the editing state
                _state.update {
                    it.copy(
                        text_name = "",
                        text_spec = "",
                        item_strength = -1f,
                        dangerous = false,
                        item_type = "",
                        isChecked = false
                    )
                }
            }

            ListItemEvent.HideEditDialog -> {
                _state.update {
                    it.copy(isChecked = false)
                }
            }
            ListItemEvent.SaveItem -> {
                var name = state.value.text_name
                var spec = state.value.text_spec
                var strength = state.value.item_strength
                var type = state.value.item_type
                val danger = state.value.dangerous

                if(name == "")
                    name = "Nameless"
                if(spec == "")
                    spec = "None"
                if(strength == -1f)
                    strength = 1f
                if(type == "")
                    type = "Lich"

                val item = DatabaseItem(name, spec, strength, type, danger)
                viewModelScope.launch {
                    dao.upsertItem(item)
                }
                _state.update {
                    it.copy(
                        text_name = "",
                        text_spec = "",
                        item_strength = -1f,
                        item_type = "",
                        dangerous = false,
                        isChecked = false,
                        isNew = false,
                    )
                }
            }
        }
    }


}