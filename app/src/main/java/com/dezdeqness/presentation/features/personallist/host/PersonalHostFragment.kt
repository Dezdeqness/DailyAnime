package com.dezdeqness.presentation.features.personallist.host

import androidx.core.os.bundleOf
import com.dezdeqness.R
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.NavigateToPersonalList
import com.dezdeqness.presentation.event.NavigateToUnauthorized
import com.dezdeqness.presentation.features.unauthorized.host.BaseUnauthorizedHostFragment

class PersonalHostFragment : BaseUnauthorizedHostFragment() {

    override fun getNavigationGraph() = R.navigation.personal_list_nav_graph

    override fun getStartDestination(isAuthorized: Boolean) =
        if (isAuthorized) {
            R.id.personalList
        } else {
            R.id.personalUnauthorized
        }

    override fun consumeEvent(event: Event) {
        val controller = getNavController()
        val graph = controller.graph
        when (event) {
            NavigateToUnauthorized -> {
                graph.setStartDestination(R.id.personalUnauthorized)
                controller.setGraph(graph, bundleOf())
            }

            NavigateToPersonalList -> {
                graph.setStartDestination(R.id.personalList)
                controller.setGraph(graph, bundleOf())
            }

            else -> {}
        }
    }

}
