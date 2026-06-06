package com.example.composepractice.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


class AddNotesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold { innerPadding ->
                        AddNoteScreen(
                            modifier = Modifier.padding(innerPadding),
                            onSaveNote = { title, content ->
                                // Handle the saved note here (e.g., save to DB or return result)
                                Toast.makeText(
                                    this@AddNotesActivity,
                                    "Note Saved: $title",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            },
                            onCancel = { finish() })
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier, onSaveNote: (String, String) -> Unit, onCancel: () -> Unit
) {
    var noteTextTitle by remember { mutableStateOf("") }
    var noteTextContent by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add New Note", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = noteTextTitle,
            onValueChange = { noteTextTitle = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = noteTextContent,
            onValueChange = { noteTextContent = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    onSaveNote(noteTextTitle, noteTextContent)
                },
            ) {
                Text("Save Note")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    onCancel()
                },
            ) {
                Text("Cancel")
            }
        }
    }
}
