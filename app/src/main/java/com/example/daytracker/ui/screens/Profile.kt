package com.example.daytracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.daytracker.data.model.User
import com.example.daytracker.ui.viewmodel.ContextViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(viewModel: ContextViewModel) {
    Scaffold() {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.height(100.dp)) { }
            Column {
                Header(onDismiss = {
                    viewModel.logoutUser()
                })
            }
        }
    }

}

@Composable
fun Header(onDismiss: () -> Unit) {
    Box(modifier = Modifier.height(100.dp)) { }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(250.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val user = User()
        user.avatar = "https://avatars.githubusercontent.com/u/1"
        user.name = "Mario"
        user.email = "mariomcorrea3@gmail.com"
        AsyncImage(
            model = user.avatar,
            contentDescription = "User Avatar",
            modifier = Modifier.size(64.dp)
        )
        Text(
            user.name,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            user.email,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onDismiss) { Text("Cerrar Sesión") }
    }
}
