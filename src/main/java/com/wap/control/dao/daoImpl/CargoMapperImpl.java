package com.wap.control.dao.daoImpl;

import com.wap.control.dao.CargoMapper;
import com.wap.model.Cargo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 货物
 *
 * @auther CalmLake
 * @create 2017/11/24  11:58
 */
@Service("cargoMapperImpl")
public class CargoMapperImpl implements CargoMapper {
    @Resource(name = "cargoMapper")
    private CargoMapper cargoMapper;

    public int deleteByPrimaryKey(Integer id) {
        return cargoMapper.deleteByPrimaryKey(id);
    }

    public int insert(Cargo record) {
        return cargoMapper.insert(record);
    }

    public int insertSelective(Cargo record) {
        return cargoMapper.insertSelective(record);
    }

    public List<Cargo> selectByCargo(Cargo record) {
        return cargoMapper.selectByCargo(record);
    }

    public Cargo selectByPrimaryKey(Integer id) {
        return cargoMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Cargo record) {
        return cargoMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Cargo record) {
        return cargoMapper.updateByPrimaryKey(record);
    }
}
