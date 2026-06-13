package com.asepstore.app

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppStoreAdapter(
    private val apps: List<AppItem>,
    private val context: Context
) : RecyclerView.Adapter<AppStoreAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val icon: TextView = view.findViewById(R.id.app_icon)
        val name: TextView = view.findViewById(R.id.app_name)
        val desc: TextView = view.findViewById(R.id.app_desc)
        val size: TextView = view.findViewById(R.id.app_size)
        val getBtn: TextView = view.findViewById(R.id.btn_get)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val app = apps[position]
        holder.icon.text = app.emoji
        holder.name.text = app.name
        holder.desc.text = app.shortDesc
        holder.size.text = app.size

        holder.getBtn.setOnClickListener {
            AppDetailSheet(app, context as MainActivity).show(
                (context as MainActivity).supportFragmentManager, "detail"
            )
        }

        holder.itemView.setOnClickListener {
            AppDetailSheet(app, context as MainActivity).show(
                (context as MainActivity).supportFragmentManager, "detail"
            )
        }
    }

    override fun getItemCount() = apps.size
}