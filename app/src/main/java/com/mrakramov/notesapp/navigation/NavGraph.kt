package com.mrakramov.notesapp.navigation

import com.mrakramov.notesapp.feature.presentation.destinations.NotesScreenDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object NavGraph {

    val root = object : NavGraphSpec {
        override val route = "rootGraph"
        override val startRoute = NotesScreenDestination routedIn this
        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            NotesScreenDestination
        ).routedIn(this).associateBy { it.route }
    }
}