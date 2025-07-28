package com.nutrisport.home.domain

enum class CustomDrawerState {
    Opened,
    Closed
}

fun CustomDrawerState.isOpened(): Boolean {
    // retorna true, caso esteja open
    return this == CustomDrawerState.Opened
}

fun CustomDrawerState.opposite(): CustomDrawerState {
    // retorna o contrário
    return if (this == CustomDrawerState.Opened) CustomDrawerState.Closed
    else CustomDrawerState.Opened
}