package cn.sr.user.provider.ui

import cn.sr.user.provider.api.dto.UserInfoDto
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@EnableApolloConfig
@EnableDubbo
@ComponentScan(basePackages = ["cn.sr.user.provider"])
open class App {

}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}


