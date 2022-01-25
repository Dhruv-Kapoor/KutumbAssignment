package com.example.kutumbassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.databinding.RepoItemViewBinding

class TrendingReposAdapter(private var list: List<Repository>): RecyclerView.Adapter<RepoViewHolder>() {

    var expandedViewPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            RepoItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(list[position], position==expandedViewPosition){
            selectItemAtPosition(it)
        }
    }

    override fun getItemCount(): Int = list.size

    private fun selectItemAtPosition(position: Int){
        if(expandedViewPosition == position){
            expandedViewPosition = -1
            notifyItemChanged(position)
            return
        }
        val prev = expandedViewPosition
        expandedViewPosition = position
        if(prev>=0){
            notifyItemChanged(prev)
        }
        notifyItemChanged(position)
    }

    fun setData(list: List<Repository>){
        this.list = list
        notifyDataSetChanged()
    }
}

class RepoViewHolder(private val binding: RepoItemViewBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(repository: Repository, isExpanded: Boolean, onClick: (Int)->Unit) {
        binding.repository = repository
        binding.isExpanded = isExpanded
        binding.root.setOnClickListener {
            onClick(adapterPosition)
        }
        binding.executePendingBindings()
    }

}