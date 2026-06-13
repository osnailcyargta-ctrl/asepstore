package com.asepstore.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class DownloadService : Service() {

    companion object {
        const val ACTION_DOWNLOAD = "download"
        const val EXTRA_URL = "url"
        const val EXTRA_NAME = "name"
        const val CHANNEL_ID = "download_channel"
        const val NOTIF_ID = 42
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY
        val name = intent.getStringExtra(EXTRA_NAME) ?: "app"
        createChannel()
        startForeground(NOTIF_ID, buildNotif(name, 0))
        Thread { downloadAndInstall(url, name) }.start()
        return START_NOT_STICKY
    }

    private fun downloadAndInstall(urlStr: String, appName: String) {
        try {
            val cacheDir = File(cacheDir, "apk_cache").also { it.mkdirs() }
            val apkFile = File(cacheDir, "${appName.replace(" ", "_")}.apk")

            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.connect()
            val total = conn.contentLength
            val input = BufferedInputStream(conn.inputStream)
            val output = FileOutputStream(apkFile)

            val buf = ByteArray(8192)
            var downloaded = 0
            var read: Int
            while (input.read(buf).also { read = it } != -1) {
                output.write(buf, 0, read)
                downloaded += read
                val progress = if (total > 0) (downloaded * 100 / total) else 0
                updateNotif(appName, progress)
            }
            output.close()
            input.close()

            handler.post { install(apkFile, appName) }
        } catch (e: Exception) {
            handler.post {
                showDoneNotif(appName, false)
                stopSelf()
            }
        }
    }

    private fun install(file: File, appName: String) {
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
        showDoneNotif(appName, true)
        stopSelf()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_ID, "Downloads", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }

    private fun buildNotif(name: String, progress: Int): Notification {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading $name")
            .setContentText(if (progress < 100) "$progress%" else "Installing...")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
        if (progress in 1..99) {
            builder.setProgress(100, progress, false)
        } else if (progress == 0) {
            builder.setProgress(100, 0, true)
        }
        return builder.build()
    }

    private fun updateNotif(name: String, progress: Int) {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(NOTIF_ID, buildNotif(name, progress))
    }

    private fun showDoneNotif(name: String, success: Boolean) {
        val nm = getSystemService(NotificationManager::class.java)
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(if (success) "$name siap diinstall" else "Download gagal")
            .setContentText(if (success) "Tap untuk install" else "Coba lagi")
            .setSmallIcon(if (success) android.R.drawable.stat_sys_download_done else android.R.drawable.stat_notify_error)
            .setAutoCancel(true)
            .build()
        nm.notify(NOTIF_ID + 1, notif)
    }
}