package paba.project.berita_online

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class berita(
    var gambar : String,
    var judul : String,
    var detail : String
) : Parcelable
