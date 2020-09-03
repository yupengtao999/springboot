package com.ypt.springboot.Mapper;

import com.ypt.springboot.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface UserMapper {
    @Cacheable("findAll")
    @Select("select * from user")
    List<User> findAll();
    //查询一个User
    @Cacheable("findUserById")
    @Select("select * from user where id = #{id}")
    User findUserById(Integer id);
    //修改User
    @CacheEvict(value = {"addUser,findAll,findUserById,findByName"},allEntries = true)
    @Update("update user set username = '#{username}',birthday = #{birthday},sex = #{sex},address = #{address} where id = #{id}")
    int updateUser(User user);
    //添加一个User
    @Cacheable("addUser")
    @Insert({"insert into user(id,username,birthday,sex,address) values (#{id},#{username},#{birthday},#{sex},#{address})"})
    int addUser(User user);
    //删除一个User
    @CacheEvict(value = {"addUser,findAll,findUserById,findByName"},allEntries = true)
    @Delete("delete from user where id = #{id}")
    int deleteUser(Integer id);
    @Cacheable("findByName")
    @Select("select * from user where username = #{username}")
    User findByName(String username);
    @Insert("insert into photo(id,photo) values(#{id},#{photo})")
    int addPhoto(int id,byte[] photo);
}
