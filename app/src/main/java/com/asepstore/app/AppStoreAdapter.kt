package com.asepstore.app

import android.content.Context
import android.content.pm.PackageManager
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

        val installed = isInstalled(app.packageName)

        if (installed) {
            holder.getBtn.text = "OPEN"
            holder.getBtn.setTextColor(0xFF4CAF50.toInt())
            holder.getBtn.setBackgroundColor(0xFF1A2A1A.toInt())
            holder.getBtn.setOnClickListener {
                val launch = context.packageManager.getLaunchIntentForPackage(app.packageName)
                if (launch != null) context.startActivity(launch)
            }
        } else {
            holder.getBtn.text = "GET"
            holder.getBtn.setTextColor(0xFF7777EE.toInt())
            holder.getBtn.setBackgroundColor(0xFF17171F.toInt())
            holder.getBtn.setOnClickListener {
                openDetail(app)
            }
        }

        holder.itemView.setOnClickListener { openDetail(app) }
    }

    private fun openDetail(app: AppItem) {
        AppDetailSheet(app, context as MainActivity).show(
            (context as MainActivity).supportFragmentManager, "detail"
        )
    }

    fun isInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun getItemCount() = apps.size
}
