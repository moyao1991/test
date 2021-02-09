package com.moyao.mapper;

import java.util.List;

import static com.moyao.entity.User.ALL_COLUMN;

import com.moyao.entity.User;
import com.moyao.generator.runtime.GenericDao;
import com.moyao.generator.runtime.NotSupportDelete;
import com.moyao.generator.runtime.NotSupportVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends GenericDao<User>, NotSupportVersion, NotSupportDelete {

    @Select(ALL_COLUMN)
    List<User> selectAll();
}