package com.fetchrewards.codingexercise.service

import com.fetchrewards.codingexercise.model.Item

class ItemService {

    //Filter for Blank Names and then Sort the data
  companion object{
      fun filterBlankNamesAndSort(itemList: MutableList<Item.ItemItem>): List<Item.ItemItem> {
          for (i in itemList.indices.reversed()) {
              if (itemList[i].name.isNullOrEmpty()) itemList.removeAt(i)
          }
          return itemList.sortedWith(compareBy<Item.ItemItem> { it.listId }.thenBy { it.name })
      }
  }

}

