package com.kepware.opc.dto.status;

/**
 * 子车状态
 *
 * @auther CalmLake
 * @create 2018/3/21  9:39
 */
public class ScBlockStatus extends BlockStatus {
    /**
     * A原点
     */
    private boolean AOrigin;
    /**
     * 后退
     */
    private boolean backUp;
    /**
     * B原点
     */
    private boolean BOrigin;
    /**
     * 充电
     */
    private boolean charging;
    /**
     * 提升机上待机
     */
    private boolean elevator;
    /**
     * 故障
     */
    private boolean error;
    /**
     * A原点
     */
    private boolean free;
    /**
     * 空闲
     */
    private boolean goAhead;
    /**
     * 放板
     */
    private boolean goDown;
    /**
     * 取板
     */
    private boolean jacking;
    /**
     * 母车上待机
     */
    private boolean RGV;
    /**
     * 自/手动
     */
    private boolean auto;


    /**
     * 列
     */
    private String line;
    /**
     * 排
     */
    private String row;
    /**
     * 层
     */
    private String tier;
    /**
     * 电量
     */
    private String kWh;
    /**
     * 目标列
     */
    private String targetLine;
    /**
     * 目标排
     */
    private String targetRow;
    /**
     * 目标层
     */
    private String targetTier;


    public ScBlockStatus(String blockNo) {
        super(blockNo);
    }

    public boolean isAOrigin() {
        return AOrigin;
    }

    public void setAOrigin(boolean AOrigin) {
        this.AOrigin = AOrigin;
    }

    public boolean isBackUp() {
        return backUp;
    }

    public void setBackUp(boolean backUp) {
        this.backUp = backUp;
    }

    public boolean isBOrigin() {
        return BOrigin;
    }

    public void setBOrigin(boolean BOrigin) {
        this.BOrigin = BOrigin;
    }

    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public boolean isFree() {
        return free;
    }

    @Override
    public void setFree(boolean free) {
        this.free = free;
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

    public boolean isJacking() {
        return jacking;
    }

    public void setJacking(boolean jacking) {
        this.jacking = jacking;
    }

    public boolean isRGV() {
        return RGV;
    }

    public void setRGV(boolean RGV) {
        this.RGV = RGV;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getkWh() {
        return kWh;
    }

    public void setkWh(String kWh) {
        this.kWh = kWh;
    }

    public String getTargetLine() {
        return targetLine;
    }

    public void setTargetLine(String targetLine) {
        this.targetLine = targetLine;
    }

    public String getTargetRow() {
        return targetRow;
    }

    public void setTargetRow(String targetRow) {
        this.targetRow = targetRow;
    }

    public String getTargetTier() {
        return targetTier;
    }

    public void setTargetTier(String targetTier) {
        this.targetTier = targetTier;
    }
}
