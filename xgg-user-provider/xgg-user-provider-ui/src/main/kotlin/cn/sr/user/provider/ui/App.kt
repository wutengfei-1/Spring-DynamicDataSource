package cn.sr.user.provider.ui

import cn.sr.user.provider.api.dto.UserInfoDto
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableApolloConfig
@EnableDubbo
open class App {

    //假数据
    companion object {
        var users: ArrayList<UserInfoDto> = arrayListOf()
    }
}

fun main(args: Array<String>) {

    App.users.addAll(listOf(UserInfoDto().apply {
        this.id = 1
        this.desc = "is 1"
    }, UserInfoDto().apply {
        this.id = 2
        this.desc = "is 2"
    }, UserInfoDto().apply {
        this.id = 3
        this.desc = "is 3"
    }))



    SpringApplication.run(App::class.java, *args)
}

