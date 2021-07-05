package com.fetchrewards.codingexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fetchrewards.codingexercise.model.Item
import com.fetchrewards.codingexercise.network.ItemAPI
import com.fetchrewards.codingexercise.network.NetworkConnectionInterceptor
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var itemsList = mutableListOf<Item.ItemItem>()
    private lateinit var swipeContainer : SwipeRefreshLayout
    private lateinit var apiInterface : Call<List<Item.ItemItem>>
    private lateinit var recyclerAdapter : RecyclerViewItemAdapter

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvItems = findViewById<View>(R.id.rv_homePage_itemList) as RecyclerView
        val networkConnectionInterceptor =  NetworkConnectionInterceptor(this)
        recyclerAdapter = RecyclerViewItemAdapter(itemsList)
        rvItems.adapter = recyclerAdapter
        rvItems.layoutManager = LinearLayoutManager(this)
        swipeContainer = findViewById(R.id.swipeContainer)
        apiInterface = ItemAPI.create(networkConnectionInterceptor).getItems()

        //Pull to refresh the data
        swipeContainer.setOnRefreshListener {
            fetchData()
        }
        fetchData()

        //Swipe to Delete a row of data
        val swipeHandler = object : RecyclerItemTouchHelper(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                recyclerAdapter.removeItem(viewHolder.absoluteAdapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rvItems)

    }

    //Fetch data from API and set it to RecyclerView
    @DelicateCoroutinesApi
    fun fetchData(){
        apiInterface.clone().enqueue(object : retrofit2.Callback<List<Item.ItemItem>>{
            override fun onResponse(call: Call<List<Item.ItemItem>>, response: Response<List<Item.ItemItem>>) {
                itemsList = response.body() as MutableList<Item.ItemItem>
                recyclerAdapter.clear()
                recyclerAdapter.setMovieList(itemsList)
                swipeContainer.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Item.ItemItem>>, t: Throwable?){
                if(t is NetworkConnectionInterceptor.NoConnectivityException){
                    Toast.makeText(this@MainActivity, "Please check your Connection", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(this@MainActivity, "Failed to Load Data", Toast.LENGTH_SHORT).show()
                swipeContainer.isRefreshing = false
            }
        })
    }
}