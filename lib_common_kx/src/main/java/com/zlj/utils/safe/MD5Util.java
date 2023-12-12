package com.zlj.utils.safe;

import java.security.MessageDigest;

/***
 * MD5的工具类
 */
public final class MD5Util
{

	private static final int ONE_ONE = 7;
	private static final int ONE_TWO = 12;
	private static final int ONE_THREE = 17;
	private static final int ONE_FOUR = 22;

	private static final int TWO_ONE = 5;
	private static final int TWO_TWO = 9;
	private static final int TWO_THREE = 14;
	private static final int TWO_FOUR = 20;

	private static final int THREE_ONE = 4;
	private static final int THREE_TWO = 11;
	private static final int THREE_THREE = 16;
	private static final int THREE_FOUR = 23;

	private static final int FOUR_ONE = 6;
	private static final int FOUR_TWO = 10;
	private static final int FOUR_THREE = 15;
	private static final int FOUR_FOUR = 21;
	private static final byte BYTE_128 = -128;
	private static long[] state = new long[4];
	private static long[] count = new long[2];
	private static byte[] buffer = new byte[64];
	private static byte[] padding =
	{ BYTE_128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static byte[] digest = new byte[16];
	private static String digestHexStr;

	private static String getMD5ofStr(String inbuf)
	{
		md5Init();
		md5Update(inbuf.getBytes(), inbuf.length());
		md5Final();
		StringBuffer str = new StringBuffer(32);
		for (int i = 0; i < 16; i++)
		{
			String tmp = byteHEX(digest[i]);
			str.append(tmp);
		}
		digestHexStr = str.toString();
		return digestHexStr;
	}

	private static String byteHEX(byte ib)
	{
		char[] digit =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		char[] ob = new char[2];
		ob[0] = digit[(ib >>> 4) & 0x0F];
		ob[1] = digit[ib & 0x0F];
		StringBuffer sb = new StringBuffer();
		sb.append(ob[0]);
		sb.append(ob[1]);
		String result = sb.toString();
		return result;
	}

	public static String MD5(String str)
	{
		return getMD5ofStr(str);
	}

	private static void md5Init()
	{
		count[0] = 0L;
		count[1] = 0L;

		state[0] = 0x67452301L;
		state[1] = 0xefcdab89L;
		state[2] = 0x98badcfeL;
		state[3] = 0x10325476L;

		return;
	}

	private static void md5Update(byte[] inbuf, int inputLen)
	{

		int i;
		int index;
		int partLen;
		byte[] block = new byte[64];
		index = (int) (count[0] >>> 3) & 0x3F;

		if ((count[0] += (inputLen << 3)) < (inputLen << 3))
		{
			count[1]++;
		}
		count[1] += (inputLen >>> 29);

		partLen = 64 - index;

		if (inputLen >= partLen)
		{
			md5Memcpy(buffer, inbuf, index, 0, partLen);
			md5Transform(buffer);

			for (i = partLen; i + 63 < inputLen; i += 64)
			{

				md5Memcpy(block, inbuf, 0, i, 64);
				md5Transform(block);
			}
			index = 0;

		} else
		{
			i = 0;
		}

		md5Memcpy(buffer, inbuf, index, i, inputLen - i);
	}

	private static void md5Final()
	{
		byte[] bits = new byte[8];
		int index;
		int padLen;
		encode(bits, count, 8);
		index = (int) (count[0] >>> 3) & 0x3f;
		padLen = (index < 56) ? (56 - index) : (120 - index);
		md5Update(padding, padLen);
		md5Update(bits, 8);
		encode(digest, state, 16);
	}

	private static void md5Memcpy(byte[] output, byte[] input, int outpos,
			int inpos, int len)
	{
		int i;

		for (i = 0; i < len; i++)
		{
			output[outpos + i] = input[inpos + i];
		}
	}

	private static void md5Transform(byte[] block)
	{
		long a = state[0];
		long b = state[1];
		long c = state[2];
		long d = state[3];
		long[] x = new long[16];

		decode(x, block, 64);

		a = ff(a, b, c, d, x[0], ONE_ONE, 0xd76aa478L);
		d = ff(d, a, b, c, x[1], ONE_TWO, 0xe8c7b756L);
		c = ff(c, d, a, b, x[2], ONE_THREE, 0x242070dbL);
		b = ff(b, c, d, a, x[3], ONE_FOUR, 0xc1bdceeeL);
		a = ff(a, b, c, d, x[4], ONE_ONE, 0xf57c0fafL);

		d = ff(d, a, b, c, x[5], ONE_TWO, 0x4787c62aL);
		c = ff(c, d, a, b, x[6], ONE_THREE, 0xa8304613L);
		b = ff(b, c, d, a, x[7], ONE_FOUR, 0xfd469501L);
		a = ff(a, b, c, d, x[8], ONE_ONE, 0x698098d8L);
		d = ff(d, a, b, c, x[9], ONE_TWO, 0x8b44f7afL);
		c = ff(c, d, a, b, x[10], ONE_THREE, 0xffff5bb1L);
		b = ff(b, c, d, a, x[11], ONE_FOUR, 0x895cd7beL);
		a = ff(a, b, c, d, x[12], ONE_ONE, 0x6b901122L);
		d = ff(d, a, b, c, x[13], ONE_TWO, 0xfd987193L);
		c = ff(c, d, a, b, x[14], ONE_THREE, 0xa679438eL);
		b = ff(b, c, d, a, x[15], ONE_FOUR, 0x49b40821L);

		a = gg(a, b, c, d, x[1], TWO_ONE, 0xf61e2562L);
		d = gg(d, a, b, c, x[6], TWO_TWO, 0xc040b340L);
		c = gg(c, d, a, b, x[11], TWO_THREE, 0x265e5a51L);
		b = gg(b, c, d, a, x[0], TWO_FOUR, 0xe9b6c7aaL);
		a = gg(a, b, c, d, x[5], TWO_ONE, 0xd62f105dL);
		d = gg(d, a, b, c, x[10], TWO_TWO, 0x2441453L);
		c = gg(c, d, a, b, x[15], TWO_THREE, 0xd8a1e681L);
		b = gg(b, c, d, a, x[4], TWO_FOUR, 0xe7d3fbc8L);
		a = gg(a, b, c, d, x[9], TWO_ONE, 0x21e1cde6L);
		d = gg(d, a, b, c, x[14], TWO_TWO, 0xc33707d6L);
		c = gg(c, d, a, b, x[3], TWO_THREE, 0xf4d50d87L);
		b = gg(b, c, d, a, x[8], TWO_FOUR, 0x455a14edL);
		a = gg(a, b, c, d, x[13], TWO_ONE, 0xa9e3e905L);
		d = gg(d, a, b, c, x[2], TWO_TWO, 0xfcefa3f8L);
		c = gg(c, d, a, b, x[7], TWO_THREE, 0x676f02d9L);
		b = gg(b, c, d, a, x[12], TWO_FOUR, 0x8d2a4c8aL);

		a = hh(a, b, c, d, x[5], THREE_ONE, 0xfffa3942L);
		d = hh(d, a, b, c, x[8], THREE_TWO, 0x8771f681L);
		c = hh(c, d, a, b, x[11], THREE_THREE, 0x6d9d6122L);
		b = hh(b, c, d, a, x[14], THREE_FOUR, 0xfde5380cL);
		a = hh(a, b, c, d, x[1], THREE_ONE, 0xa4beea44L);
		d = hh(d, a, b, c, x[4], THREE_TWO, 0x4bdecfa9L);
		c = hh(c, d, a, b, x[7], THREE_THREE, 0xf6bb4b60L);
		b = hh(b, c, d, a, x[10], THREE_FOUR, 0xbebfbc70L);
		a = hh(a, b, c, d, x[13], THREE_ONE, 0x289b7ec6L);
		d = hh(d, a, b, c, x[0], THREE_TWO, 0xeaa127faL);
		c = hh(c, d, a, b, x[3], THREE_THREE, 0xd4ef3085L);
		b = hh(b, c, d, a, x[6], THREE_FOUR, 0x4881d05L);
		a = hh(a, b, c, d, x[9], THREE_ONE, 0xd9d4d039L);
		d = hh(d, a, b, c, x[12], THREE_TWO, 0xe6db99e5L);
		c = hh(c, d, a, b, x[15], THREE_THREE, 0x1fa27cf8L);
		b = hh(b, c, d, a, x[2], THREE_FOUR, 0xc4ac5665L);

		a = ii(a, b, c, d, x[0], FOUR_ONE, 0xf4292244L);
		d = ii(d, a, b, c, x[7], FOUR_TWO, 0x432aff97L);
		c = ii(c, d, a, b, x[14], FOUR_THREE, 0xab9423a7L);
		b = ii(b, c, d, a, x[5], FOUR_FOUR, 0xfc93a039L);
		a = ii(a, b, c, d, x[12], FOUR_ONE, 0x655b59c3L);
		d = ii(d, a, b, c, x[3], FOUR_TWO, 0x8f0ccc92L);
		c = ii(c, d, a, b, x[10], FOUR_THREE, 0xffeff47dL);
		b = ii(b, c, d, a, x[1], FOUR_FOUR, 0x85845dd1L);
		a = ii(a, b, c, d, x[8], FOUR_ONE, 0x6fa87e4fL);
		d = ii(d, a, b, c, x[15], FOUR_TWO, 0xfe2ce6e0L);
		c = ii(c, d, a, b, x[6], FOUR_THREE, 0xa3014314L);
		b = ii(b, c, d, a, x[13], FOUR_FOUR, 0x4e0811a1L);
		a = ii(a, b, c, d, x[4], FOUR_ONE, 0xf7537e82L);
		d = ii(d, a, b, c, x[11], FOUR_TWO, 0xbd3af235L);
		c = ii(c, d, a, b, x[2], FOUR_THREE, 0x2ad7d2bbL);
		b = ii(b, c, d, a, x[9], FOUR_FOUR, 0xeb86d391L);

		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
	}

	private static void encode(byte[] output, long[] input, int len)
	{
		int i;
		int j;

		for (i = 0, j = 0; j < len; i++, j += 4)
		{
			output[j] = (byte) (input[i] & 0xffL);
			output[j + 1] = (byte) ((input[i] >>> 8) & 0xffL);
			output[j + 2] = (byte) ((input[i] >>> 16) & 0xffL);
			output[j + 3] = (byte) ((input[i] >>> 24) & 0xffL);
		}
	}

	private static void decode(long[] output, byte[] input, int len)
	{
		int i;
		int j;

		for (i = 0, j = 0; j < len; i++, j += 4)
			output[i] = b2iu(input[j]) | (b2iu(input[j + 1]) << 8)
					| (b2iu(input[j + 2]) << 16) | (b2iu(input[j + 3]) << 24);

		return;
	}

	private static long b2iu(byte b)
	{
		return b < 0 ? b & 0x7F + 128 : b;
	}

	private static long ff(long a, long b, long c, long d, long x, long s,
			long ac)
	{
		a += f(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private static long gg(long a, long b, long c, long d, long x, long s,
			long ac)
	{
		a += g(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private static long hh(long a, long b, long c, long d, long x, long s,
			long ac)
	{
		a += h(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private static long ii(long a, long b, long c, long d, long x, long s,
			long ac)
	{
		a += i(b, c, d) + x + ac;
		a = ((int) a << s) | ((int) a >>> (32 - s));
		a += b;
		return a;
	}

	private static long f(long x, long y, long z)
	{
		return (x & y) | ((~x) & z);

	}

	private static long g(long x, long y, long z)
	{
		return (x & z) | (y & (~z));

	}

	private static long h(long x, long y, long z)
	{
		return x ^ y ^ z;
	}

	private static long i(long x, long y, long z)
	{
		return y ^ (x | (~z));
	}

	/**
	 *
	 * @param origin
	 * @param charsetname 可为null
	 * @return
	 */
	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
