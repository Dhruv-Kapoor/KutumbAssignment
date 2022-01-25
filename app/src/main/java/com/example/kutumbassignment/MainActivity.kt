package com.example.kutumbassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutumbassignment.adapters.TrendingReposAdapter
import com.example.kutumbassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>{
        MainActivityViewModelFactory((application as TrendingRepoApplication).repository)
    }
    private lateinit var binding: ActivityMainBinding

    private val trendingReposAdapter = TrendingReposAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initXmlViews()
        addObservers()
        addClickListeners()
    }

    private fun addClickListeners() {
        binding.btnRetry.setOnClickListener {
            viewModel.refresh()
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun addObservers() {
        viewModel.getTrendingReposLD().observe(this, {
            if (it != null) {
                trendingReposAdapter.setData(it)
            }
        })
        viewModel.getLoadingStateLD().observe(this, {
            if(it){
                if(trendingReposAdapter.itemCount == 0){
                    binding.shimmerLayout.visibility = View.VISIBLE
                    binding.shimmerLayout.startShimmer()
                }
            }else{
                binding.swipeRefreshLayout.isRefreshing = false
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
        viewModel.getErrorStateLD().observe(this, {
            if(it){
                binding.recyclerView.visibility = View.GONE
                binding.somethingWentWrong.visibility = View.VISIBLE
            }else{
                binding.recyclerView.visibility = View.VISIBLE
                binding.somethingWentWrong.visibility = View.GONE
            }
        })
    }

    private fun initXmlViews(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trendingReposAdapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}