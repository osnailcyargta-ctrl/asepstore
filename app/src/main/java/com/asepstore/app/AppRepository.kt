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
            packageName = "com.desktoppet.app",
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
            packageName = "com.vaultgallery.app",
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
            fullDesc = "Editor pixel art lengkap yang bisa lo jalanin langsung di HP. Dibuat dari scratch dengan touch support penuh, grid canvas, dan berbagai tool menggambar.",
            size = "2.5 MB",
            version = "1.0",
            emoji = "PS",
            apkUrl = BASE + "pixel_studio.apk",
            packageName = "com.pixelstudio.app",
            features = listOf(
                "Canvas grid dengan touch support penuh",
                "Berbagai tool: pencil, fill, eraser, eyedropper",
                "Palet warna kustom",
                "Undo / redo",
                "Export ke PNG",
                "Zoom in/out dengan pinch gesture"
            )
        ),
        AppItem(
            id = 4,
            name = "Grid RPG",
            shortDesc = "RPG berbasis grid buatan sendiri",
            fullDesc = "Game RPG berbasis grid yang dibangun dari scratch. Jelajahi dungeon, lawan musuh, dan kembangkan karakter lo di dalam sistem grid yang intuitif.",
            size = "5.7 MB",
            version = "1.0",
            emoji = "GR",
            apkUrl = BASE + "grid_rpg.apk",
            packageName = "com.gridrpg.game",
            features = listOf(
                "Gameplay RPG berbasis grid",
                "Sistem pertarungan turn-based",
                "Eksplorasi dungeon",
                "Pengembangan karakter",
                "Dibangun dari scratch"
            )
        ),
        AppItem(
            id = 5,
            name = "Asep Files",
            shortDesc = "File manager buatan sendiri",
            fullDesc = "File manager ringan buatan sendiri. Jelajahi, salin, pindah, dan hapus file di HP dengan tampilan yang bersih dan minimalis.",
            size = "4.6 MB",
            version = "1.0",
            emoji = "AF",
            apkUrl = BASE + "asep_files.apk",
            packageName = "com.asepfiles.app",
            features = listOf(
                "Jelajahi semua file di HP",
                "Salin, pindah, dan hapus file",
                "Tampilan minimalis",
                "Akses penyimpanan internal dan eksternal",
                "Ringan dan cepat"
            )
        ),
        AppItem(
            id = 6,
            name = "Spun Wheel",
            shortDesc = "Spin wheel dengan warna custom",
            fullDesc = "Aplikasi spin wheel yang bisa dikustomisasi sepenuhnya. Tambah pilihan sebanyak-banyaknya, atur warna tiap slice dan teks, lalu spin dengan animasi yang smooth.",
            size = "4.4 MB",
            version = "1.0",
            emoji = "SW",
            apkUrl = BASE + "spunwheel.apk",
            packageName = "com.spunwheel.app",
            features = listOf(
                "Tambah pilihan tak terbatas",
                "Custom warna tiap slice",
                "Custom warna teks tiap pilihan",
                "Animasi spin smooth dengan decelerate",
                "Color picker dengan 20 preset + hex input",
                "Hasil spin tampil dengan animasi fade"
            )
        )
    )
}
