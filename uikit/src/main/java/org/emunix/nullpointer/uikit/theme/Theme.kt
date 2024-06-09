package org.emunix.nullpointer.uikit.theme

enum class Theme {

    DEFAULT,

    LIGHT,

    DARK;

    companion object {

        fun convertFromString(themeName: String): Theme =
            when (themeName) {
                "light" -> LIGHT
                "dark" -> DARK
                else -> DEFAULT
            }
    }
}
