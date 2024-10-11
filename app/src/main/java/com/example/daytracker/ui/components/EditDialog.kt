import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.example.daytracker.data.model.Habit

@Composable
fun EditHabitDialog(
    habit: Habit,
    onDismiss: () -> Unit,
    onSave: (Habit) -> Unit
) {
    var newTitle by remember { mutableStateOf(TextFieldValue(habit.title)) }
    var newDescription by remember { mutableStateOf(TextFieldValue(habit.description)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Habit") },
        text = {
            Column {
                TextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Title") }
                )
                TextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Description") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(habit.copy(title = newTitle.text, description = newDescription.text))
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}