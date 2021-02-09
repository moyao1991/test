package com.moyao.service;

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


    public UserDto findUser(Long id){
        User user = userMapper.findById(id);
        return Converter.MAPPER.toDto(user);
    }

}
