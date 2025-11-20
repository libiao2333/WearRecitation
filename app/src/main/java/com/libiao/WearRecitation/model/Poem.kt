package com.libiao.WearRecitation.model

import java.io.Serializable

data class Poem(
    val id: String,
    val title: String,
    val dynasty: String,
    val writer: String,
    val content: String,
    val remark: String?,
    val translation: String?,
    val shangxi: String?,
    val audioUrl: String?
) : Serializable