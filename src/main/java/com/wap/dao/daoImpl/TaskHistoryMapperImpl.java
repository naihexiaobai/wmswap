package com.wap.dao.daoImpl;

import com.wap.dao.TaskHistoryMapper;
import com.wap.model.TaskHistory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 任务历史
 *
 * @auther CalmLake
 * @create 2017/11/24  11:56
 */
@Service("taskHistoryMapperImpl")
public class TaskHistoryMapperImpl implements TaskHistoryMapper {
    @Resource(name = "taskHistoryMapper")
    private TaskHistoryMapper taskHistoryMapper;

    public int deleteByPrimaryKey(Integer id) {
        return taskHistoryMapper.deleteByPrimaryKey(id);
    }

    public int insert(TaskHistory record) {
        return taskHistoryMapper.insert(record);
    }

    public int insertSelective(TaskHistory record) {
        return taskHistoryMapper.insertSelective(record);
    }

    public TaskHistory selectByPrimaryKey(Integer id) {
        return taskHistoryMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(TaskHistory record) {
        return taskHistoryMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(TaskHistory record) {
        return taskHistoryMapper.updateByPrimaryKey(record);
    }
}
