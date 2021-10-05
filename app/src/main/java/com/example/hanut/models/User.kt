package com.example.hanut.models


class User {
    var id: String? = null
    var userName: String? = null
    var email: String? = null

    constructor() {}
    constructor(id: String?, userName: String?, password: String?) {
        this.id = id
        this.userName = userName
        email = password
    }
}