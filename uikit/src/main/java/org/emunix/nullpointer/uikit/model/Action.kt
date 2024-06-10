package org.emunix.nullpointer.uikit.model

sealed interface Action {

    data class CopyLink(val url: String) : Action

    data class ShareLink(val url: String) : Action
}