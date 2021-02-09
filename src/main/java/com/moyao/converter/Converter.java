package com.moyao.converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.moyao.dto.UserDto;
import com.moyao.entity.User;

@Mapper
public interface Converter {

    Converter MAPPER = Mappers.getMapper( Converter.class );

    UserDto toDto(User user);
}
