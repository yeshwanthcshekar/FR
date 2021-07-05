package com.fetchrewards.codingexercise

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fetchrewards.codingexercise.model.Item
import com.fetchrewards.codingexercise.service.ItemService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecyclerViewItemAdapter(private var itemsList: MutableList<Item.ItemItem> ) : RecyclerView.Adapter<RecyclerViewItemAdapter.ItemViewHolder>(){

    inner class ItemViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val nameTextView: TextView = itemView.findViewById(R.id.tv_nameData)
        val listTextView: TextView = itemView.findViewById(R.id.tv_listIdData)
        val idTextView: TextView = itemView.findViewById(R.id.tv_idData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewItemAdapter.ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewItemAdapter.ItemViewHolder, position: Int) {
        val item : Item.ItemItem = itemsList[position]
        holder.nameTextView.text = item.name
        holder.listTextView.text = item.listId.toString()
        holder.idTextView.text = item.id.toString()
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    @DelicateCoroutinesApi
    fun setMovieList(items: MutableList<Item.ItemItem>){
        this.itemsList = items
        notifyDataSetChanged()
        GlobalScope.launch { (Dispatchers.Default)
            launch { Dispatchers.Main
                itemsList = ItemService.filterBlankNamesAndSort(items).toMutableList()
            }
        }
        Handler().post { notifyDataSetChanged() }
    }

     fun removeItem(position: Int){
        itemsList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clear(){
        itemsList.clear()
        notifyDataSetChanged()
    }

    fun addAll(items: MutableList<Item.ItemItem>){
        itemsList.addAll(items)
        notifyDataSetChanged()
    }
}