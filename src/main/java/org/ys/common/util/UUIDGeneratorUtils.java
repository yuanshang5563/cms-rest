package org.ys.common.util;

import java.util.UUID;

/**
 * UUID生成工具类
 */
public class UUIDGeneratorUtils {
    private UUIDGeneratorUtils() {}

    public static String generateUUID(){
        String s = UUID.randomUUID().toString();
        //去掉“-”符号
        return s.replaceAll("-","");
    }
}
