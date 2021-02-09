package com.moyao.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    private Long id;

    private String mobile;

    private String username;

    private LocalDateTime dxModified;

}
