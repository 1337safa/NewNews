package com.my.projects.safin.newnews.interfaces

import com.my.projects.safin.newnews.models.Article

interface NewsItem {
    fun userClickOnNews(news: Article)
}