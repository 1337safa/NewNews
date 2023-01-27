package com.my.projects.safin.newnews.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.my.projects.safin.newnews.R
import com.my.projects.safin.newnews.constance.Keys
import com.my.projects.safin.newnews.models.Article
import com.my.projects.safin.newnews.databinding.FullArticleReadingBinding

class FullArticleReading() : Fragment() {
    lateinit var binding: FullArticleReadingBinding
    lateinit var argumentsAboutNews: Article

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedIntanceBundle: Bundle?
    ): View {
        binding = FullArticleReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBarArticleLoad.visibility = View.VISIBLE
        argumentsAboutNews = arguments?.get(Keys.about_news) as Article
        initArticle(argumentsAboutNews)
        tvLinkListener(argumentsAboutNews.url)
        toolbarMenuItemListener()
    }

    private fun initArticle(argumentsAboutNews: Article) {
        if (argumentsAboutNews != null) {

            Glide.with(this)
                .load(argumentsAboutNews.urlToImage)
                .centerCrop()
                .skipMemoryCache(true)
                .into(binding.imgContent)

            binding.tvWhatHappened.text = argumentsAboutNews.title
            binding.tvDescriptionArticle.text = argumentsAboutNews.description
            binding.tvContent.text = argumentsAboutNews.content
            binding.tvAuthorArticle.text = argumentsAboutNews.author
            binding.tvDataArticle.text = argumentsAboutNews.publishedAt
            binding.tvLink.text = argumentsAboutNews.url

            binding.progressBarArticleLoad.visibility = View.GONE
        }
    }

    private fun tvLinkListener(link: String) {
        binding.tvLink.setOnClickListener() {
            link.let { it ->
                openLinkInGoogle(it)
            }
        }
    }


    private fun toolbarMenuItemListener() {
        binding.fullArticleToolbar.setOnMenuItemClickListener() { itemView ->
            if (itemView.itemId == R.id.btn_full_article_toolbar_item_back) {
                removeThisFragment()
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity?.applicationContext, "Msg is $msg", Toast.LENGTH_LONG).show()
    }

    private fun removeThisFragment() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

    private fun openLinkInGoogle(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun getInstance(): Fragment {
            return FullArticleReading()
        }

        fun getInstance(aboutArticle: Bundle?): Fragment {
            val thisFragment = FullArticleReading()
            thisFragment.arguments = aboutArticle
            return thisFragment
        }
    }

}