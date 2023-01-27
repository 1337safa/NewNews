package com.my.projects.safin.newnews.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.my.projects.safin.newnews.R
import com.my.projects.safin.newnews.adapters.diffutil.NewNewsDiffUtil
import com.my.projects.safin.newnews.models.Article
import com.my.projects.safin.newnews.interfaces.NewsItem
import com.my.projects.safin.newnews.databinding.TemplateNewNewsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.runBlocking

class NewNewsAdapter(

    private val listener: NewsItem,
    private val fragment: Fragment

) : RecyclerView.Adapter<NewNewsAdapter.ContentHolder>() {

    var newsList = mutableListOf<Article>()

    class ContentHolder(private val view: View, private val fragment: Fragment) :
        RecyclerView.ViewHolder(view) {
        private val binding = TemplateNewNewsBinding.bind(view)
        fun bind(articleNews: Article, listener: NewsItem) {
            try {
                
                Glide.with(fragment)
                    .load(articleNews.urlToImage)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(binding.imgMainContent)

                binding.tvTitle.text = articleNews.title
                binding.tvDescription.text = articleNews.description
                binding.tvData.text = articleNews.publishedAt
                descriptionListener(articleNews, listener)

            } catch (e: Exception) {
                printExceptionInfo(e.toString())
            }
        }

        private fun descriptionListener(newNews: Article, listener: NewsItem) {
            binding.tvDescription.setOnClickListener() {
                listener.userClickOnNews(newNews)
            }
        }

        private fun printExceptionInfo(msg: String) {
            Log.d("MyLog", "Sorry, but happened a exception, info: $msg")
        }

        private fun showLog(msg: String) {
            Log.d("MyLog", "msg: $msg")
        }

        private fun showToast(view: View, msg: String) {
            Toast.makeText(view.context, "msg: $msg", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.template_new_news, parent, false)
        return NewNewsAdapter.ContentHolder(view, this.fragment)
    }

    override fun onBindViewHolder(holder: NewNewsAdapter.ContentHolder, position: Int) {
        holder.bind(newsList[position], listener)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    fun submitNewNewsList(newList: List<Article>) {

        val newNewsDiffUtil = NewNewsDiffUtil(
            oldList = this.newsList, newList = newList
        )

        val calculateDiff = DiffUtil.calculateDiff(newNewsDiffUtil)
        this.newsList.clear()
        this.newsList = newList.toMutableList()
        calculateDiff.dispatchUpdatesTo(this@NewNewsAdapter)

    }

}