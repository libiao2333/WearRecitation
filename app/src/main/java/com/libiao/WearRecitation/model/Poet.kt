package com.libiao.WearRecitation.model

import java.io.Serializable

data class Poet(
    val id: String,
    val name: String,
    val headImageUrl: String?,
    val simpleIntro: String?,
    val detailIntro: String?
) : Serializable