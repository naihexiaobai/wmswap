package com.wap.model;

/**
 * opc点位item
 *
 * @auther CalmLake
 * @create 2017/11/28  11:58
 */
public class OpcItemFinalString {
    /**
     * 堆垛机自/手动
     */
    public static final String DDJZISHOUDONG = "machine.duiDuoJi.ziShouDong";
    /**
     * 堆垛机待命
     */
    public static final String DDJDAIMING = "machine.duiDuoJi.daiMing";
    /**
     * 堆垛机空闲
     */
    public static final String DDJKONGXIAN = "machine.duiDuoJi.kongXian";
    /**
     * 堆垛机载荷
     */
    public static final String DDJZAIWU = "machine.duiDuoJi.zaiWu";
    /**
     * 堆垛机载车
     */
    public static final String DDJZAICHE = "machine.duiDuoJi.zaiChe";
    /**
     * 堆垛机任务码
     */
    public static final String DDJRENWUMA = "machine.duiDuoJi.renWuMa";
    /**
     * 堆垛机列
     */
    public static final String DDJLIE = "machine.duiDuoJi.lie";
    /**
     * 堆垛机层
     */
    public static final String DDJCENG = "machine.duiDuoJi.ceng";
    /**
     * 堆垛机目标列
     */
    public static final String DDJMUBIAOLIE = "machine.duiDuoJi.muBiaoLie";
    /**
     * 堆垛机目标层
     */
    public static final String DDJMUBIAOCENG = "machine.duiDuoJi.muBiaoCeng";
    /**
     * 堆垛机动作指令
     */
    public static final String DDJDONGZUOZHILING = "machine.duiDuoJi.dongZuoZhiLing";
    /**
     * 堆垛机动作任务码
     */
    public static final String DDJDONGZUORENWUHAO = "machine.duiDuoJi.dongZuoRenWuHao";


    /**
     * 一号子车group
     */
    public static final String YIHAOZICHEGROUP = "machine.yiHaoZiChe";
    /**
     * 二号子车group
     */
    public static final String ERHAOZICHEGROUP = "machine.erHaoZiChe";
    /**
     * 三号子车group
     */
    public static final String SANHAOZICHEGROUP = "machine.sanHaoZiChe";

    /**
     * 子车自/手动
     */
    public static final String ZICHEZISHOUDONG = "ziShouDong";
    /**
     * 子车待命
     */
    public static final String ZICHEDAIMING = "daiMing";
    /**
     * 子车空闲
     */
    public static final String ZICHEKONGXIAN = "kongXian";
    /**
     * 子车A待
     */
    public static final String ZICHEAYUANDIANDAIJI = "AYuanDianDaiJi";
    /**
     * 子车B待
     */
    public static final String ZICHEBYUANDIANDAIJI = "BYuanDianDaiJi";
    /**
     * 子车载物
     */
    public static final String ZICHEZAIWU = "zaiWu";
    /**
     * 子车是否故障
     */
    public static final String ZICHESHIFOUGUZHANG = "shiFouGuZhang";
    /**
     * 子车升降机待机
     */
    public static final String ZICHETISHENGJISHANGDAIJI = "tiShengJiShangDaiJI";
    /**
     * 子车rgv待机
     */
    public static final String ZICHEMUCHESHANGDAIJI = "muCheShangDaiJi";
    /**
     * 子车电量
     */
    public static final String ZICHEDIANLIANG = "dianLiang";
    /**
     * 子车任务码
     */
    public static final String ZICHERENWUMA = "renWuMa";
    /**
     * 子车列
     */
    public static final String ZICHELIE = "lie";
    /**
     * 子车排
     */
    public static final String ZICHEPAI = "pai";
    /**
     * 子车层
     */
    public static final String ZICHECENG = "ceng";
    /**
     * 子车wcs任务码
     */
    public static final String ZICHEWCSRENWUMA = "wcsRenWuMa";
    /**
     * 子车故障信息
     */
    public static final String ZICHEGUZHANGXINXI = "guZhangXinXi";
    /**
     * 子车动作指令
     */
    public static final String ZICHEDONGZUOZHILING = "dongZuoZhiLing";
    /**
     * 子车动作任务码
     */
    public static final String ZICHEDONGZUORENWUHAO = "dongZuoRenWuHao";
    /**
     * 子车目标层
     */
    public static final String ZICHEMUBIAOCENG = "muBiaoCeng";
    /**
     * 子车目标列
     */
    public static final String ZICHEMUBIAOLIE = "muBiaoLie";
    /**
     * 子车目标排
     */
    public static final String ZICHEMUBIAOPAI = "muBiaoPai";

