package com.example.kutumbassignment.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.kutumbassignment.R
import com.example.kutumbassignment.dataClasses.Favorites
import com.example.kutumbassignment.dataClasses.Repository
import com.example.kutumbassignment.dataClasses.RepositoryHeader
import com.example.kutumbassignment.dataClasses.RepositoryListItem
import com.example.kutumbassignment.databinding.FavoriteItemViewBinding
import com.example.kutumbassignment.databinding.HeaderItemViewBinding
import com.example.kutumbassignment.databinding.RepoItemViewBinding

class TrendingReposAdapter(
    private var list: List<RepositoryListItem>,
    private val callbacks: TrendingReposAdapterCallbacks
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val TYPE_REPO = 0
        const val TYPE_HEADER = 1
        const val TYPE_FAVORITE = 2
    }

    private var expandedViewRank = -1
    private var expandedViewPosition = -1
    private var showHeaders = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == TYPE_HEADER) {
            return HeaderViewHolder(
                HeaderItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }else if(viewType == TYPE_FAVORITE){
            return FavoriteViewHolder(
                FavoriteItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                callbacks
            )
        }
        return RepoViewHolder(
            RepoItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            callbacks
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is RepoViewHolder->{
                val repo = list[position] as Repository
                holder.bind(repo, repo.rank==expandedViewRank){ rank, pos->
                    selectItemAtPosition(rank, pos)
                }
            }
            is FavoriteViewHolder->{
                val repo = list[position] as Favorites
                holder.bind(repo, repo.rank==expandedViewRank){ rank, pos->
                    selectItemAtPosition(rank, pos)
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
            is Favorites -> TYPE_FAVORITE
            else -> super.getItemViewType(position)
        }
    }

    fun selectItemAtRank(rank: Int){
        var position = -1
        for(i in list.indices){
            val item = list[i]
            if(item is Repository && item.rank == rank){
                position = i
            }
        }
        selectItemAtPosition(rank, position)
    }

    fun selectItemAtPosition(rank: Int, position: Int){
        if(position>=list.size || position<0){
            expandedViewRank = rank
            expandedViewPosition = -1
            return
        }
        if(expandedViewRank == rank && expandedViewPosition!=-1){
            expandedViewRank = -1
            expandedViewPosition = -1
            notifyItemChanged(position)
            callbacks.onNewItemSelected(rank)
            return
        }
        val prev = expandedViewPosition
        expandedViewPosition = position
        expandedViewRank = rank
        if(prev>=0){
            notifyItemChanged(prev)
        }
        notifyItemChanged(position)
        callbacks.onNewItemSelected(rank)
    }

    fun setData(list: List<Repository>){
        if(showHeaders){
            this.list = addHeadersToList(list)
        }else{
            this.list = list
        }
        notifyDataSetChanged()
        selectItemAtRank(expandedViewRank)
    }

    fun setFavoritesData(list: List<Favorites>){
        showHeaders=false
        this.list = list
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
        selectItemAtPosition(-1,-1)
        if(showHeaders) removeHeaders() else showHeaders()
        callbacks.onHeadersToggled(showHeaders)
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

    fun setShowHeaders(boolean: Boolean) {
        if(showHeaders!=boolean){
            toggleHeaders()
        }
    }
}

class RepoViewHolder(
    private val binding: RepoItemViewBinding,
    private val callbacks: TrendingReposAdapterCallbacks
): RecyclerView.ViewHolder(binding.root){

    fun bind(repository: Repository, isExpanded: Boolean, onClick: (Int, Int)->Unit) {
        binding.repository = repository
        binding.isExpanded = isExpanded
        binding.root.setOnClickListener {
            onClick(repository.rank?:0, adapterPosition)
        }
        binding.ivMenu.setOnClickListener {
            val popupMenu = PopupMenu(binding.root.context, binding.ivMenu)
            popupMenu.inflate(R.menu.repo_item_menu)
            popupMenu.menu.removeItem(R.id.removeFavorite)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.addFavorite->{
                        callbacks.addFavorite(repository)
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }
        binding.executePendingBindings()
    }

}

class FavoriteViewHolder(
    private val binding: FavoriteItemViewBinding,
    private val callbacks: TrendingReposAdapterCallbacks
): RecyclerView.ViewHolder(binding.root){

    fun bind(repository: Favorites, isExpanded: Boolean, onClick: (Int, Int)->Unit) {
        binding.repository = repository
        binding.isExpanded = isExpanded
        binding.root.setOnClickListener {
            onClick(repository.rank?:0, adapterPosition)
        }
        binding.ivMenu.setOnClickListener {
            val popupMenu = PopupMenu(binding.root.context, binding.ivMenu)
            popupMenu.inflate(R.menu.repo_item_menu)
            popupMenu.menu.removeItem(R.id.addFavorite)

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.removeFavorite->{
                        callbacks.removeFavorite(repository)
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
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

interface TrendingReposAdapterCallbacks{
    fun onNewItemSelected(position: Int)
    fun onHeadersToggled(value: Boolean)
    fun addFavorite(repository: Repository)
    fun removeFavorite(repository: Favorites)
}