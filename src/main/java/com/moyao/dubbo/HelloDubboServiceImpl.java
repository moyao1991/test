package com.moyao.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.moyao.dto.UserDto;
import com.moyao.service.UserService;

@Component
@DubboService
public class HelloDubboServiceImpl implements HelloDubboService{

    @Autowired
    private UserService userService;

    @Override
    public UserDto findUser(Long id){
        return userService.findUser(id);
    }


}
