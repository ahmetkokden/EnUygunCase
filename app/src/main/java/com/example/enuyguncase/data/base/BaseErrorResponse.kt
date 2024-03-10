package com.example.enuyguncase.data.base

import java.io.Serializable

data class BaseErrorResponse(
    val code: Int,
    val message: String
) : Serializable