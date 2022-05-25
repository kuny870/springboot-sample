/*
 *  CryptoUtils.java    $Revision: 12696 $ $Date: 2018-07-26 10:03:26 +0900 (목, 26 7월 2018) $
 * 
 *  Copyright (c) 2008-2018 WIZVERA Corp.
 *  All rights reserved. http://www.wizvera.com/
 *
 *  This software is the confidential and proprietary information of
 *  WIZVERA Corp. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with WIZVERA.
 */
package com.wizvera.templet.util;

import org.apache.tomcat.util.buf.HexUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 암호화 관련 유틸 메소드를 제공하는 유틸 클래스이다.
 *
 * @author jeehoon.song
 * @version $Revision: 12696 $
 * @since 2018. 7. 4.
 */
public class CryptoUtils {
	private static final String	HASH_ALG_NAME_SHA_256	= "SHA-256";
	private static final String	DEFAULT_CHARSET			= "UTF-8";

	/**
	 * 여러개의 byte array들에 대하여 SHA-256 해쉬 함수를 적용한 값을 반환한다.
	 * 
	 * @param dataArray
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hashWithSHA256(byte[]... dataArray) throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance(HASH_ALG_NAME_SHA_256);
		for (byte[] data : dataArray) {
			sha256.update(data);
		}
		return sha256.digest();
	}

	/**
	 * 특정 byte array에 대하여 특정 회수만큼 SHA-256 해쉬 함수를 적용한 값을 반환한다.
	 * 
	 * @param data
	 * @param count
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] hashWithSHA256(byte[] data,
										int count)
			throws NoSuchAlgorithmException {
		for (int i = 0; i < count; i++) {
			data = hashWithSHA256(data);
		}
		return data;
	}

	/**
	 * Creates a random bytes whose length is the number of characters specified.
	 * 
	 * @param length
	 *        the length of random bytes to create
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final byte[] genRandom(int length) throws NoSuchAlgorithmException {
		byte[] random = new byte[length];
		SecureRandom sr;
		sr = SecureRandom.getInstance("SHA1PRNG");
		sr.nextBytes(random);
		return random;
	}

	// public static void main(String[] args) throws NoSuchAlgorithmException {
	// System.out.println(HexUtils.encodeToString(hashWithSHA256("pinsignadmin".getBytes(), 256)));
	// System.out.println(HexUtils.encodeToString(hashWithSHA256("pinsignadmin".getBytes(), 512)));
	//
	// System.out.println(HexUtils.encodeToString(hashWithSHA256("12345".getBytes(), 256)));
	// System.out.println(HexUtils.encodeToString(hashWithSHA256("12345".getBytes(), 512)));
	//
	// String tmp = "u005_name-u005-BNJP5il";
	// String[] tmps = tmp.split("\\-");
	//
	// for (int i = 0; i < tmps.length; i++) {
	// System.out.println(tmps[i]);
	// }

	// byte[] key = null;
	// byte[] data = null;
	// Mac sha256hmac = Mac.getInstance("HmacSHA456");
	// SecretKeySpec mackey = new SecretKeySpec(key, "HmacSHA256");
	// try {
	// sha256hmac.init(mackey);
	// }
	// catch (InvalidKeyException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// sha256hmac.doFinal(data);
	// }

	/**
	 * SHA-256 알고리즘을 이용하여 특정 회수만큼 해쉬함수를 적용한 값을 반환한다.
	 * 
	 * @param input
	 *        the array of bytes
	 * @param count
	 *        iteration count
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static final byte[] genSHA256(	byte[] input,
											int count)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(HASH_ALG_NAME_SHA_256);
		byte[] hashValue = input;

		for (int i = 0; i < count; i++) {
			hashValue = md.digest(hashValue);
		}
		return hashValue;
	}

	/**
	 * SHA-256 알고리즘을 이용하여 특정 회수만큼 해쉬함수를 적용한 값을 Hex값으로 변환하여 반환한다.
	 * 
	 * @param input
	 *        the string
	 * @param count
	 *        iteration count
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static final String genHexSHA256(String input,
											int count)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] hashValue = genSHA256(input.getBytes(DEFAULT_CHARSET), count);
		return HexUtils.toHexString(hashValue);
		// return HexUtils.encodeToString(hashValue);
	}
}
