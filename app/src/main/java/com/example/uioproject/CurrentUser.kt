package com.example.uioproject


object CurrentUser {
    var userId: String? = null
    var tagId: String? = null

    fun setUserInfo(userId: String, tagId: String) {
        this.userId = userId
        this.tagId = tagId
    }

    fun clearUserInfo() {
        userId = null
        tagId = null
    }
}
