package com.khan366kos.common.interfaces

interface IDbResponse<T> {
    val isSuccess: Boolean
    val result: T
}
