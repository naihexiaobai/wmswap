package com.kepware.opc.dto.status;

/**
 * 升降机状态
 *
 * @auther CalmLake
 * @create 2018/3/21  9:16
 */
public class ElBlockStatus extends BlockStatus {
    /**
     * 自/手动
     */
    private boolean auto;
    /**
     * 故障
     */
    private boolean error;
    /**
     * 故障信息
     */
    private String errorCode;
    /**
     * 下降
     */
    private boolean goDown;
    /**
     * 上升
     */
    private boolean goUp;
    /**
     * 复位
     */
    private boolean reset;
    /**
     * 载车
     */
    private boolean theCar;
    /**
     * 目标层
     */
    private String targetTier;
    /**
     * 当前层
     */
    private String tier;

    public ElBlockStatus(String blockNo) {
        super(blockNo);
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isGoDown() {
        return goDown;
    }

    public void setGoDown(boolean goDown) {
        this.goDown = goDown;
    }

    public boolean isGoUp() {
        return goUp;
    }

    public void setGoUp(boolean goUp) {
        this.goUp = goUp;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }

    public boolean isTheCar() {
        return theCar;
    }

    public void setTheCar(boolean theCar) {
        this.theCar = theCar;
    }
}
