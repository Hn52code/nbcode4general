package com.pads.nbiot.decoder;

public class Utilty {

    public static final int MIN_MID_VALUE = 1;
    public static final int MAX_MID_VALUE = 65535;

    private static Utilty instance = new Utilty();

    public static Utilty getInstance() {
        return instance;
    }

    // 截取字节数组中指定长度的元素转为整型值
    public int bytes2Int(byte[] b, int start, int length) {
        int sum = 0;
        int end = start + length;
        for (int k = start; k < end; k++) {
            int n = ((int) b[k]) & 0xff;
            n <<= (--length) * 8;
            sum += n;
        }
        return sum;
    }

    // 将整型转为字节数组
    public byte[] int2Bytes(int value, int length) {
        byte[] b = new byte[length];
        for (int k = 0; k < length; k++) {
            b[length - k - 1] = (byte) ((value >> 8 * k) & 0xff);
        }
        return b;
    }

    // 校验mid的范围
    public boolean isValidOfMid(int mId) {
        if (mId < MIN_MID_VALUE || mId > MAX_MID_VALUE) {
            return false;
        }
        return true;
    }

    // 将字节码转为16进制
    public String parseByte2Hex(byte[] buf) {
        if (null == buf) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    // 将字节码转为字符
    public String bytes2Str(byte[] bytes, int start, int len) {
        byte[] target = new byte[len];
        System.arraycopy(bytes, start, target, 0, len);
        return new String(target);
    }

}
