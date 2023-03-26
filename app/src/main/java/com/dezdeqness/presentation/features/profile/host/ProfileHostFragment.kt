package com.dezdeqness.presentation.features.profile.host

import androidx.core.os.bundleOf
import com.dezdeqness.R
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.NavigateToProfile
import com.dezdeqness.presentation.event.NavigateToUnauthorized
import com.dezdeqness.presentation.features.unauthorized.host.BaseUnauthorizedHostFragment

class ProfileHostFragment : BaseUnauthorizedHostFragment() {

    override fun getNavigationGraph() = R.navigation.profile_nav_graph

    override fun getStartDestination(isAuthorized: Boolean) =
        if (isAuthorized) {
            R.id.profile
        } else {
            R.id.profileUnauthorized
        }

    override fun getPageArguments() =
        bundleOf("titleResId" to R.string.unauthorized_title_profile)

    override fun consumeEvent(event: Event) {
        val controller = getNavController()
        val graph = controller.graph
        when (event) {
            NavigateToUnauthorized -> {
                graph.setStartDestination(R.id.profileUnauthorized)
                controller.setGraph(
                    graph,
                    getPageArguments(),
                )
            }

            NavigateToProfile -> {
                graph.setStartDestination(R.id.profile)
                controller.setGraph(graph, bundleOf())
            }

            else -> {}
        }
    }

}
