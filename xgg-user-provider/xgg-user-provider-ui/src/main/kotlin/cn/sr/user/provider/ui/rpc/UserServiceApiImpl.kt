package cn.sr.user.provider.ui.rpc

import cn.sr.user.provider.api.UserServiceApi
import cn.sr.user.provider.api.dto.UserInfoDto
import cn.sr.user.provider.ui.App
import com.alibaba.dubbo.config.annotation.Service

@Service
class UserServiceApiImpl : UserServiceApi {

    override fun getUserInfoByUserId(id: Int): UserInfoDto {

        return UserInfoDto().apply {
            desc = "这是一个dubbo接口返回的数据"
        }
    }

    override fun addUserInfo(userInfoDto: UserInfoDto): Boolean {
        return false
    }
}