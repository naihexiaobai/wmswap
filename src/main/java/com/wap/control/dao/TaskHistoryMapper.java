package com.wap.control.dao;

import com.wap.model.TaskHistory;
import org.springframework.stereotype.Repository;

@Repository("taskHistoryMapper")
public interface TaskHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskHistory record);

    int insertSelective(TaskHistory record);

    TaskHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskHistory record);

    int updateByPrimaryKey(TaskHistory record);
}