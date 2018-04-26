package com.thief.wcs.dao;

import com.thief.wcs.entity.Route;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("routeMapper")
public interface RouteMapper {
    int deleteByPrimaryKey(Integer id);

    List<Route> selectAll();

    int insert(Route record);

    int insertSelective(Route record);

    Route selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Route record);

    int updateByPrimaryKey(Route record);
}