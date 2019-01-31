package org.ys.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

/**
 * 密码工具类
 * @author Louis
 * @date Sep 1, 2018
 */
public class PasswordUtils {
	private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	private PasswordUtils(){}

	/**
	 * 匹配密码
	 * @param rawPass 明文
	 * @param encPass 密文
	 * @return
	 */
	public static boolean matches(String rawPass, String encPass) {
		return bCryptPasswordEncoder.matches(rawPass,encPass);
	}
	
	/**
	 * 明文密码加密
	 * @param rawPass 明文
	 * @return
	 */
	public static String encode(String rawPass) {
		return bCryptPasswordEncoder.encode(rawPass);
	}
}
