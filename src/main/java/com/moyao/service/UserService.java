package com.moyao.service;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moyao.converter.Converter;
import com.moyao.dto.UserDto;
import com.moyao.entity.User;
import com.moyao.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedissonClient redissonClient;


    public UserDto findUser(Long id){
        User user = userMapper.findById(id);
        return Converter.MAPPER.toDto(user);
    }

    public UserDto findUserUseCache(Long id){
        RBucket<UserDto> rBucket = redissonClient.getBucket("user_" + id);
        UserDto userDto = rBucket.get();
        if(userDto == null){
            userDto = findUser(id);
            rBucket.set(userDto, 30L, TimeUnit.SECONDS);
        }
        return userDto;

    }

}
