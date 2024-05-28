package com.example.plant

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration (private val dwidth: Int) : RecyclerView.ItemDecoration() {
    //리사이클러뷰 가로로 만드는 클래스
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dwidth
        outRect.left = dwidth
        outRect.right = dwidth
    }
}