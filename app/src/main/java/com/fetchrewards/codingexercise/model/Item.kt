package com.fetchrewards.codingexercise.model

//Data Model
class Item : ArrayList<Item.ItemItem>(){
    data class ItemItem(
        val id: Int,
        val listId: Int? ,
        val name: String?
    )
}