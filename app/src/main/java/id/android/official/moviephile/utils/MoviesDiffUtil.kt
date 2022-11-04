package id.android.official.moviephile.utils

import androidx.recyclerview.widget.DiffUtil
import id.android.official.moviephile.models.D

class MoviesDiffUtil(
    private val oldList: List<D>,
    private val newList: List<D>
    ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}