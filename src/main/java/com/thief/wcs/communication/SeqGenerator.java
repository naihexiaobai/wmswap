package com.thief.wcs.communication;

import org.apache.commons.lang3.StringUtils;

/**
 * 消息序号
 *
 * @auther CalmLake
 * @create 2018/3/14  11:14
 */
public class SeqGenerator {
    private int seqNoToBeTransmitted = 0;
    private int seqNoToBeReceived = 0;

    public void reset() {
        seqNoToBeTransmitted = 0;
        seqNoToBeReceived = 0;
    }

    public synchronized String getSeqNoToBeTransmitted() {
        if (9999 == seqNoToBeTransmitted) {
            seqNoToBeTransmitted = 1;
            return ("9999");
        } else {
            return StringUtils.leftPad(String.valueOf(seqNoToBeTransmitted++), 4, '0');
        }
    }

    public void setSeqNoToBeTransmitted(String seqNo) {
        seqNoToBeTransmitted = Integer.parseInt(seqNo);
    }

    public String getSeqNoToBeReceived() {
        if (9999 == seqNoToBeReceived) {
            seqNoToBeReceived = 1;
            return ("9999");
        } else {
            return StringUtils.leftPad(String.valueOf(seqNoToBeReceived++), 4, '0');
        }
    }

    public void setSeqNoToBeReceived(String seqNo) {
        seqNoToBeReceived = Integer.parseInt(seqNo);
    }

    public boolean isValidSeqNo(int seqNo) {
        if (seqNo != seqNoToBeReceived - 1) {
            seqNoToBeReceived = seqNo + 1;
            return true;
        } else {
            return false;
        }
    }
}
