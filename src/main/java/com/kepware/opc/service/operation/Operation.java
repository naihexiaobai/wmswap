package com.kepware.opc.service.operation;


import com.kepware.opc.dto.command.BlockCommand;

/**
 * @auther CalmLake
 * @create 2018/3/27  15:52
 */
public interface Operation {

    boolean isFinishWork(BlockCommand blockCommand, String blockNo) throws Exception;

}
