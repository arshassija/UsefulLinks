package org.smc.inputmethod.payboard.shops

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import org.smc.inputmethod.indic.R

abstract class ParentRVAdapter<T : ParentRVAdapter.ParentViewHolder, U : Any?>(val mContext: Context) : RecyclerView.Adapter<T>() {

    companion object {
        const val VIEW_TYPE_LOADER = 1
        const val VIEW_TYPE_DEFAULT = 2
    }

    abstract fun getDataList(): ArrayList<U>

    fun addData(temp: ArrayList<U>) {
        getDataList().addAll(temp)
    }

    @LayoutRes
    abstract fun getDefaultViewLayout(): Int

    @LayoutRes
    fun getLoadingViewLayout() = R.layout.item_progress_bar_loader

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        val view = if (viewType == VIEW_TYPE_LOADER) {
            LayoutInflater.from(mContext).inflate(getLoadingViewLayout(), parent, false)
        } else {
            LayoutInflater.from(mContext).inflate(getDefaultViewLayout(), parent, false)
        }
        return getViewHolderObject(view, viewType)
    }

    abstract fun getViewHolderObject(itemView: View, viewType: Int): T

    override fun getItemCount(): Int {
        return if (isShowingLoader) getDataList().size + 1 else getDataList().size
    }

    override fun getItemViewType(position: Int): Int {
        return if (getDataList().size == position) {
            VIEW_TYPE_LOADER
        } else {
            VIEW_TYPE_DEFAULT
        }
    }

    override fun onBindViewHolder(holder: T, position: Int) {
        if (position == getDataList().size) {
            //it's a loader
            return
        } else {
            bindData(holder, position)
        }
    }

    abstract fun bindData(holder: T, position: Int)

    private var isShowingLoader = false

    fun showLoader() {
        if (isShowingLoader.not()) {
            isShowingLoader = true
            recyclerView?.post { notifyItemInserted(getDataList().size) }
        }
    }

    fun hideLoader() {
        if (isShowingLoader) {
            isShowingLoader = false
            recyclerView?.post { notifyItemRemoved(getDataList().size) }
        }
    }

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    open class ParentViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView)

}