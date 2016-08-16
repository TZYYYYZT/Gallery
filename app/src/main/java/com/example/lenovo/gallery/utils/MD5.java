package com.example.lenovo.gallery.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * 
 * 类的名称：MD5<br>
 * 类的描述：MD5功能类<br>
 * 编码作者：LiuQiang<br>
 * 创建时间：2014-12-25<br>
 * 修改时间：2014-12-25<br>
 * 版 本：V1.0<br>
 * 
 */
public class MD5 {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);

		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 
	 * 功能：计算文件MD5值<br>
	 * 编码作者：LiuQiang<br>
	 * 创建时间：2014-12-25<br>
	 * 修改时间：2014-12-25<br>
	 * 版 本：V1.0<br>
	 * 
	 * @param filename
	 * @return<br>
	 * 
	 */
	public static String md5SumFile(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;

		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * 功能：计算String MD5值<br>
	 * 编码作者：LiuQiang<br>
	 * 创建时间：2014-12-25<br>
	 * 修改时间：2014-12-25<br>
	 * 版 本：V1.0<br>
	 * 
	 * @param s
	 * @return<br>
	 * 
	 */
	public static String md5SumString(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
