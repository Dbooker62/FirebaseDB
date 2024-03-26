package com.example.firebasedb

data class Employee(
    val id: String,
    val name: String,
    val position: String,
    val email: String
) {

    constructor() : this("", "", "", "")
}

