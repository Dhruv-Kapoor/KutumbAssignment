package com.example.kutumbassignment

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutumbassignment.adapters.TrendingReposAdapter
import com.example.kutumbassignment.adapters.TrendingReposAdapterCallbacks
import com.example.kutumbassignment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TrendingReposAdapterCallbacks {

    companion object{
        const val KEY_LAST_EXPANDED_RANK = "last_expanded_rank"
    }

    private val viewModel by viewModels<MainActivityViewModel>{
        MainActivityViewModelFactory((application as TrendingRepoApplication).repository)
    }
    private lateinit var binding: ActivityMainBinding

    private val trendingReposAdapter by lazy {
        TrendingReposAdapter(ArrayList(), this)
    }

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

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
        binding.ivMenu.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.ivMenu)
            popupMenu.inflate(R.menu.main_activity_menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.loadDummyData->{
                        viewModel.loadDummyData()
                        return@setOnMenuItemClickListener true
                    }
                    R.id.toggleHeaders->{
                        trendingReposAdapter.toggleHeaders()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {return@setOnMenuItemClickListener false}
                }
            }
            popupMenu.show()
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

        if(sharedPreferences.contains(KEY_LAST_EXPANDED_RANK)){
            trendingReposAdapter.selectItemAtRank(
                sharedPreferences.getInt(KEY_LAST_EXPANDED_RANK, 0)
            )
        }
    }

    override fun onNewItemSelected(rank: Int) {
        sharedPreferences.edit()
            .putInt(KEY_LAST_EXPANDED_RANK, rank)
            .apply()
    }
}