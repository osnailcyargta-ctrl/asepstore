package com.asepstore.app

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AppDetailSheet(
    private val app: AppItem,
    private val ctx: MainActivity
) : BottomSheetDialogFragment() {

    private lateinit var installBtn: TextView
    private lateinit var openBtn: TextView
    private lateinit var uninstallBtn: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView
    private lateinit var progressContainer: LinearLayout
    private lateinit var actionContainer: LinearLayout

    private val progressReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getIntExtra(DownloadService.EXTRA_APP_ID, -1) != app.id) return
            when (intent.action) {
                DownloadService.BROADCAST_PROGRESS -> {
                    val p = intent.getIntExtra(DownloadService.EXTRA_PROGRESS, 0)
                    val dl = intent.getLongExtra(DownloadService.EXTRA_DOWNLOADED, 0)
                    val total = intent.getLongExtra(DownloadService.EXTRA_TOTAL, 1)
                    progressBar.progress = p
                    progressText.text = "$p%  •  ${formatSize(dl)} / ${formatSize(total)}"
                }
                DownloadService.BROADCAST_DONE -> {
                    progressBar.progress = 100
                    progressText.text = "Installing..."
                    installBtn.text = "Installing..."
                }
                DownloadService.BROADCAST_ERROR -> {
                    progressContainer.visibility = View.GONE
                    installBtn.text = "Retry"
                    installBtn.isEnabled = true
                    Toast.makeText(ctx, "Download failed", Toast.LENGTH_SHORT).show()
                }
                DownloadService.BROADCAST_INSTALL_DONE -> {
                    if (intent.getIntExtra(DownloadService.EXTRA_APP_ID, -1) != app.id) return
                    progressContainer.visibility = View.GONE
                    isCancelable = true
                    showInstalledState()
                    // Refresh list
                    ctx.refreshList()
                }
            }
        }
    }

    private fun formatSize(bytes: Long): String {
        return if (bytes < 1024 * 1024) "${bytes / 1024} KB"
        else String.format("%.1f MB", bytes / (1024f * 1024f))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.sheet_app_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        installBtn = view.findViewById(R.id.btn_install)
        openBtn = view.findViewById(R.id.btn_open)
        uninstallBtn = view.findViewById(R.id.btn_uninstall)
        progressBar = view.findViewById(R.id.progress_bar)
        progressText = view.findViewById(R.id.progress_text)
        progressContainer = view.findViewById(R.id.progress_container)
        actionContainer = view.findViewById(R.id.action_container)

        view.findViewById<TextView>(R.id.sheet_icon).text = app.emoji
        view.findViewById<TextView>(R.id.sheet_name).text = app.name
        view.findViewById<TextView>(R.id.sheet_size).text = "${app.size} • v${app.version}"
        view.findViewById<TextView>(R.id.sheet_desc).text = app.fullDesc

        val featContainer = view.findViewById<LinearLayout>(R.id.sheet_features)
        app.features.forEach { feat ->
            val tv = TextView(requireContext())
            tv.text = "  $feat"
            tv.setTextColor(0xFF555555.toInt())
            tv.textSize = 13f
            tv.setPadding(0, 5, 0, 0)
            featContainer.addView(tv)
        }

        // Detect installed state
        val installed = isInstalled()
        if (installed) showInstalledState() else showInstallState()

        // Register broadcast receiver
        val filter = IntentFilter().apply {
            addAction(DownloadService.BROADCAST_PROGRESS)
            addAction(DownloadService.BROADCAST_DONE)
            addAction(DownloadService.BROADCAST_ERROR)
            addAction(DownloadService.BROADCAST_INSTALL_DONE)
        }
        requireContext().registerReceiver(progressReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
    }

    private fun isInstalled(): Boolean {
        return try {
            requireContext().packageManager.getPackageInfo(app.packageName, 0)
            true
        } catch (e: Exception) { false }
    }

    private fun showInstallState() {
        installBtn.visibility = View.VISIBLE
        openBtn.visibility = View.GONE
        uninstallBtn.visibility = View.GONE
        installBtn.setOnClickListener { startDownload() }
    }

    private fun showInstalledState() {
        installBtn.visibility = View.GONE
        openBtn.visibility = View.VISIBLE
        uninstallBtn.visibility = View.VISIBLE

        openBtn.setOnClickListener {
            val launch = requireContext().packageManager.getLaunchIntentForPackage(app.packageName)
            if (launch != null) startActivity(launch)
        }

        uninstallBtn.setOnClickListener {
            val uri = Uri.parse("package:${app.packageName}")
            val intent = Intent(Intent.ACTION_DELETE, uri)
            startActivity(intent)
        }
    }

    private fun startDownload() {
        installBtn.text = "Connecting..."
        installBtn.isEnabled = false
        progressContainer.visibility = View.VISIBLE
        progressBar.progress = 0
        progressText.text = "0%"
        isCancelable = false

        val intent = Intent(ctx, DownloadService::class.java).apply {
            action = DownloadService.ACTION_DOWNLOAD
            putExtra(DownloadService.EXTRA_URL, app.apkUrl)
            putExtra(DownloadService.EXTRA_NAME, app.name)
            putExtra(DownloadService.EXTRA_APP_ID, app.id)
        }
        requireContext().startForegroundService(intent)
    }

    override fun onResume() {
        super.onResume()
        // Recheck installed state when returning from system installer
        if (isInstalled()) {
            showInstalledState()
            ctx.refreshList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { requireContext().unregisterReceiver(progressReceiver) } catch (e: Exception) {}
    }
}
