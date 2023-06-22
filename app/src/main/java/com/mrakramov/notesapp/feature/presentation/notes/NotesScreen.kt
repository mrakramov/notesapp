@file:OptIn(ExperimentalMaterialApi::class)

package com.mrakramov.notesapp.feature.presentation.notes

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mrakramov.notesapp.R
import com.mrakramov.notesapp.feature.domain.model.NoteEntity
import com.mrakramov.notesapp.feature.presentation.destinations.EditNoteScreenDestination
import com.mrakramov.notesapp.navigation.NavGraph
import com.mrakramov.notesapp.theme.background
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RootNavGraph(start = true)
@Destination
fun NotesScreen(
    navController: NavController, notesViewModel: NotesViewModel = hiltViewModel()
) {
    val state = notesViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = background,
        topBar = { TopBarWithSearch() }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(15.dp)
        ) {
            ListNotesView(state.value, {
                navController.navigate(EditNoteScreenDestination(it) within NavGraph.root)
            }, { id ->
                notesViewModel.deleteNote(id)
            })
            EditNotes(state.value) {
                navController.navigate(EditNoteScreenDestination() within NavGraph.root)
            }
        }
    }
}

@Composable
fun ColumnScope.ListNotesView(
    value: NotesScreenState, onItemClick: (id: Int) -> Unit, onDelete: (id: Int) -> Unit
) {
    LazyColumn(modifier = Modifier.weight(1f)) {
        var lastDate: String? = null
        val notes = value.notes
        notes.forEachIndexed { index, noteEntity ->
            if (lastDate != noteEntity.date) {
                item {
                    Text(
                        text = if (noteEntity.today) "Today" else noteEntity.date,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
            item(key = noteEntity.id) {
                NoteItemView(noteEntity, notes.lastIndex != index, { onItemClick(noteEntity.id) }) {
                    onDelete(noteEntity.id)
                }
            }
            lastDate = noteEntity.date
        }
    }
}

@Composable
fun NoteItemView(
    note: NoteEntity, showDivider: Boolean, onItemClick: () -> Unit, onDelete: () -> Unit
) {

    RevealSwipe(
        modifier = Modifier.fillMaxWidth(),
        directions = setOf(RevealDirection.EndToStart),
        hiddenContentEnd = {
            Icon(
                modifier = Modifier.padding(horizontal = 25.dp),
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = Color.White
            )
        },
        onBackgroundEndClick = onDelete,
        backgroundCardEndColor = Color.Red,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(size = 0.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clickable { onItemClick() }
                .padding(horizontal = 15.dp, vertical = 6.dp)) {
                Text(text = note.title, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.size(3.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.time, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = note.content, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                if (showDivider) {
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 0.5.dp,
                        color = Color(0xFFD2D2D2)
                    )
                }
            }
        }
    }

}

@Composable
fun EditNotes(value: NotesScreenState, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = value.loading) {
                CircularProgressIndicator(
                    color = Color(0xFF8B8C8E), modifier = Modifier.size(20.dp), strokeWidth = 1.dp
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = value.notesCount,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 12.sp
            )
        }
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_note),
                contentDescription = null,
                tint = Color(0xFFE0A404),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun TopBarWithSearch() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp, vertical = 10.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Notes", color = Color.Black, fontSize = 30.sp, fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.FilterList, contentDescription = null,
                    tint = Color(0xFFE0A404),
                )
            }
        }

        SearBar()
    }

}

@Composable
fun SearBar() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                clip = true
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3E3E8))
    ) {
        Row(
            Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                tint = Color(0xFF8B8C8E)
            )
            Spacer(Modifier.size(10.dp))
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = "Search",
                onValueChange = {

                },
                textStyle = TextStyle(color = Color(0xff8B8C8E), fontSize = 14.sp),
                singleLine = true,
                cursorBrush = SolidColor(Color(0xff8B8C8E)),
            )
        }
    }
}
