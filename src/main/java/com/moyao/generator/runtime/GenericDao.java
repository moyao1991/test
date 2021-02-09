package com.moyao.generator.runtime;

import java.util.List;

public interface GenericDao<T extends BaseDo> {

    T getById(Long id);

    List<T> getByIdList(List<Long> idList);

    T findById(Long id);

    List<T> findByIdList(List<Long> idList);

    int insert(T record);

    int insertList(List<T> records);

    int update(T record);

    int updateByVersion(T record);

    int delete(Long id);

    int deleteList(List<Long> idList);

    int deleteByVersion(T record);
}
