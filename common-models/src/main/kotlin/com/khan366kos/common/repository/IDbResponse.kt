package com.khan366kos.common.repository

interface IDbResponse<T> {
    val isSuccess: Boolean
    val result: T
}
