package org.ys.common.util;

import java.io.Closeable;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 计算工具类
 */
public class MathUtils {
    private MathUtils(){}

    /**
     * a除b，保留两位小数点
     * @param a
     * @param b
     */
    public static float mathFloat(int a,int b) {
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
        String floatStr = df.format((float) a / b);
        return Float.parseFloat(floatStr);
    }
}
