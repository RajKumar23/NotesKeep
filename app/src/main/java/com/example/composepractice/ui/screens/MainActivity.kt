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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(userId.value) {
                        if (userId.value != -1) {
                            viewModel.getAllNotes(userId.value)
                        }
                    }

                    Scaffold { innerPadding ->
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            notes = notesList,
                            onDeleteNote = { noteId ->
                                scope.launch {
                                    val result = viewModel.deleteNote(noteId)
                                    if (result > 0) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Note deleted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Failed to delete note",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            onEditNote = { note ->
                                val intent = Intent(this@MainActivity, AddNotesActivity::class.java)
                                intent.putExtra("NOTE_DATA", note)
                                startActivity(intent)
                            },
                            onToggleFavorite = { note ->
                                viewModel.toggleFavorite(note)
                            }
                        )
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
    onDeleteNote: (noteId: Int) -> Unit = {},
    onEditNote: (NotesModel) -> Unit = {},
    onToggleFavorite: (NotesModel) -> Unit = {}
) {
    val context = LocalContext.current
    val favoriteList = remember {
        mutableStateListOf<String>()
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (favoriteList.isEmpty()) Arrangement.Center else Arrangement.spacedBy(
                8.dp
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            if (favoriteList.isEmpty()) {
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
                items(favoriteList) { item ->
                    Box(
                        modifier = Modifier.clickable {
                            Toast.makeText(
                                context, "Favorites item clicked", Toast.LENGTH_SHORT
                            ).show()
                        }) {
                        Text(
                            text = item.split(" ").last(),
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            .padding(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Created Date with Time " + item.createdAt,
                                modifier = Modifier.padding(end = 8.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    onEditNote(item)
                                },
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    onToggleFavorite(item)
                                },
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    onDeleteNote(item.id)
                                },
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    if (index < notes.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp), onClick = {
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
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.End,
            )
        }

    }
}