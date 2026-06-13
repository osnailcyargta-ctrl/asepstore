package com.asepstore.app

object AppRepository {
    private const val BASE = "https://github.com/osnailcyargta-ctrl/asepstore/raw/main/apks/"

    val apps = listOf(
        AppItem(
            id = 1,
            name = "Desktop Pet",
            shortDesc = "Overlay pet yang hidup di layar lo",
            fullDesc = "Pet virtual yang nongol di atas semua aplikasi lain. Bisa di-drag ke mana aja, dikasih makan, minum, atau ditidurin. Pas lagi tidur dan volume HP di atas 30%, dia bakal otomatis bangun setelah 4 detik.",
            size = "2.6 MB",
            version = "1.0",
            emoji = "DP",
            apkUrl = BASE + "desktop_pet.apk",
            packageName = "com.osnailcyargta.desktoppet",
            features = listOf(
                "Overlay di atas semua aplikasi",
                "Drag bebas ke mana saja",
                "Mode: Idle, Walk, Sleep",
                "Auto bangun kalau volume lebih dari 30%",
                "Ganti gambar pet dari galeri HP",
                "Tombol X untuk tutup langsung"
            )
        ),
        AppItem(
            id = 2,
            name = "Vault Gallery",
            shortDesc = "Galeri dengan fitur foto tersembunyi",
            fullDesc = "Galeri foto lengkap dengan fitur rahasia. Foto bisa disembunyikan pakai trik volume. Bisa dijadikan aplikasi galeri default di HP.",
            size = "4.9 MB",
            version = "1.0",
            emoji = "VG",
            apkUrl = BASE + "gallery.apk",
            packageName = "com.osnailcyargta.vaultgallery",
            features = listOf(
                "Tampilkan semua foto dari galeri HP",
                "Volume max + klik foto = foto tersembunyi",
                "Volume max + brightness min = foto muncul lagi",
                "Bisa dijadikan galeri default",
                "Kompatibel Android dan HarmonyOS",
                "Dark mode minimalis"
            )
        ),
        AppItem(
            id = 3,
            name = "Pixel Studio",
            shortDesc = "Editor pixel art dengan touch support",
            fullDesc = "Editor pixel art lengkap yang bisa lo jalanin langsung di HP. Dibuat dari scratch dengan touch support penuh, grid canvas, dan berbagai tool menggambar. Cocok buat bikin sprite game atau aset pixel art.",
            size = "2.5 MB",
            version = "1.0",
            emoji = "PS",
            apkUrl = BASE + "pixel_studio.apk",
            packageName = "com.osnailcyargta.pixelstudio",
            features = listOf(
                "Canvas grid dengan touch support penuh",
                "Berbagai tool: pencil, fill, eraser, eyedropper",
                "Palet warna kustom",
                "Undo / redo",
                "Export ke PNG",
                "Zoom in/out dengan pinch gesture"
            )
        )
    )
}
