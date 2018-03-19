package com.www.util;

/**
 * 十六进制数据
 *
 * @auther CalmLake
 * @create 2017/12/24  14:52
 */
public enum HexAscIIEnum {

    HEX_0(0xff30, "0"), HEX_1(0xff31, "1"), HEX_2(0xff32, "2"), HEX_3(0xff33, "3"), HEX_4(0xff34, "4"),
    HEX_5(0xff35, "5"), HEX_6(0xff36, "6"), HEX_7(0xff37, "7"), HEX_8(0xff38, "8"), HEX_9(0xff39, "9"),
    HEX_A(0xff41, "A"), HEX_B(0xff42, "B"), HEX_C(0xff43, "C"), HEX_D(0xff44, "D"), HEX_E(0xff45, "E"), HEX_F(0xff46, "F"), HEX_G(0xff47, "G"),
    HEX_H(0xff48, "H"), HEX_I(0xff49, "I"), HEX_J(0xff4A, "J"), HEX_K(0xff4B, "K"), HEX_L(0xff4C, "L"), HEX_M(0xff4D, "M"), HEX_N(0xff4E, "N"),
    HEX_O(0xff4F, "O"), HEX_P(0xff50, "P"), HEX_Q(0xff51, "Q"), HEX_R(0xff52, "R"), HEX_S(0xff53, "S"), HEX_T(0xff54, "T"), HEX_U(0xff55, "U"),
    HEX_V(0xff56, "V"), HEX_W(0xff57, "W"), HEX_X(0xff58, "X"), HEX_Y(0xff59, "Y"), HEX_Z(0xff5A, "Z"),
    HEX_a(0xff61, "a"), HEX_b(0xff62, "b"), HEX_c(0xff63, "c"), HEX_d(0xff64, "d"), HEX_e(0xff65, "e"), HEX_f(0xff66, "f"), HEX_g(0xff67, "g"),
    HEX_h(0xff68, "h"), HEX_i(0xff69, "i"), HEX_j(0xff6A, "j"), HEX_k(0xff6B, "k"), HEX_l(0xff6C, "l"), HEX_m(0xff6D, "m"), HEX_n(0xff6E, "n"),
    HEX_o(0xff6F, "o"), HEX_p(0xff70, "p"), HEX_q(0xff71, "q"), HEX_r(0xff72, "r"), HEX_s(0xff73, "s"), HEX_t(0xff74, "t"), HEX_u(0xff75, "u"),
    HEX_v(0xff76, "v"), HEX_w(0xff77, "w"), HEX_x(0xff78, "x"), HEX_y(0xff79, "y"), HEX_z(0xff7A, "z");

    private int value;
    private String name;

    HexAscIIEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 获取String
     *
     * @param value
     * @return String
     */
    public static String getName(int value) {
        for (HexAscIIEnum hexAscIIEnum : HexAscIIEnum.values()) {
            if (hexAscIIEnum.value == value) {
                return hexAscIIEnum.name;
            }
        }
        return null;
    }

    /**
     *
     * @param name
     * @return int
     */
    public static int getValue(String name) {
        for (HexAscIIEnum hexAscIIEnum : HexAscIIEnum.values()) {
            if (hexAscIIEnum.name.equals(name)) {
                return hexAscIIEnum.value;
            }
        }
        return 999;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
