package com.example.composepractice.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.composepractice.R
import com.example.composepractice.data.model.NotesModel
import com.example.composepractice.ui.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNotesActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val userId = viewModel.getUserIdSession().collectAsState(initial = -1)

                    Scaffold { innerPadding ->
                        AddNoteScreen(
                            modifier = Modifier.padding(innerPadding),
                            onSaveNote = { title, content ->
                                // Handle the saved note here (e.g., save to DB or return result)
                                lifecycleScope.launch {
                                    val returnCheck = viewModel.insertNote(
                                        NotesModel(
                                            title = title, content = content,
                                            createdBy = userId.value
                                        )
                                    )
                                    if (returnCheck > 0) {
                                        Toast.makeText(
                                            this@AddNotesActivity,
                                            "Note Saved: $title",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@AddNotesActivity,
                                            "Insert failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
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
    val isNotesTitleInvalid = remember { mutableStateOf(false) }

    var noteTextContent by remember { mutableStateOf("") }
    val isNotesContentInvalid = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Add New Note", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = noteTextTitle,
            onValueChange = {
                noteTextTitle = it
                isNotesTitleInvalid.value = false
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = isNotesTitleInvalid.value,
            trailingIcon = {
                if (isNotesTitleInvalid.value) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isNotesTitleInvalid.value) {
                    Text(
                        text = stringResource(R.string.error_notes_title),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            })
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = noteTextContent,
            onValueChange = { noteTextContent = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            isError = isNotesContentInvalid.value,
            trailingIcon = {
                if (isNotesContentInvalid.value) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
            supportingText = {
                if (isNotesContentInvalid.value) {
                    Text(
                        text = stringResource(R.string.error_notes_content),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            })
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (validateTextField(
                            noteTextTitle, isNotesTitleInvalid
                        ) && validateTextField(
                            noteTextContent, isNotesContentInvalid
                        )
                    ) {
                        onSaveNote(noteTextTitle, noteTextContent)
                    }
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