    /**
     * 任务优先级 N
     */
    public static final String LEVEL_N = "N";
    /**
     * 任务优先级 R
     */
    public static final String LEVEL_R = "R";
    /**
     * 任务优先级 SR
     */
    public static final String LEVEL_SR = "SR";
    /**
     * 任务优先级 SSR
     */
    public static final String LEVEL_SSR = "SSR";

    /**
     * 0
     */
    public static final byte BYTE0 = 0;
    /**
     * 堆垛机设备id
     */
    public static final int DDJMACHINEID = 10;

    /**
     * 一号子车id
     */
    public static final int ZICHEYIMACHINEID = 4;
    /**
     * 二号子车id
     */
    public static final int ZICHEERMACHINEID = 5;
    /**
     * 三号子车id
     */
    public static final int ZICHESANMACHINEID = 6;

    /**
     * 静止
     */
    public static final byte COMMAND0 = 0;
    /**
     * 左侧取货
     */
    public static final byte COMMAND11RGV = 11;
    /**
     * 右侧取货
     */
    public static final byte COMMAND12RGV = 12;
    /**
     * 左侧出货
     */
    public static final byte COMMAND21RGV = 21;
    /**
     * 右侧出货
     */
    public static final byte COMMAND22RGV = 22;
    /**
     * 子车A面入货
     */
    public static final byte COMMAND1 = 1;
    /**
     * 子车A面出货
     */
    public static final byte COMMAND2 = 2;

    /**
     * 子车B面入货
     */
    public static final byte COMMAND3 = 3;
    /**
     * 子车B面出货
     */
    public static final byte COMMAND4 = 4;
    /**
     * 子车A面整仓
     */
    public static final byte COMMAND5 = 5;
    /**
     * 子车B面整仓
     */
    public static final byte COMMAND6 = 6;
    /**
     * 子车出母车到输送线B
     */
    public static final byte COMMAND7 = 7;
    /**
     * 子车上提升机
     */
    public static final byte COMMAND8 = 8;
    /**
     * 子车出输送机到输送线A
     */
    public static final byte COMMAND9 = 9;
    /**
     * 子车A面上RGV
     */
    public static final byte COMMAND11 = 11;
    /**
     * 子车A面下RGV
     */
    public static final byte COMMAND12 = 12;
    /**
     * 子车B面上RGV
     */
    public static final byte COMMAND13 = 13;
    /**
     * 子车B面下RGV
     */
    public static final byte COMMAND14 = 14;
    /**
     * 子车A面盘点
     */
    public static final byte COMMAND15 = 15;
    /**
     * 子车B面盘点
     */
    public static final byte COMMAND16 = 16;
    /**
     * 子车A-B面 切换
     */
    public static final byte COMMAND17 = 17;
    /**
     * 子车B-A面切换
     */
    public static final byte COMMAND18 = 18;
    /**
     * 子车小车充电
     */
    public static final byte COMMAND19 = 19;
    /**
     * 子车A面载货上RGV
     */
    public static final byte COMMAND21 = 21;
    /**
     * 子车A面载货下RGV
     */
    public static final byte COMMAND22 = 22;
    /**
     * 子车B面载货上RGV
     */
    public static final byte COMMAND23 = 23;
    /**
     * 子车B面载货下RGV
     */
    public static final byte COMMAND24 = 24;
}
