package com.ypt.springboot.Mapper;

import com.ypt.springboot.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface UserMapper {
    @Select("select * from user")
    List<User> findAll();
    //查询一个User
    @Select("select * from user where id = #{id}")
    User findUserById(Integer id);
    //修改User
    @Update("update user set username = '#{username}',birthday = #{birthday},sex = #{sex},address = #{address} where id = #{id}")
    int updateUser(User user);
    //添加一个User
    @Insert({"insert into user(id,username,birthday,sex,address) values (#{id},#{username},#{birthday},#{sex},#{address})"})
    int addUser(User user);
    //删除一个User
    @Delete("delete from user where id = #{id}")
    int deleteUser(Integer id);
    @Select("select * from user where username = #{username}")
    User findByName(String username);
    @Insert("insert into photo(id,photo) values(#{id},#{photo})")
    int addPhoto(int id,byte[] photo);
}
