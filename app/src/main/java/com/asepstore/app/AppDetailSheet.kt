package com.asepstore.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AppDetailSheet(
    private val app: AppItem,
    private val ctx: Context
) : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.sheet_app_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.sheet_icon).text = app.emoji
        view.findViewById<TextView>(R.id.sheet_name).text = app.name
        view.findViewById<TextView>(R.id.sheet_size).text = "${app.size} • v${app.version}"
        view.findViewById<TextView>(R.id.sheet_desc).text = app.fullDesc

        val featContainer = view.findViewById<LinearLayout>(R.id.sheet_features)
        app.features.forEach { feat ->
            val tv = TextView(requireContext())
            tv.text = "• $feat"
            tv.setTextColor(0xFF666666.toInt())
            tv.textSize = 13f
            tv.setPadding(0, 6, 0, 0)
            featContainer.addView(tv)
        }

        val installBtn = view.findViewById<TextView>(R.id.btn_install)
        installBtn.setOnClickListener {
            installBtn.text = "Downloading..."
            installBtn.isEnabled = false
            startDownload()
            dismiss()
        }
    }

    private fun startDownload() {
        val intent = Intent(ctx, DownloadService::class.java).apply {
            action = DownloadService.ACTION_DOWNLOAD
            putExtra(DownloadService.EXTRA_URL, app.apkUrl)
            putExtra(DownloadService.EXTRA_NAME, app.name)
        }
        requireContext().startForegroundService(intent)

        Toast.makeText(ctx, "Downloading ${app.name}...", Toast.LENGTH_SHORT).show()
    }
}