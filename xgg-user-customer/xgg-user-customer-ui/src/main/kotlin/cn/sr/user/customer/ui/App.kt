package cn.sr.user.customer.ui

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableDubbo
@EnableApolloConfig
open class App {
}

fun main(args: Array<String>) {
    SpringApplication.run(App::class.java, *args)
}