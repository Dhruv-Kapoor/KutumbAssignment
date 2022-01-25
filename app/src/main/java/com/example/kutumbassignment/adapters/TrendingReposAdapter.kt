package com.example.kutumbassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.dataClasses.RepositoryHeader
import com.example.kutumbassignment.dataClasses.RepositoryListItem
import com.example.kutumbassignment.databinding.HeaderItemViewBinding
import com.example.kutumbassignment.databinding.RepoItemViewBinding

class TrendingReposAdapter(private var list: List<RepositoryListItem>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val TYPE_REPO = 0
        const val TYPE_HEADER = 1
    }

    private var expandedViewPosition = -1
    private var showHeaders = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_HEADER) {
            return HeaderViewHolder(
                HeaderItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        return RepoViewHolder(
            RepoItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RepoViewHolder->{
                holder.bind(list[position] as Repository, position==expandedViewPosition){
                    selectItemAtPosition(it)
                }
            }
            is HeaderViewHolder->{
                holder.bind(list[position] as RepositoryHeader)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return when(list[position]){
            is Repository -> TYPE_REPO
            is RepositoryHeader -> TYPE_HEADER
            else -> super.getItemViewType(position)
        }
    }

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
        if(showHeaders){
            this.list = addHeadersToList(list)
        }else{
            this.list = list
        }
        notifyDataSetChanged()
    }

    private fun showHeaders(){
        if(showHeaders) return
        showHeaders = true
        this.list = addHeadersToList(list)
        notifyDataSetChanged()
    }

    private fun removeHeaders(){
        if(!showHeaders) return
        showHeaders = false
        val tempList = ArrayList<Repository>()
        for(item in list){
            if(item is Repository){
                tempList.add(item)
            }
        }
        this.list = tempList
        notifyDataSetChanged()
    }

    fun toggleHeaders(){
        if(showHeaders) removeHeaders() else showHeaders()
    }

    fun addHeadersToList(list: List<RepositoryListItem>): List<RepositoryListItem>{
        val tempList = ArrayList<RepositoryListItem>()
        var prev: Repository? = null
        for (item in list){
            if(item is Repository && (prev==null || prev.language!=item.language)){
                tempList.add(RepositoryHeader(item.language, item.languageColor))
                prev = item
            }
            tempList.add((item))
        }
        return tempList
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

class HeaderViewHolder(private val binding: HeaderItemViewBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(header: RepositoryHeader){
        binding.header = header
        binding.executePendingBindings()
    }

}