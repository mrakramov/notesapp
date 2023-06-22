package com.mrakramov.notesapp.feature.presentation.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mrakramov.notesapp.R
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun EditNoteScreen(
    id: Int = -1,
    navController: NavController,
    viewModel: EditNoteViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is EditNoteEvents.Error -> TODO()
                EditNoteEvents.SuccessfullyAdded -> navController.navigateUp()
            }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopBar({ navController.navigateUp() }, {
            viewModel.addNote()
        })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(15.dp)
        ) {

            TextField(modifier = Modifier.fillMaxWidth(),
                value = state.value.title,
                onValueChange = viewModel::updateTitle,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color(0xFF292929),
                    placeholderColor = Color(0xFFBFBFBF)
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                placeholder = { Text(text = "Title...") })
            TextField(modifier = Modifier.fillMaxWidth(),
                value = state.value.description,
                onValueChange = viewModel::updateDesc,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color(0xFF292929),
                    placeholderColor = Color(0xFFBFBFBF)
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                textStyle = TextStyle(fontSize = 16.sp),
                placeholder = { Text(text = "Description...") })
        }
    }
}

@Composable
fun TopBar(
    onNavigateBack: () -> Unit, onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.Default.ArrowBackIos,
                contentDescription = null,
                tint = Color(0xFFE0A404),
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "Notes", color = Color(0xFFE0A404), fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = Color(0xFFE0A404),
            )
        }

    }
}
