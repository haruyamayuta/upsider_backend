package jp.co.upsider.entities

import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class CustomError(
    val message:String,
    val code: Int,
    @Transient val status: HttpStatusCode = HttpStatusCode.OK
)

enum class Errors(val value: CustomError){
    //Usersテーブルにデータがない
    USER_NOT_FOUND(CustomError("user not found", 1000)),
    //clientテーブルにデータがない
    CLIENT_NOT_FOUND(CustomError("client not found", 1001))
}