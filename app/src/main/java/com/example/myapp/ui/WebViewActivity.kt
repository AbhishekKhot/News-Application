package com.example.myapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.R
import com.example.myapp.db.ArticleDatabase
import com.example.myapp.model.Article
import com.example.myapp.repository.NewsRepository
import com.example.myapp.util.Constans
import com.example.myapp.util.Resource
import com.example.myapp.viewmodel.NewsViewModel
import com.example.myapp.viewmodel.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.fragment_home.*

class WebViewActivity : AppCompatActivity() {
    lateinit var viewModel:NewsViewModel
    var articleUrl:String?=null
    lateinit var article:Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        supportActionBar?.hide()

         articleUrl = intent.getStringExtra("url").toString()
        webView.webViewClient = WebViewClient()
        webView.loadUrl(articleUrl.toString())


        fab.setOnClickListener {
            saveArticleToDatabase()
           viewModel.saveArticle(article)
            Toast.makeText(this,"Article saved successfully",Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveArticleToDatabase() {
        viewModel.breakingNews.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let {
                        it.articles.forEach{
                            if(it.url==articleUrl){
                                article=Article(it.id,it.author,it.content,it.description,it.publishedAt,it.source,it.title,it.url,it.urlToImage)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Toast.makeText(this,"An error occurred: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })
    }


}