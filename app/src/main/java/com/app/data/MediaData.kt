package com.app.data

import android.net.Uri

abstract class MediaData(
    val name : String
)

class AudioData(
    val title : String,
    val artist : String,
    val duration : Long,
    val uri : Uri
)