package com.asepstore.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: AppStoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = AppStoreAdapter(AppRepository.apps, this)
        recycler.adapter = adapter
    }

    fun refreshList() {
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        // Refresh installed state when returning from system uninstall/install
        adapter.notifyDataSetChanged()
    }
}
