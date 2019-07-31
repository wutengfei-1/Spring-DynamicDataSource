package cn.sr.user.provider.api

import cn.sr.user.provider.api.dto.UserInfoDto

interface UserServiceApi {
    fun getUserInfoByUserId(id: Int): UserInfoDto

    fun addUserInfo(userInfoDto: UserInfoDto)
}