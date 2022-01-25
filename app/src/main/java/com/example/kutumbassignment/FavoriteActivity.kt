package com.example.kutumbassignment

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutumbassignment.adapters.TrendingReposAdapter
import com.example.kutumbassignment.adapters.TrendingReposAdapterCallbacks
import com.example.kutumbassignment.dataClasses.Favorites
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity(), TrendingReposAdapterCallbacks {

    private val viewModel by viewModels<FavoriteActivityViewModel> {
        FavoriteActivityViewModelFactory((application as TrendingRepoApplication).repository)
    }
    private lateinit var binding: ActivityFavoriteBinding

    private val trendingReposAdapter by lazy {
        TrendingReposAdapter(ArrayList(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite)

        initXmlViews()
        addObservers()

    }

    private fun initXmlViews() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trendingReposAdapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun addObservers() {
        viewModel.favorites.observe(this, {
            if (!it.isNullOrEmpty()) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.nothingToShow.visibility = View.GONE
                trendingReposAdapter.setFavoritesData(it)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.nothingToShow.visibility = View.VISIBLE
            }
        })
    }

    override fun onNewItemSelected(position: Int) {

    }

    override fun onHeadersToggled(value: Boolean) {
    }

    override fun addFavorite(repository: Repository) {
    }

    override fun removeFavorite(repository: Favorites) {
        viewModel.removeFavorite(repository)
    }
}