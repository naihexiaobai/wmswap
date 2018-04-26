package com.wap.dao;

import com.wap.model.Cargo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("cargoMapper")
public interface CargoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cargo record);

    int insertSelective(Cargo record);

    List<Cargo> selectByCargo(Cargo record);

    List<Cargo> selectAll();

    Cargo selectByCargoStorageNo(String storageNo);

    Cargo selectByCargoPalletNo(String palletNo);

    Cargo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cargo record);

    int updateByPrimaryKey(Cargo record);
}