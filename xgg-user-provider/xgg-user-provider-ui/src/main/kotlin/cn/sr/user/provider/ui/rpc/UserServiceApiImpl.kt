package cn.sr.user.provider.ui.rpc

import cn.sr.user.provider.api.UserServiceApi
import cn.sr.user.provider.api.dto.UserInfoDto
import cn.sr.user.provider.ui.App
import com.alibaba.dubbo.config.annotation.Service
import org.springframework.stereotype.Component

@Service
class UserServiceApiImpl : UserServiceApi {

    override fun getUserInfoByUserId(id: Int): UserInfoDto {
        App.users.forEach {
            if (it.id == id) {
                return it
            }
        }
        return UserInfoDto()
    }

    override fun addUserInfo(userInfoDto: UserInfoDto) {
        if (userInfoDto.id != null && App.users.find { it.id == userInfoDto.id } == null) {
            App.users.add(userInfoDto)
        }
    }
}