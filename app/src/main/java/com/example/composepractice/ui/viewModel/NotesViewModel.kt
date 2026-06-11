package com.example.composepractice.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepractice.data.model.NotesModel
import com.example.composepractice.data.repository.NotesRepository
import com.example.composepractice.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepository: NotesRepository, private val sessionManager: SessionManager
) : ViewModel() {

    private val _notes = MutableStateFlow<List<NotesModel>>(emptyList())
    val notes: StateFlow<List<NotesModel>> = _notes.asStateFlow()

    private val _detailNote = MutableStateFlow<NotesModel?>(null)
    val detailNote: StateFlow<NotesModel?> = _detailNote.asStateFlow()

    private val _favoriteNotes = MutableStateFlow<List<NotesModel>>(emptyList())
    val favoriteNotes: StateFlow<List<NotesModel>> = _favoriteNotes.asStateFlow()

    fun getUserIdSession(): Flow<Int> {
        return sessionManager.userIdFlow
    }

    fun getAllNotes() {
        viewModelScope.launch {
            sessionManager.userIdFlow.collect { createdBy ->
                notesRepository.getAllNotes(createdBy).collect { notesList ->
                    _notes.value = notesList
                }
            }
        }
    }

    fun getFavoriteNotesByUser() {
        viewModelScope.launch {
            sessionManager.userIdFlow.collect { createdBy ->
                notesRepository.getFavoriteNotesByUser(createdBy).collect { favoriteNotes ->
                    _favoriteNotes.value = favoriteNotes
                }
            }
        }
    }

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            _detailNote.value = notesRepository.getNoteById(id)
        }
    }

    suspend fun insertNote(notes: NotesModel): Long =
        notesRepository.insertNote(notes)


    fun updateNote(note: NotesModel) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }


    suspend fun toggleFavorite(id: Int) {
        notesRepository.toggleFavorite(id)
    }

    suspend fun deleteNote(notesId: Int): Int {
        return notesRepository.deleteNote(notesId)
    }

    /*fun deleteAllNotes(userId: Int) {
        viewModelScope.launch {
            notesRepository.deleteAllNotes(userId)
        }
    }*/
}