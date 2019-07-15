package com.hxy.common.utils;

import java.math.BigDecimal;

/**
 * BigDecimal运算工具类，解决浮点运算精度丢失问题
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: BigDecimalUtil
 * @date 2019年07月15日 17:38:29
 */
public class BigDecimalUtil {

    /**
     * 远离0的方向1.1-> 2   1.5-> 2   1.8-> 2   -1.1-> -2   -1.5-> -2   -1.8-> -2
     */
    public final static int ROUND_UP = BigDecimal.ROUND_UP;
    /**
     * 与ROUND_UP相反，向0的方向移动1.1-> 1   1.5-> 1   1.8-> 1   -1.1-> -1   -1.5-> -1   -1.8> -1
     */
    public final static int ROUND_DOWN = BigDecimal.ROUND_DOWN;
    /**
     * 舍位时往正无穷方向移动   1.1-> 2   1.5-> 2   1.8-> 2   -1.1-> -1   -1.5-> -1   -1.8-> -1
     */
    public final static int ROUND_CEILING = BigDecimal.ROUND_CEILING;
    /**
     * 与CEILING相反，往负无穷   1.1-> 1   1.5-> 1   1.8-> 1   -1.1-> -2   -1.5-> -2   -1.8-> -2
     */
    public final static int ROUND_FLOOR = BigDecimal.ROUND_FLOOR;
    /**
     * 四舍五入
     */
    public final static int ROUND_HALF_UP = BigDecimal.ROUND_HALF_UP;
    /**
     * 以5为分界线，或曰五舍六入1.5-> 1   1.6-> 2   -1.5-> -1   -1.6-> -2
     */
    public final static int ROUND_HALF_DOWN = BigDecimal.ROUND_HALF_DOWN;
    /**
     * 同样以5为分界线，如果是5，则前一位变偶数1.15-> 1.2   1.16-> 1.2   1.25-> 1.2   1.26-> 1.3
     */
    public final static int ROUND_HALF_EVEN = BigDecimal.ROUND_HALF_EVEN;
    /**
     * 无需舍位
     */
    public final static int ROUND_UNNECESSARY = BigDecimal.ROUND_UNNECESSARY;

    /**
     * 将构造器私有化，不允许外部创建该类的实体
     */
    private BigDecimalUtil() {
    }

    /**
     * 两数相加
     *
     * @param v1 加数
     * @param v2 被加数
     * @return 相加结果
     */
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 两数相减
     *
     * @param v1 减数
     * @param v2 被减数
     * @return 相减结果
     */
    public static BigDecimal subtract(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 两数相乘
     *
     * @param v1 乘数1
     * @param v2 乘数2
     * @return
     */
    public static BigDecimal multiply(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 两数相除
     * 四舍五入，保留两位小数
     *
     * @param v1 除数
     * @param v2 被除数
     * @return 相除结果
     */
    public static BigDecimal divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两数相除（设置保留位数跟舍位模式）
     *
     * @param v1            除数
     * @param v2            被除数
     * @param roundingModel 舍入模式
     * @param scale         保留位数
     * @return 相除结果
     */
    public static BigDecimal divide(double v1, double v2, int scale, int roundingModel) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, roundingModel);
    }
}
