package com.example.daytracker.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.daytracker.R
import com.example.daytracker.ui.viewmodel.ContextViewModel

@Composable
fun SearchBar(viewModel: ContextViewModel, onQueryChange: (String) -> Unit) {
    val user by viewModel.user.collectAsState()
    var query by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(40.dp))
    ) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                onQueryChange(it)
            },
            modifier = Modifier
                .weight(1f)
                .size(48.dp),
            leadingIcon = {
                AsyncImage(
                    model = R.drawable.taskhand,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            },
            placeholder = { Text("Search...", style = TextStyle(textAlign = TextAlign.Center)) },
            trailingIcon = {
                AsyncImage(
                    model = user.avatar,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }
        )
    }
}