package com.kepware.opc.dto.status;

/**
 * 堆垛机状态
 *
 * @auther CalmLake
 * @create 2018/3/21  9:26
 */
public class MlBlockStatus extends BlockStatus {
    /**
     * 自/手动
     */
    private boolean auto;
    /**
     * 后退
     */
    private boolean backUp;
    /**
     * 故障
     */
    private boolean error;
    /**
     * 前进
     */
    private boolean goAhead;
    /**
     * 下降
     */
    private boolean goDown;
    /**
     * 上升
     */
    private boolean goUp;
    /**
     * 载车
     */
    private boolean theCar;
    /**
     * 列
     */
    private String line;
    /**
     * 层
     */
    private String tier;
    /**
     * 错误信息
     */
    private String errorCode;
    /**
     * 目标列
     */
    private String targetLine;
    /**
     * 目标层
     */
    private String targetTier;

    public MlBlockStatus(String blockNo) {
        super(blockNo);
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isBackUp() {
        return backUp;
    }

    public void setBackUp(boolean backUp) {
        this.backUp = backUp;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isGoAhead() {
        return goAhead;
    }

    public void setGoAhead(boolean goAhead) {
        this.goAhead = goAhead;
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

    public boolean isTheCar() {
        return theCar;
    }

    public void setTheCar(boolean theCar) {
        this.theCar = theCar;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }
}
