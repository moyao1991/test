package com.moyao.dubbo;

import com.moyao.dto.UserDto;

public interface HelloDubboService {

    UserDto findUser(Long id);
}
