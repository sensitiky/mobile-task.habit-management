package com.example.daytracker.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.daytracker.data.model.Tasks
import com.example.daytracker.data.model.User
import com.example.daytracker.ui.components.AdMobBanner
import com.example.daytracker.ui.theme.Typography
import com.example.daytracker.ui.viewmodel.ContextViewModel
import com.example.daytracker.ui.viewmodel.TasksViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ContextViewModel,
    taskViewModel: TasksViewModel,
    onNavigate: (String) -> Unit
) {
    val user by viewModel.user.collectAsState()
    val tasks by taskViewModel.task.observeAsState(emptyList())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    viewModel,
                    onNavigate = onNavigate,
                    onCloseDrawer = { scope.launch { drawerState.close() } })
            }
        },
        gesturesEnabled = drawerState.isOpen
    ) {
        Scaffold(
            topBar = { TopBar(drawerState, scope) },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item { Header(user = user) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { StatisticsSection() }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { RecentActivitiesSection(tasks = tasks) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { AchievementsSection() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = {
            Text(
                "Profile",
                style = Typography.headlineMedium.copy(fontSize = 18.sp),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { scope.launch { drawerState.open() } },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun Header(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AdMobBanner()
        AsyncImage(
            model = user.avatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            user.name,
            style = Typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            user.email,
            style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun StatisticsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Your Progress",
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(3) { index ->
                StatCard(
                    title = when (index) {
                        0 -> "Daily Goal"
                        1 -> "Weekly Streak"
                        else -> "Monthly Progress"
                    },
                    value = when (index) {
                        0 -> "80%"
                        1 -> "5 days"
                        else -> "65%"
                    },
                    color = when (index) {
                        0 -> MaterialTheme.colorScheme.primary
                        1 -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.tertiary
                    }
                )
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                style = Typography.bodyMedium.copy(color = color)
            )
            Text(
                value,
                style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = color)
            )
        }
    }
}

@Composable
fun RecentActivitiesSection(tasks: List<Tasks>) {
    val completedTasks = tasks.filter { it.completed }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Recent Activities",
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                completedTasks.forEach { task ->
                    ActivityItem(
                        title = task.title,
                        time = task.completedDate.toString(),
                        icon = Icons.Outlined.Restaurant
                    )
                }
            }
        }
    }
}

@Composable
fun ActivityItem(
    title: String,
    time: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = Typography.bodyMedium)
            Text(
                time,
                style = Typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
    }
}

@Composable
fun AchievementsSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Achievements",
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(4) { index ->
                AchievementItem(
                    title = when (index) {
                        0 -> "Early Bird"
                        1 -> "7-Day Streak"
                        2 -> "Fitness Pro"
                        else -> "Super User"
                    },
                    icon = when (index) {
                        0 -> Icons.Outlined.WbSunny
                        1 -> Icons.Outlined.Whatshot
                        2 -> Icons.Outlined.FitnessCenter
                        else -> Icons.Outlined.Star
                    }
                )
            }
        }
    }
}

@Composable
fun AchievementItem(title: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Card(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            title,
            style = Typography.bodySmall,
            modifier = Modifier.width(80.dp)
        )
    }
}

@Composable
fun Premium() {
    // Coroutine scope for managing the simulated payment
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var paymentStatus by remember { mutableStateOf<String?>(null) }

    // Function simulating a Stripe payment check
    fun simulateStripePayment() {
        scope.launch {
            isLoading = true
            delay(2000)
            val isSuccess = (0..1).random() == 1 // Randomly simulate success or failure
            paymentStatus = if (isSuccess) "Payment Successful!" else "Payment Failed. Try again."
            isLoading = false
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Star,
                contentDescription = "Premium",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Upgrade to Premium",
                style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Unlock all features and remove ads",
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Payment status message
            paymentStatus?.let {
                Text(
                    text = it,
                    color = if (it == "Payment Successful!") MaterialTheme.colorScheme.primary else Color.Red,
                    style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Upgrade button with loading state
            Button(
                onClick = { simulateStripePayment() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .width(150.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(20.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Upgrade Now")
                }
            }
        }
    }
}


@Composable
fun DrawerContent(
    viewModel: ContextViewModel,
    onNavigate: (String) -> Unit,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Menu",
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        DrawerItem("Tasks", Icons.AutoMirrored.Outlined.Assignment) {
            onNavigate("tasks")
            onCloseDrawer()
        }
        DrawerItem("Habits", Icons.Outlined.Repeat) {
            onNavigate("home")
            onCloseDrawer()
        }
        DrawerItem("Statistics", Icons.Outlined.BarChart) {
            onNavigate("statistics")
            onCloseDrawer()
        }
        DrawerItem("Settings", Icons.Outlined.Settings) {
            onNavigate("profile")
            onCloseDrawer()
        }
        Spacer(modifier = Modifier.weight(1f))
        Premium()
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.logoutUser() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .width(150.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ExitToApp,
                contentDescription = "Log Out",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Log Out")
        }
    }
}

@Composable
fun DrawerItem(title: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            title,
            style = Typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}