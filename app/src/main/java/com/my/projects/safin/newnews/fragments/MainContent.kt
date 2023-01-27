package com.my.projects.safin.newnews.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.projects.safin.newnews.R
import com.my.projects.safin.newnews.adapters.NewNewsAdapter
import com.my.projects.safin.newnews.constance.Keys
import com.my.projects.safin.newnews.constance.Logs
import com.my.projects.safin.newnews.constance.ServerData
import com.my.projects.safin.newnews.models.AboutNews
import com.my.projects.safin.newnews.models.Article
import com.my.projects.safin.newnews.databinding.MainContentBinding
import com.my.projects.safin.newnews.interfaces.NewsItem
import com.my.projects.safin.newnews.date_about_phone.DateTime
import com.my.projects.safin.newnews.internet.ApiService
import com.my.projects.safin.newnews.internet.ServerModul
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*

const val DEFAULT_SORT_BY = "popularity"
const val DEFAULT_NEWS_TOPIC = " Game"

class MainContent() : Fragment(), NewsItem {
    private lateinit var binding: MainContentBinding
    private lateinit var rcAdapter: NewNewsAdapter
    private var retrofit = ServerModul().getRetrofit()
    private var dateTime = DateTime()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceBundle: Bundle?
    ): View {
        binding = MainContentBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            ifInternetExistGetDataFromServerAndListenSearchView()
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        mainProgressBarListener()
    }

    private fun ifInternetExistGetDataFromServerAndListenSearchView() {

        val internetState: Boolean = isInternetEnable()
        if (internetState) {
            val newNews = getResponseOrFailureFromServer(
                retrofit, DEFAULT_NEWS_TOPIC, dateTime.getCurrentDate(),
                DEFAULT_SORT_BY, ServerData.api_key
            )
            processResponseFromServer(newNews)
            mainSearchViewListener()
        }

    }

    private fun mainProgressBarListener() {
        binding.mainProgressBar.setOnClickListener() {
            ifInternetExistGetDataFromServerAndListenSearchView()
        }
    }

    private fun isInternetEnable(): Boolean {
        val connectivityManager =
            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null
    }

    private fun init() {
        rcAdapter = NewNewsAdapter(this, this)
        binding.rcMainContent.layoutManager = LinearLayoutManager(activity?.applicationContext)
        binding.rcMainContent.adapter = rcAdapter
        binding.edtvMainSearchNews.setHint(
            "${resources.getString(R.string.default_name_for_news) + DEFAULT_NEWS_TOPIC}"
        )
        binding.mainProgressBar.visibility = View.VISIBLE
    }

    private fun getResponseOrFailureFromServer(
        retrofit: Retrofit, newsTopic: String, data: String, sortBy: String, apiKey: String
    ): Call<AboutNews> {
        val news = retrofit.create(ApiService::class.java).getNewsByParametrs(
            newsTopic,
            data, sortBy, apiKey
        )
        return news
    }

    private fun processResponseFromServer(news: Call<AboutNews>) {
        lifecycleScope.launch(Dispatchers.IO) {
            news.enqueue(object : Callback<AboutNews> {
                override fun onResponse(call: Call<AboutNews>, response: Response<AboutNews>) {
                    parseDataFromServer(response)
                }

                override fun onFailure(call: Call<AboutNews>, t: Throwable) {
                    Log.d(Logs.tag, "Failure is $t")
                }
            })
        }
    }

    private fun parseDataFromServer(response: Response<AboutNews>) {
        try {
            if (response.body()?.articles?.isEmpty() == true) {
                showToast(resources.getString(R.string.not_data))
            } else {
                val body = response.body()!!
                rcAdapter.submitNewNewsList(body.articles)
                binding.mainProgressBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            printExceptionInfo(e.toString())
        }
    }

    private fun mainSearchViewListener() {
        binding.edtvMainSearchNews.setOnEditorActionListener(object: TextView.OnEditorActionListener {

            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                var thisIsWhatYouNeed = false

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val userText = binding.edtvMainSearchNews.text.toString()
                    val news = getResponseOrFailureFromServer(
                        retrofit, userText, dateTime.getCurrentDate(),
                        DEFAULT_SORT_BY, ServerData.api_key
                    )
                    processResponseFromServer(news)
                    thisIsWhatYouNeed = true
                }

                return thisIsWhatYouNeed
            }

        })
    }

    private fun printExceptionInfo(msg: String) {
        Log.d("MyLog", "Sorry, but happened a exception, info: $msg")
    }

    private fun showToast(msg: String) {
        Toast.makeText(activity?.applicationContext, "$msg", Toast.LENGTH_LONG).show()
    }

    private fun openFragment(placeHolder: Int, fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()?.add(placeHolder, fragment)?.commit()
    }

    override fun userClickOnNews(news: Article) {
        val aboutNewsArguments = Bundle()
        aboutNewsArguments.putSerializable(Keys.about_news, news)
        openFragment(
            R.id.main_frame_layout, FullArticleReading
                .getInstance(aboutArticle = aboutNewsArguments)
        )
    }

    companion object {
        @JvmStatic
        fun getInstance(): Fragment {
            return MainContent()
        }
    }

}