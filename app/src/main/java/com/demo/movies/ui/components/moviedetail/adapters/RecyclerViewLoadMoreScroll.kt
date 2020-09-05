package com.demo.movies.ui.components.moviedetail.adapters
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.movies.utils.OnLoadMoreListener

class RecyclerViewLoadMoreScroll(layoutManager: GridLayoutManager) : NestedScrollView.OnScrollChangeListener {

    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var mIsLoading: Boolean = false
    private var mVisibleThresholdDistance:Int = 300
    private var mLayoutManager: RecyclerView.LayoutManager = layoutManager

    /*Called to dismiss loadMore*/
    fun setLoaded() {
        mIsLoading = false
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    override fun onScrollChange(
        v: NestedScrollView?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        if(v?.getChildAt(v.childCount - 1) != null) {
            if ((scrollY >= (v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight - mVisibleThresholdDistance)) &&
                scrollY > oldScrollY) {
                if (!mIsLoading && mLayoutManager.itemCount > 0) {
                    mOnLoadMoreListener.onLoadMore()
                    mIsLoading = true
                }
            }
        }
    }
}
