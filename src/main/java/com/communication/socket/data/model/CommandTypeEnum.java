package com.communication.socket.data.model;

/**
 * 指令类型枚举
 *
 * @auther CalmLake
 * @create 2017/11/8  9:29
 */
public enum CommandTypeEnum {

    HANDSHAKE(1, "握手"), HEARTBEAT(2, "心跳");

    int value;
    String name;

    CommandTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
