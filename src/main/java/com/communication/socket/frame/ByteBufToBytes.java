package com.communication.socket.frame;

import io.netty.buffer.ByteBuf;

/**
 * Created by admin on 2017/8/25.
 */
public class ByteBufToBytes {
    public byte[] read(ByteBuf datas) {
        byte[] bytes = new byte[datas.readableBytes()];
        datas.readBytes(bytes);
        return bytes;
    }
}
