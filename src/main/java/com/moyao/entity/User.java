package com.moyao.entity;

import com.moyao.generator.runtime.BaseDo;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *   用户表
 */
@Builder
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseDo implements Serializable {
    private String externalUserId;

    private Byte type;

    private String mobile;

    private String username;

    private String userNick;

    private Long mainUserId;

    private Long originInstanceId;

    private static final long serialVersionUID = 1L;

    public static final String ALL_COLUMN = " select id,external_user_id,type,mobile,username,user_nick,dx_created,dx_modified,main_user_id,origin_instance_id from users";
}