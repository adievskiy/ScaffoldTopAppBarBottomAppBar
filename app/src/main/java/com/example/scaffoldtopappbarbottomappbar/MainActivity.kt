package com.example.scaffoldtopappbarbottomappbar

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhoneBook()
        }
    }
}

@Composable
fun PhoneBook() {
    val contactsList = remember { mutableStateListOf<Contact>() }
    var contact by rememberSaveable { mutableStateOf("") }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    Scaffold(
        topBar = { TopBar(selectedContact) },
        bottomBar = { BottomBar(selectedContact) },
        floatingActionButton = {
            Fab(contact) {
                contactsList.add(Contact(contact))
                /*contact = ""*/
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputContact(contact) { newContact ->
                contact = newContact
            }
            ContactView(
                contactsList,
                onContactDelete = { contact ->
                contactsList.remove(contact)
            },
                onContactClick = { contact -> selectedContact = contact }
            )
        }
    }
}

@Composable
fun ContactView(contactsList: MutableList<Contact>, onContactDelete: (Contact) -> Unit, onContactClick: (Contact) -> Unit) {
    val listState = rememberLazyListState()
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    LazyColumn(
        state = listState
    ) {
        items(contactsList) { contact ->
            val isSelected = selectedContact == contact
            val backgroundColor by animateColorAsState(targetValue = if (isSelected) Color.Blue else Color.LightGray)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .background(
                        shape = RoundedCornerShape(10.dp),
                        color = backgroundColor
                    )
                    .padding(start = 3.dp)
                    .clickable {
                        selectedContact = if (isSelected) null else contact
                        onContactClick(contact)
                    }
            ) {
                Text(
                    text = contact.contact,
                    fontSize = 22.sp,
                    color = if (isSelected) Color.White else Color.Black
                )
                IconButton(onClick = { onContactDelete(contact) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Удалить")
                }
            }
        }
    }
}

@Composable
private fun InputContact(contact: String, onContactChange: (String) -> Unit) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = contact,
        onValueChange = onContactChange
    )
}

@Composable
fun Fab(newContact: String, onAddContact: () -> Unit) {
    val context = LocalContext.current
    FloatingActionButton(onClick = {
        if (newContact.isNotBlank()) onAddContact()
        else Toast.makeText(context, "Введите контакт", Toast.LENGTH_LONG).show()
    }) {
        Icon(Icons.Filled.Add, contentDescription = "Добавить")
    }
}

@Composable
fun BottomBar(selectedContact: Contact?) {
    val context = LocalContext.current
    BottomAppBar(
        actions = {
            IconButton(onClick = {
                if (selectedContact != null) {
                    Toast.makeText(
                        context,
                        "Сообщение абоненту ${selectedContact.contact} отправлено",
                        Toast.LENGTH_LONG
                    ).show()
                } else Toast.makeText(
                    context,
                    "Контакт не выбран",
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Отправить",
                    tint = Color.White
                )
            }
            IconButton(onClick = {
                if (selectedContact != null) {
                    Toast.makeText(
                        context,
                        "Контакт ${selectedContact.contact} отредактирован",
                        Toast.LENGTH_LONG
                    ).show()
                } else Toast.makeText(
                    context,
                    "Контакт не выбран",
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Icon(Icons.Filled.Edit, contentDescription = "Редактировать", tint = Color.White)
            }
        },
        containerColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(selectedContact: Contact?) {
    val context = LocalContext.current
    TopAppBar(
        title = { Text("Записная книга") },
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Filled.Menu, contentDescription = "Меню")
            }
        },
        actions = {
            IconButton(onClick = {
                if (selectedContact != null) {
                    Toast.makeText(
                        context,
                        "Звонок абоненту ${selectedContact.contact} совершен",
                        Toast.LENGTH_LONG
                    ).show()
                } else Toast.makeText(
                    context,
                    "Контакт не выбран",
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Icon(Icons.Filled.Call, contentDescription = "Звонок")
            }
            IconButton(onClick = {
                if (context is Activity) {
                    context.finish()
                }
            }) {
                Icon(Icons.Filled.Clear, contentDescription = "Закрыть")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = Color.White,
            navigationIconContentColor = MaterialTheme.colorScheme.primaryContainer,
            actionIconContentColor = Color.LightGray
        )
    )
}

data class Contact(val contact: String)  {
    override fun equals(other: Any?): Boolean {
        return other is Contact && other.contact == this.contact
    }

    override fun hashCode(): Int {
        return contact.hashCode()
    }
}