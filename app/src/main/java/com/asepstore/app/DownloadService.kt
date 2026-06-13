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
        const val EXTRA_APP_ID = "app_id"
        const val CHANNEL_ID = "dl_ch"
        const val NOTIF_ID = 77

        const val BROADCAST_PROGRESS = "com.asepstore.PROGRESS"
        const val BROADCAST_DONE = "com.asepstore.DONE"
        const val BROADCAST_ERROR = "com.asepstore.ERROR"
        const val BROADCAST_INSTALL_DONE = "com.asepstore.INSTALL_DONE"
        const val EXTRA_PROGRESS = "progress"
        const val EXTRA_DOWNLOADED = "downloaded"
        const val EXTRA_TOTAL = "total"
    }

    private val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val url = intent?.getStringExtra(EXTRA_URL) ?: return START_NOT_STICKY
        val name = intent.getStringExtra(EXTRA_NAME) ?: "app"
        val appId = intent.getIntExtra(EXTRA_APP_ID, 0)
        createChannel()
        startForeground(NOTIF_ID, buildNotif(name, 0, true))
        Thread { downloadAndInstall(url, name, appId) }.start()
        return START_NOT_STICKY
    }

    private fun downloadAndInstall(urlStr: String, appName: String, appId: Int) {
        try {
            val cacheDir = File(cacheDir, "apk_cache").also { it.mkdirs() }
            val apkFile = File(cacheDir, "${appName.replace(" ", "_")}.apk")

            var urlStr2 = urlStr
            var conn: HttpURLConnection
            // Follow redirects manually (GitHub raw URLs redirect)
            var redirects = 0
            while (true) {
                val url = URL(urlStr2)
                conn = url.openConnection() as HttpURLConnection
                conn.instanceFollowRedirects = false
                conn.connect()
                val code = conn.responseCode
                if (code in 300..399 && redirects < 5) {
                    urlStr2 = conn.getHeaderField("Location") ?: break
                    conn.disconnect()
                    redirects++
                } else break
            }

            val total = conn.contentLength.toLong()
            val input = BufferedInputStream(conn.inputStream)
            val output = FileOutputStream(apkFile)

            val buf = ByteArray(8192)
            var downloaded = 0L
            var read: Int
            var lastBroadcast = -1

            while (input.read(buf).also { read = it } != -1) {
                output.write(buf, 0, read)
                downloaded += read
                val progress = if (total > 0) (downloaded * 100 / total).toInt() else 0

                if (progress != lastBroadcast) {
                    lastBroadcast = progress
                    val i = Intent(BROADCAST_PROGRESS).apply {
                        putExtra(EXTRA_APP_ID, appId)
                        putExtra(EXTRA_PROGRESS, progress)
                        putExtra(EXTRA_DOWNLOADED, downloaded)
                        putExtra(EXTRA_TOTAL, total)
                    }
                    sendBroadcast(i)
                    updateNotif(appName, progress, false)
                }
            }
            output.close()
            input.close()

            sendBroadcast(Intent(BROADCAST_DONE).apply { putExtra(EXTRA_APP_ID, appId) })
            handler.post { install(apkFile, appName, appId) }

        } catch (e: Exception) {
            sendBroadcast(Intent(BROADCAST_ERROR).apply {
                putExtra(EXTRA_APP_ID, appId)
                putExtra("error", e.message)
            })
            stopSelf()
        }
    }

    private fun install(file: File, appName: String, appId: Int) {
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/vnd.android.package-archive")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(intent)
        stopSelf()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(CHANNEL_ID, "Downloads", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }

    private fun buildNotif(name: String, progress: Int, indeterminate: Boolean): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading $name")
            .setContentText(if (indeterminate) "Connecting..." else "$progress%")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setProgress(100, progress, indeterminate)
            .setOngoing(true)
            .build()
    }

    private fun updateNotif(name: String, progress: Int, indeterminate: Boolean) {
        getSystemService(NotificationManager::class.java).notify(NOTIF_ID, buildNotif(name, progress, indeterminate))
    }
}
