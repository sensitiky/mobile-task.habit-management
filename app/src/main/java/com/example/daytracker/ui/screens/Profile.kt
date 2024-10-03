package com.example.daytracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.daytracker.data.model.User
import com.example.daytracker.ui.theme.Typography
import com.example.daytracker.ui.viewmodel.ContextViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(viewModel: ContextViewModel) {
    val user by viewModel.user.collectAsState()
    Scaffold(topBar = { TopBar() }) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.height(100.dp)) { }
            Column {
                Header(user = user, onDismiss = {
                    viewModel.logoutUser()
                })
            }
        }
    }

}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Text(
            text = "Profile",
            style = Typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun Header(user: User, onDismiss: () -> Unit) {
    Box(modifier = Modifier.height(100.dp)) { }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .size(250.dp)
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = user.avatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(40.dp))
        )
        Text(
            user.name,
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            user.email,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onDismiss) { Text("Log Out") }
    }
}
