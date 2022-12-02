package com.yourparkingspace.reddit.ui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.yourparkingspace.reddit.ui.common.utils.getNavBarHeightDp
import com.yourparkingspace.reddit.ui.common.utils.getStatusBarHeightDp
import com.yourparkingspace.reddit.ui.common.utils.openUrl
import com.yourparkingspace.reddit.ui.common.utils.toast
import kotlinx.coroutines.flow.onEach

abstract class BaseComposeFragment<VM : BaseViewModel> : Fragment() {

    protected abstract val model: VM

    open val addTopPadding = false
    open val addBottomPadding = false

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeModelEvents(model)
        onViewModelReady()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val statusHeight = requireContext().getStatusBarHeightDp()
        val navHeight = requireContext().getNavBarHeightDp()

        return ComposeView(requireActivity()).apply {
            setContent {
                Column(
                    Modifier
                        .fillMaxSize()
                        .then(if (addBottomPadding) Modifier.padding(bottom = navHeight.dp) else Modifier)
                ) {
                    if (addTopPadding)
                        Spacer(modifier = Modifier.height(statusHeight.dp))
                    this@BaseComposeFragment.Content()

                }
            }
        }
    }

    @Composable
    open fun Content() {

    }

    open fun onViewModelReady() {

    }

    protected fun observeModelEvents(baseViewModel: BaseViewModel) {
        baseViewModel.eventFLow
            .onEach(::onHandleEvent)
            .observeIn(this)
    }

    @CallSuper
    protected open fun onHandleEvent(event: UiEvent) {
        when (event) {
            is Events.ShowToast -> toast(event.message)
            is Events.ShowLongToast -> toast(event.message)
            is Events.OpenUrl -> openUrl(event.url)
            is Events.ShowError -> toast("Something went wrong")
        }
    }
}

