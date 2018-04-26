package com.kepware.opc.dto.status;

/**
 * 母车状态
 *
 * @auther CalmLake
 * @create 2018/3/21  9:34
 */
public class McBlockStatus extends BlockStatus {
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
     * 载车
     */
    private boolean theCar;
    /**
     * 列
     */
    private String line;
    /**
     * 目标列
     */
    private String targetLine;

    public McBlockStatus(String blockNo) {
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

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }
}
