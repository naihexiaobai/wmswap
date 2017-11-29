package com.wap.control.dao;

import com.wap.model.Storage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("storageMapper")
public interface StorageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Storage record);

    int insertSelective(Storage record);

    List<Storage> selectByStorage(Storage record);

    Storage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Storage record);

    int updateByPrimaryKey(Storage record);
}