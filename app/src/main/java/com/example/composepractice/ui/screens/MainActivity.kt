package com.example.composepractice.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composepractice.data.model.NotesModel
import com.example.composepractice.ui.viewModel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val userId = viewModel.getUserIdSession().collectAsState(initial = -1)
                    Log.e("TAG ", "onCreate: " + userId.value.toString())

                    val notesList by viewModel.notes.collectAsState()
                    val favouriteNotesList by viewModel.favoriteNotes.collectAsState()
                    val scope = rememberCoroutineScope()

                    viewModel.getAllNotes()
                    viewModel.getFavoriteNotesByUser()

                    Scaffold { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            notes = notesList,
                            favoriteNotes = favouriteNotesList,
                            onEditNote = { id ->
                                val intent = Intent(this@MainActivity, AddNotesActivity::class.java)
                                intent.putExtra("NOTE_ID", id)
                                startActivity(intent)
                            },
                            onToggleFavorite = { id ->
                                scope.launch {
                                    viewModel.toggleFavorite(id)
                                }
                            },
                            onDeleteNote = { noteId ->
                                scope.launch {
                                    val result = viewModel.deleteNote(noteId)
                                    if (result > 0) {
                                        Toast.makeText(
                                            this@MainActivity, "Note deleted", Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Failed to delete note",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    notes: List<NotesModel>,
    favoriteNotes: List<NotesModel>,
    onToggleFavorite: (noteId: Int) -> Unit = {},
    onEditNote: (noteId: Int) -> Unit = {},
    onDeleteNote: (noteId: Int) -> Unit = {}
) {
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (notes.isEmpty()) {
                item {
                    Text(
                        text = "No notes yet. Tap '+ Notes' to add one.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                itemsIndexed(notes) { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(5.dp)
                                .clickable {
                                    onEditNote(item.id)
                                }) {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = formatMillis(item.createdAt),
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.RemoveRedEye,
                            contentDescription = "Edit",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    onEditNote(item.id)
                                },
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Icon(
                            imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    onToggleFavorite(item.id)
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
                                    onDeleteNote(item.id)
                                },
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    if (index < notes.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 0.dp))
                    }
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (favoriteNotes.isEmpty()) Arrangement.Center else Arrangement.spacedBy(
                8.dp
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
        ) {
            if (favoriteNotes.isEmpty()) {
                item {
                    Text(
                        text = "No favourite notes found.",
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(top = 32.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(favoriteNotes) { item ->
                    Box(
                        modifier = Modifier.clickable {
                            onEditNote(item.id)
                        }) {
                        Column(
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = formatMillis(item.createdAt),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(start = 16.dp, top = 5.dp), onClick = {
                    val intent = Intent(context, ProfileEditActivity::class.java)
                    context.startActivity(intent)
                }) {
                Icon(
                    imageVector = Icons.Default.Person3,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

//            Text(" Welcome $userName ")

            Text(
                text = "+ Notes",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth()
                    .clickable {
                        /*val newId = itemList.size + 1
                        itemList.add("Note $newId")
                        Toast.makeText(
                            context,
                            "Note $newId added",
                            Toast.LENGTH_SHORT
                        ).show()*/
                        val intent = Intent(context, AddNotesActivity::class.java)
                        context.startActivity(intent)
                    }
                    .padding(5.dp),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.End,
            )
        }

    }
}

fun formatMillis(millis: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy, hh:mm a")
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime()
        .format(formatter)
}