package com.my.projects.safin.newnews.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.my.projects.safin.newnews.models.Article

class NewNewsDiffUtil(
    private val oldList: List<Article>,
    private val newList: List<Article>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return this.oldList.size
    }

    override fun getNewListSize(): Int {
       return this.newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return this.oldList[oldItemPosition].content == this.newList[newItemPosition].content
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return this.oldList[oldItemPosition].equals(this.newList[newItemPosition])
    }

}