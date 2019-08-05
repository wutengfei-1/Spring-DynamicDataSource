package cn.sr.user.provider.core.mapper

import cn.sr.user.provider.core.po.UserPo
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

interface UserMapper {

    @Select("select * from t_user where id = #{id}")
    fun selectUserById(@Param("id") id: Int): UserPo?

    @Insert("insert into t_user(id,description) values(#{id},#{description})")
    fun insertUser(userPo: UserPo): Int

}