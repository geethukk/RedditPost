package com.yourparkingspace.reddit.ui.common.composables

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yourparkingspace.reddit.ui.theme.isDark

@Composable
fun PageView(
    tabTitles: List<String>,
    pageAdapter: FragmentStateAdapter,
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(modifier.background(androidx.compose.ui.graphics.Color.Yellow)) {
        AndroidView(
            modifier = Modifier
                .fillMaxHeight()
                .background(androidx.compose.ui.graphics.Color.Green),
            update = {
                it.findViewWithTag<TabLayout>("tabs")
                    .setBackgroundColor(if (isDark.value) Color.parseColor("#121212") else Color.WHITE)

            },
            factory = { ctx ->
                val tabs = TabLayout(context).apply {
                    tag = "tabs"
                    layoutParams = ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    isTabIndicatorFullWidth = false
                    tabGravity = TabLayout.GRAVITY_CENTER
                    tabMode = TabLayout.MODE_FIXED
                }

                val pager = ViewPager2(context).apply {
                    adapter = pageAdapter
                    setPadding(0, 0, 0, 100)
                }

                val layout = LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    addView(tabs)
                    addView(pager)
                }

                TabLayoutMediator(tabs, pager) { tab, position ->
                    tab.text = tabTitles[position]
                }.attach()

                layout
            })
    }
}