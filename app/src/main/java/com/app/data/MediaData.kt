package com.app.data

import android.net.Uri

open class MediaData(
    val name : String,
    val uri : Uri
)

class AudioData(
    title : String,
    val artist : String,
    val duration : Long,
    uri : Uri
) : MediaData(title, uri)