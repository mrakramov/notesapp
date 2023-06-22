package com.mrakramov.notesapp.navigation

import com.mrakramov.notesapp.feature.presentation.destinations.EditNoteScreenDestination
import com.mrakramov.notesapp.feature.presentation.destinations.NotesScreenDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraph {

    val root = object : NavGraphSpec {
        override val route = "rootGraph"
        override val startRoute = NotesScreenDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            NotesScreenDestination,
            EditNoteScreenDestination
        ).routedIn(this).associateBy { it.route }
    }
}