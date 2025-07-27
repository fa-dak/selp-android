package com.kosa.selp.shared.navigation

enum class BottomBarRoute(val route: String) {
    Home("home"),
    Gift("ageGift"),
    Calendar("calendar"),
    MyPage("mypage");

    companion object {
        val allRoutes = entries.map { it.route }
        fun shouldShow(route: String?) = route in allRoutes
        fun indexOf(route: String?) = allRoutes.indexOf(route).takeIf { it >= 0 } ?: 0
        fun fromIndex(index: Int) = entries.getOrNull(index)?.route ?: "home"
    }
}