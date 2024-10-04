package com.example.daytracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun Login(onLogin: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") })
        Button(onClick = { onLogin(username, password) }) { Text("Login") }
    }
}