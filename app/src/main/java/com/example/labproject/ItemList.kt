package com.example.labproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemList(navController: NavController, state: ListItemState, onEvent: (ListItemEvent) -> Unit) {

    var selectedItemPosition by remember { mutableStateOf(-1) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ListItemEvent.ShowDialog)
            },
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add unit"
                )
            }
        },

        modifier = Modifier
            .padding(16.dp)
            .padding(bottom = 60.dp)
    ) { padding ->
        if(state.isNew){
            AddUnitDialog(state = state, onEvent = onEvent)
        }
        if(state.isBeingEdited){
            EditUnitDialog(state = state, onEvent = onEvent, id = selectedItemPosition)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Unit list",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(state.listItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedItemPosition = state.listItems.indexOf(item)
                            onEvent(ListItemEvent.ShowEditDialog)
                        }

                ){
                    var checked by remember { mutableStateOf(item.isChecked) }
                    Image(
                        painter = when(item.item_type){
                            "Lich" -> painterResource(id = R.drawable.playericon)
                            else -> painterResource(id = R.drawable.skeletonicon)
                        },
                        contentDescription = "Unit type",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(100.dp, 100.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = item.text_name,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = "Spec: ${item.text_spec}\nStrength: ${item.item_strength}",
                            fontSize = 12.sp,
                        )

                    }
                    var showDialog by remember {
                        mutableStateOf(false)
                    }
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }

                    if(showDialog) {
                        AlertDialog(
                            title = { Text(text = "Delete") },
                            text = {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Text(
                                        text = "Czy na pewno chcesz usunąć?",
                                    )

                                }
                            },
                            onDismissRequest = {
                                showDialog = false
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                        onEvent(ListItemEvent.DeleteItem(item))
                                    }
                                ) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        showDialog = false
                                    }
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        )
                    }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            onEvent(ListItemEvent.SetChecked(checked, item.id!!))
                        },
                        modifier = Modifier.size(40.dp)
                    )
                }
                Divider()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUnitDialog(
    state: ListItemState,
    onEvent: (ListItemEvent) -> Unit,
    modifier: Modifier = Modifier
){
    val choices = listOf("Lich", "Skeleton", "Ghoul")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(choices[0]) }
    val strength = listOf("1","2","3","4","5")
    val (selectedStrength, setSelectedStrength) = remember { mutableStateOf(strength[0]) }
    var danger by remember { mutableStateOf(false) }

    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Add unit") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ){
                Text(
                    text = "Name",
                )
                TextField(
                    value = state.text_name,
                    onValueChange = {
                        onEvent(ListItemEvent.SetName(it))
                    },
                    placeholder = { Text(text = "Name") },
                )
                Text(
                    text = "Specifics",
                )
                TextField(
                    value = state.text_spec,
                    onValueChange = {
                        onEvent(ListItemEvent.SetSpec(it))
                    },
                    placeholder = { Text(text = "Specifics") },
                )
                Text(
                    text = "Dangerous",
                )
                Checkbox(
                    checked = danger,
                    onCheckedChange = {
                        danger = it
                        onEvent(ListItemEvent.SetDanger(danger))
                                      },
                    modifier = Modifier.size(40.dp)
                )
                mySpinner(label ="strength", choices = strength, selectedOption = selectedStrength, setSelected = setSelectedStrength, onEvent = onEvent)
                mySpinner(label = "type", choices =choices , selectedOption = selectedOption, setSelected = setSelectedOption, onEvent = onEvent)
            }
        },
        onDismissRequest = {
            onEvent(ListItemEvent.HideDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(ListItemEvent.SaveItem)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(ListItemEvent.HideDialog)
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun EditUnitDialog(
    state: ListItemState,
    onEvent: (ListItemEvent) -> Unit,
    modifier: Modifier = Modifier,
    id: Int
){
    var name by remember { mutableStateOf(state.listItems[id].text_name) }
    var spec by remember { mutableStateOf(state.listItems[id].text_spec) }
    var danger by remember { mutableStateOf(state.listItems[id].dangerous) }
    var item_strength by remember { mutableFloatStateOf(state.listItems[id].item_strength) }
    var type by remember { mutableStateOf(state.listItems[id].item_type) }
    val choices = listOf("Lich", "Skeleton", "Ghoul")
    val (selectedOption, setSelectedOption) = remember { mutableStateOf(type) }
    val strength = listOf("1","2","3","4","5")
    val (selectedStrength, setSelectedStrength) = remember { mutableStateOf(item_strength.toString()) }

    AlertDialog(
        modifier = modifier,
        title = { Text(text = "Show and Edit unit") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ){
                Text(
                    text = "Name",
                )
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        onEvent(ListItemEvent.SetName(it))
                    },
                    placeholder = { Text(text = "Name") },
                )
                Text(
                    text = "Specifics",
                )
                TextField(
                    value = spec,
                    onValueChange = {
                        spec = it
                        onEvent(ListItemEvent.SetSpec(spec))
                    },
                    placeholder = { Text(text = "Specifics") },
                )
                Text(
                    text = "Dangerous",
                )
                Checkbox(
                    checked = danger,
                    onCheckedChange = { danger = it }
                )
                Text(
                    text = "Strength: $item_strength",
                )
                mySpinner(label ="strength", choices = strength, selectedOption = selectedStrength, setSelected = setSelectedStrength, onEvent = onEvent)
                Text(
                    text = "Type: $type",
                )
                mySpinner(label = "type", choices =choices , selectedOption = selectedOption, setSelected = setSelectedOption, onEvent = onEvent)
            }
        },
        onDismissRequest = {
            onEvent(ListItemEvent.HideEditDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onEvent(ListItemEvent.SetDanger(danger))
                    onEvent(ListItemEvent.EditItem(state.listItems[id]))
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(ListItemEvent.HideEditDialog)
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun mySpinner(
    label: String,
    choices: List<String>,
    selectedOption: String,
    setSelected: (selected: String) -> Unit,
    onEvent: (ListItemEvent) -> Unit
) {
    var spinnerText by rememberSaveable { mutableStateOf(selectedOption) }
    var my_expanded by rememberSaveable { mutableStateOf(false) }

    Row {
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .width(150.dp),
            text = label,
            fontWeight = FontWeight.Bold
        )
        Box(
            Modifier
                .width(150.dp)
                .border(width = 1.dp, color = Color.Black)
        ) {
            Row(Modifier
                .padding(start = 12.dp)
                .clickable {
                    my_expanded = !my_expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedOption,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = my_expanded, onDismissRequest = { my_expanded = false }) {
                    choices.forEach { a_choice ->
                        DropdownMenuItem(
                            text = { Text(text = a_choice) },
                            onClick = {
                                my_expanded = false
                                spinnerText = a_choice
                                setSelected(spinnerText)
                                if(label == "type")
                                    onEvent(ListItemEvent.SetType(spinnerText))
                                else if(label == "strength")
                                    onEvent(ListItemEvent.SetStrength(spinnerText.toFloatOrNull() ?: 0f))
                            })

                    }
                }
            }
        }
    }
}

@Composable
fun deleteButton(
    state: ListItemState,
    onEvent: (ListItemEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        var showDialog by remember {
            mutableStateOf(false)
        }
        Button(onClick = {
            showDialog = true
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Checked"
            )
        }

        if(showDialog) {
            AlertDialog(
                modifier = modifier,
                title = { Text(text = "Delete") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = "Czy na pewno chcesz usunąć?",
                        )

                    }
                },
                onDismissRequest = {
                    showDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            onEvent(ListItemEvent.DeleteChecked)
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
    }
}





