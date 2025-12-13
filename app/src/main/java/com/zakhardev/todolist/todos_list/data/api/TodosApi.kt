package com.zakhardev.todolist.todos_list.data.api

import com.zakhardev.todolist.todos_list.data.model.ElementRequest
import com.zakhardev.todolist.todos_list.data.model.FetchItemResponse
import com.zakhardev.todolist.todos_list.data.model.FetchListResponse
import com.zakhardev.todolist.todos_list.data.model.ItemResponse
import com.zakhardev.todolist.todos_list.data.model.PatchListRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodosApi {
    @GET("list")
    suspend fun fetchList(
        @Header("X-Generate-Fails") fails: Int? = null,
    ): FetchListResponse

    @PATCH("list")
    suspend fun patchList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body body: PatchListRequest,
        @Header("X-Generate-Fails") fails: Int? = null,
    ): FetchListResponse

    @GET("list/{id}")
    suspend fun fetchItem(@Path("id") id: String): FetchItemResponse

    @POST("list")
    suspend fun addItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body body: ElementRequest,
    ): ItemResponse

    @PUT("list/{id}")
    suspend fun updateItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
        @Body body: ElementRequest,
    ): ItemResponse

    @DELETE("list/{id}")
    suspend fun deleteItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String,
    ): ItemResponse
}
