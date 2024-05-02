package com.play.musicplayerzx

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val albumId: Long,
    val duration: Long,
    val size: Long,
    val path: String,
    val albumArtUri: Uri
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readParcelable(Uri::class.java.classLoader) ?: Uri.EMPTY
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(album)
        parcel.writeLong(albumId)
        parcel.writeLong(duration)
        parcel.writeLong(size)
        parcel.writeString(path)
        parcel.writeParcelable(albumArtUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AudioFile> {
        override fun createFromParcel(parcel: Parcel): AudioFile {
            return AudioFile(parcel)
        }

        override fun newArray(size: Int): Array<AudioFile?> {
            return arrayOfNulls(size)
        }
    }
}


