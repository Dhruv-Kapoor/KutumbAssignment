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
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout.stopShimmer()
                binding.recyclerView.visibility = View.VISIBLE
            }
        })

    }

    private fun initXmlViews(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = trendingReposAdapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}