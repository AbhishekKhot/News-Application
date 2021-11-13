package com.example.myapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.R
import com.example.myapp.db.ArticleDatabase
import com.example.myapp.model.Article
import com.example.myapp.repository.NewsRepository
import com.example.myapp.viewmodel.NewsViewModel
import com.example.myapp.viewmodel.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_webview.*

class WebViewActivity : AppCompatActivity() {
    lateinit var viewModel:NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        supportActionBar?.hide()

        val articleUrl = intent.getStringExtra("url").toString()
        webView.webViewClient = WebViewClient()
        webView.loadUrl(articleUrl)

        fab.setOnClickListener {
           viewModel.saveArticle(article = Article(null,null,null,null,null,null,null, articleUrl,null))
            Snackbar.make(View(this), "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}