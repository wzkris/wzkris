package com.wzkris.common.core.dynamicCompilation.bytecode;

/**
 * Class文件修改器
 *
 * @author thinglinks
 * @date 2022-07-04
 */
public class ClassModifier {
    private static final int CONSTANT_POOL_COUNT_INDEX = 8;
    private static final int CONSTANT_Utf8_info = 1;
    private static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};
    private static final int u1 = 1;
    private static final int u2 = 2;
    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    public static int bytes2Int(byte[] b, int start, int len) {
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum = n + sum;
        }
        return sum;
    }

    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    public static byte[] bytesReplace(byte[] originalBytes, int offset, int len, byte[] replaceBytes) {
        byte[] newBytes = new byte[originalBytes.length + (replaceBytes.length - len)];
        System.arraycopy(originalBytes, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
        System.arraycopy(originalBytes, offset + len, newBytes, offset + replaceBytes.length, originalBytes.length - offset - len);
        return newBytes;
    }

    public byte[] modifyUTF8Constant4Class(Class<?> oldClass, Class<?> newClass) {
        return modifyUTF8Constant4ClassPath(oldClass.getName(), newClass.getName());
    }

    public byte[] modifyUTF8Constant4ClassPath(String oldClassName, String newClassName) {
        String oldReference = oldClassName.replace(".", "/");
        String newReference = newClassName.replace(".", "/");
        return modifyUTF8Constant4Reference(oldReference, newReference);
    }

    /**
     * 修改字符串常量池的符号引用
     */
    public byte[] modifyUTF8Constant4Reference(String oldReference, String newReference) {
        int cpc = getConstantPoolCount();
        int offset = CONSTANT_POOL_COUNT_INDEX + u2;
        for (int i = 0; i < cpc; i++) {
            int tag = bytes2Int(classByte, offset, u1);
            if (tag == CONSTANT_Utf8_info) {
                int len = bytes2Int(classByte, offset + u1, u2);
                offset += (u1 + u2);
                String str = new String(classByte, offset, len);
                if (str.equalsIgnoreCase(oldReference)) {
                    byte[] strBytes = newReference.getBytes();
                    byte[] strLen = int2Bytes(newReference.length(), u2);
                    classByte = bytesReplace(classByte, offset - u2, u2, strLen);
                    //这里不只是替换，应该是填充，把新的字节数据插入到原来的位置，然后存在后面字节的向前或者先后移动
                    classByte = bytesReplace(classByte, offset, len, strBytes);
                    return classByte;
                }
                else {
                    offset += len;
                }
            }
            else {
                offset += CONSTANT_ITEM_LENGTH[tag];
            }
        }

        //没有找到需要注入的引用字符串，直接返回原始数据
        return classByte;
    }

    public int getConstantPoolCount() {
        return bytes2Int(classByte, CONSTANT_POOL_COUNT_INDEX, u2);
    }
}
