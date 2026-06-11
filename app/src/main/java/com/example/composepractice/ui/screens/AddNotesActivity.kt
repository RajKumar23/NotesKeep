package com.example.composepractice.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

        val noteId = intent.getIntExtra("NOTE_ID", -1)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()

                    val userId = viewModel.getUserIdSession().collectAsState(initial = -1)
                    val noteToEdit by viewModel.detailNote.collectAsState(initial = null)

                    LaunchedEffect(noteId) {
                        if (noteId > 0) {
                            viewModel.getNoteById(noteId)
                        }
                    }

                    Scaffold { innerPadding ->
                        AddNoteScreen(
                            modifier = Modifier.padding(innerPadding),
                            operationString = if (noteId > 0) "Edit Note" else "Add New Note",
                            note = noteToEdit,
                            onSaveNote = { title, content ->
                                // Handle the saved note here (e.g., save to DB or return result)
                                if (noteToEdit?.title == title && noteToEdit?.content == content) {
                                    finish()
                                } else {
                                    lifecycleScope.launch {
                                        val noteToSave = NotesModel(
                                            id = if (noteId > 0) noteId else 0,
                                            title = title,
                                            content = content,
                                            createdBy = userId.value,
                                            isFavorite = noteToEdit?.isFavorite ?: false
                                        )
                                        val returnCheck = if (noteId > 0) {
                                            viewModel.updateNote(noteToSave)
                                            1L // Assuming update returns void or success
                                        } else {
                                            viewModel.insertNote(noteToSave)
                                        }

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
                                }
                            },
                            onToggleFavorite = { id ->
                                scope.launch {
                                    viewModel.toggleFavorite(id)
                                    viewModel.getNoteById(noteId)
                                }
                            },
                            onDeleteNote = { noteId ->
                                scope.launch {
                                    val result = viewModel.deleteNote(noteId)
                                    if (result > 0) {
                                        Toast.makeText(
                                            this@AddNotesActivity,
                                            "Note deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@AddNotesActivity,
                                            "Failed to delete note",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            onCancel = { finish() },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteScreen(
    modifier: Modifier = Modifier,
    operationString: String,
    note: NotesModel? = null,
    onSaveNote: (String, String) -> Unit,
    onToggleFavorite: (noteId: Int) -> Unit = {},
    onDeleteNote: (noteId: Int) -> Unit = {},
    onCancel: () -> Unit,
) {
    var noteTextTitle by remember { mutableStateOf("") }
    val isNotesTitleInvalid = remember { mutableStateOf(false) }

    var noteTextContent by remember { mutableStateOf("") }
    val isNotesContentInvalid = remember { mutableStateOf(false) }

    LaunchedEffect(note) {
        note?.let {
            noteTextTitle = it.title
            noteTextContent = it.content
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = operationString,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            Row {
                Icon(
                    imageVector = if (note?.isFavorite == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            note?.let { onToggleFavorite(it.id) }
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                        .clickable {
                            note?.let { onDeleteNote(it.id) }
                        },
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
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
            onValueChange = {
                noteTextContent = it
                isNotesContentInvalid.value = false
            },
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

