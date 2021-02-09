package com.moyao.generator.runtime;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseDo {

    private Long id;

    private Long version;

    private Boolean deleted;

    private LocalDateTime dxCreated;

    private LocalDateTime dxModified;
}
