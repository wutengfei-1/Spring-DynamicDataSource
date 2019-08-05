package cn.sr.user.provider.core.test

import com.ctrip.framework.apollo.ConfigService
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor
import org.springframework.stereotype.Component

@Component("容器启动完成之后->运行时注入 可以成功！！！")
open class TTmnt : BeanFactoryPostProcessor {

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory?) {

        //这样会阻塞spring的启动过程
        //Thread.sleep(10000000)

        /*Thread() {
            Thread.sleep(20000)
            println("----------->>> 开始注入xxx")
            beanFactory?.registerSingleton("xxx", "pppppppppp")
        }.start()


        Thread() {
            while (true) {
                Thread.sleep(7000)
                try {
                    println(beanFactory?.getBean("xxx"))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }.start()*/

        //var cfg = ConfigService.getAppConfig()
        //cfg.getArrayProperty()

    }

}