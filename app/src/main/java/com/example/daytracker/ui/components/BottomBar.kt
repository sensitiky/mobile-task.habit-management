package com.example.daytracker.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.daytracker.R

@Composable
fun BottomAppBarState(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.home,"home"),
        BottomNavItem("Tasks", R.drawable.tasks,"tasks"),
        BottomNavItem("Profile", R.drawable.profile,"profile")
    )
    NavigationBar {
        val selectedItem = remember { mutableIntStateOf(0) }
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = item.icon),
                        contentDescription = item.name,
                        modifier = Modifier.width(20.dp)
                    )
                },
                label = { Text(item.name) },
                selected = selectedItem.value == index,
                onClick = { selectedItem.value = index
                navController.navigate(item.route) }
            )
        }
    }
}

data class BottomNavItem(val name: String, val icon: Int,val route:String)