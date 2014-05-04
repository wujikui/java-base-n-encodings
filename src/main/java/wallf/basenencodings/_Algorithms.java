package wallf.basenencodings;

import java.util.HashMap;
import java.util.Map;


final class Base16 {
    private final char[] charMap;
    private final Map<Character, Integer> indexMap;

    public Base16(char[] alphabet) {
        this.charMap = alphabet;
        this.indexMap = new HashMap<Character, Integer>(16);
        for (int i = 0; i < 16; i++)
            indexMap.put(alphabet[i], i);
    }

    public int encodeSize(int length) {
        return length * 2;
    }

    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut) {
        int lengthOut = encodeSize(lengthIn);
        if (charsOut.length - offsetOut < lengthOut)
            throw new IllegalArgumentException("output sequence does not have enough capacity");
        return encode(bytesIn, offsetIn, lengthIn, charsOut, offsetOut, lengthOut);
    }

    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut, int lengthOut) {
        // ===========================
        //         [1               ]
        // 1:{xxxx 0123} 2:{xxxx 4567}
        // ===========================
        if (lengthIn == 0) return 0;
        int boundIn = offsetIn + lengthIn;
        while (offsetIn != boundIn) {
            int v = (int) bytesIn[offsetIn++] & 0xFF;
            charsOut[offsetOut++] = charMap[v >> 4];
            charsOut[offsetOut++] = charMap[v & 0x0F];
        }
        return lengthOut;
    }

    public int decodeSize(int length) {
        if (length % 2 != 0)
            throw new IllegalArgumentException("input sequence is not a valid base sequence");
        return length / 2;
    }

    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut) {
        int lengthOut = decodeSize(lengthIn);
        if (bytesOut.length - offsetOut < lengthOut)
            throw new IllegalArgumentException("output sequence does not have enough capacity");
        return decode(charsIn, offsetIn, lengthIn, bytesOut, offsetOut, lengthOut);
    }

    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut, int lengthOut) {
        // ===========================
        // 1:{xxxx 0123} 2:{xxxx 4567}
        //         [1               ]
        // ===========================
        if (lengthIn == 0) return 0;
        int boundIn = offsetIn + lengthIn;
        while (offsetIn != boundIn) {
            char ca = charsIn[offsetIn++], cb = charsIn[offsetIn++];
            if (!indexMap.containsKey(ca) || !indexMap.containsKey(cb))
                throw new IllegalArgumentException("input sequence is not a valid base sequence");
            int ia = indexMap.get(ca), ib = indexMap.get(cb);
            bytesOut[offsetOut++] = (byte) (ia << 4 | ib);
        }
        return lengthOut;
    }

    public boolean isValidBaseSequence(char[] chars, int offset, int length) {
        if (length % 2 != 0) return false;
        int bound = offset + length;
        while (offset != bound) {
            if (!indexMap.containsKey(chars[offset++]))
                return false;
        }
        return true;
    }
}


final class Base32 {

    private final char[] charMap;
    private final Map<Character, Integer> indexMap;
    private final char paddingChar;

    public Base32(char[] alphabet, char padding) {
        this.charMap = alphabet;
        this.indexMap = new HashMap<Character, Integer>(32);
        for (int i = 0; i < 32; i++)
            indexMap.put(alphabet[i], i);
        this.paddingChar = padding;
    }

    public int encodeSize(int length) {
        return (int) Math.ceil(length / 5f) * 8;
    }

    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut) {
        int lengthOut = encodeSize(lengthIn);
        if (charsOut.length - offsetOut < lengthOut)
            throw new IllegalArgumentException("output sequence does not have enough capacity");
        return encode(bytesIn, offsetIn, lengthIn, charsOut, offsetOut, lengthOut);

    }

    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut, int lengthOut) {
        // ===============================================================================================================
        //       [1               ][2                      ] [3              ][4                       ][5              ]
        // 1:{xxx0 1234} 2:{xxx5 6701} 3:{xxx2 3456} 4:{xxx7 0123} 5:{xxx4 5670} 6:{xxx1 2345} 7:{xxx6 7012} 8:{xxx3 4567}
        // ===============================================================================================================
        if (lengthIn == 0) return 0;
        int last = 5, temp = 0, boundIn = offsetIn + lengthIn, boundOut = offsetOut + lengthOut;
        while (offsetIn != boundIn) {
            int v = (int) bytesIn[offsetIn++] & 0xFF;
            charsOut[offsetOut++] = charMap[temp | (v >> (8 - last))];
            if (last <= 3) {
                charsOut[offsetOut++] = charMap[(v >> (3 - last)) & 0x1F];
                last += 5;
            }
            temp = (byte) ((v << (last = last - 3)) & 0x1F);
        }
        if (offsetOut != boundOut)
            charsOut[offsetOut++] = charMap[temp];
        while (offsetOut < boundOut)
            charsOut[offsetOut++] = paddingChar;
        return lengthOut;
    }

    public int decodeSize(char[] chars, int offset, int length, TypeWrapper<Integer> paddingNumWrapper) {
        int paddingNum = 0;
        if (length == 0) {
            paddingNumWrapper.setValue(0);
            return 0;
        }
        if (length % 4 != 0) throw new IllegalArgumentException("input sequence is not a valid base sequence");
        int lastIndex = offset + length - 1;
        for (int ir = offset + length, i = ir - 7; i < ir; i++) {
            if (chars[i] == paddingChar) {
                paddingNum = ir - i;
                break;
            }
        }
        paddingNumWrapper.setValue(paddingNum);
        return (length - paddingNum) / 8 * 5 + PADDING_VALUES_NUM_MAP[paddingNum];
    }

    private static final int[] PADDING_VALUES_NUM_MAP = {0, 4, 0, 3, 2, 0, 1};

    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut) {
        TypeWrapper<Integer> paddingNumWrapper = new TypeWrapper<Integer>();
        int lengthOut = decodeSize(charsIn, offsetIn, lengthIn, paddingNumWrapper);
        if (bytesOut.length - offsetOut < lengthOut)
            throw new IllegalArgumentException("output sequence does not have enough capacity");
        return decode(charsIn, offsetIn, lengthIn, bytesOut, offsetOut, lengthOut, paddingNumWrapper.getValue());
    }

    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut, int lengthOut, int paddingNum) {
        // ===============================================================================================================
        // 1:{xxx0 1234} 2:{xxx5 6701} 3:{xxx2 3456} 4:{xxx7 0123} 5:{xxx4 5670} 6:{xxx1 2345} 7:{xxx6 7012} 8:{xxx3 4567}
        //       [1               ][2                      ] [3              ][4                       ][5              ]
        // ===============================================================================================================
        if (lengthIn == 0) return 0;
        int remain = 8, temp = 0, boundIn = offsetIn + lengthIn - paddingNum;
        while (offsetIn != boundIn) {
            char c = charsIn[offsetIn++];
            if (!indexMap.containsKey(c))
                throw new IllegalArgumentException("input sequence is not a valid base sequence");
            int v = indexMap.get(c);
            if (remain > 5) {
                temp = temp | (v << (remain -= 5));
            } else {
                bytesOut[offsetOut++] = (byte) (temp | (v >> (5 - remain)));
                temp = v << (remain += 3);
            }
        }
        return lengthOut;
    }

    public boolean isValidBaseSequence(char[] chars, int offset, int length) {
        if (length == 0) return true;
        if (length % 8 != 0) return false;
        int bound = offset + length;
        boolean findChar = false;
        for (int i = bound - 1, ir = bound - 7; i > ir; i--) {
            char c = chars[i];
            if (c == paddingChar) {
                if (findChar)
                    return false;
            } else {
                if (!indexMap.containsKey(c))
                    return false;
                findChar = true;
            }
        }
        for (int i = bound - 7; i >= offset; i--) {
            if (!indexMap.containsKey(chars[i]))
                return false;
        }
        return true;
    }
}


final class Base64 {
    private final char[] charMap;
    private final Map<Character, Integer> indexMap;
    private final char paddingChar;

    public Base64(char[] alphabet, char padding) {
        this.charMap = alphabet;
        this.indexMap = new HashMap<Character, Integer>(64);
        for (int i = 0; i < 64; i++)
            indexMap.put(alphabet[i], i);
        this.paddingChar = padding;
    }

    public int encodeSize(int length) {
        return (int) Math.ceil(length / 3f) * 4;
    }

    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut) {
        int lengthOut = encodeSize(lengthIn);
        if (charsOut.length - offsetOut < lengthOut)
            throw new IllegalArgumentException("output sequence does not have enough capacity");
        return encode(bytesIn, offsetIn, lengthIn, charsOut, offsetOut, lengthOut);
    }

    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut, int lengthOut) {
        // =======================================================
        //      [1             ] [2             ][3             ]
        // 1:{xx01 2345} 2:{xx67 0123} 3:{xx45 6701} 4:{xx23 4567}
        // =======================================================
        if (lengthIn == 0) return 0;
        int last = 6, temp = 0, boundIn = offsetIn + lengthIn, boundOut = offsetOut + lengthOut;
        while (offsetIn != boundIn) {
            int v = (int) bytesIn[offsetIn++] & 0xFF;
            charsOut[offsetOut++] = charMap[temp | (v >> (8 - last))];
            if (last <= 2) {
                charsOut[offsetOut++] = charMap[(v >> (2 - last)) & 0x3F];
                last += 6;
            }
            temp = (v << (last = last - 2)) & 0x3F;
        }
        if (offsetOut != boundOut)
            charsOut[offsetOut++] = charMap[temp];
        while (offsetOut < boundOut)
            charsOut[offsetOut++] = paddingChar;
        return lengthOut;
    }

    public int decodeSize(char[] chars, int offset, int length, TypeWrapper<Integer> paddingNumWrapper) {
        int paddingNum = 0;
        if (length == 0) {
            paddingNumWrapper.setValue(0);
            return 0;
        }
        if (length % 4 != 0) throw new IllegalArgumentException("input sequence is not a valid base sequence");
        int lastIndex = offset + length - 1;
        if (chars[lastIndex - 1] == paddingChar) paddingNum = 2;
        else if (chars[lastIndex] == paddingChar) paddingNum = 1;
        paddingNumWrapper.setValue(paddingNum);
        return (length - paddingNum) / 4 * 3 + PADDING_VALUES_NUM_MAP[paddingNum];
    }

    private static final int[] PADDING_VALUES_NUM_MAP = {0, 2, 1};

    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut) {
        TypeWrapper<Integer> paddingNumWrapper = new TypeWrapper<Integer>();
        int lengthOut = decodeSize(charsIn, offsetIn, lengthIn, paddingNumWrapper);
        if (bytesOut.length - offsetOut < lengthOut)
            throw new IllegalArgumentException("output sequence does not have enough capacity");
        return decode(charsIn, offsetIn, lengthIn, bytesOut, offsetOut, lengthOut, paddingNumWrapper.getValue());
    }

    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut, int lengthOut, int paddingNum) {
        // =======================================================
        // 1:{xx01 2345} 2:{xx67 0123} 3:{xx45 6701} 4:{xx23 4567}
        //      [1             ] [2             ][3             ]
        // =======================================================
        if (lengthIn == 0) return 0;
        int remain = 8, temp = 0, boundIn = offsetIn + lengthIn - paddingNum;
        while (offsetIn != boundIn) {
            char c = charsIn[offsetIn++];
            if (!indexMap.containsKey(c))
                throw new IllegalArgumentException("input sequence is not a valid base sequence");
            int v = indexMap.get(c);
            if (remain > 6) {
                temp = temp | (v << (remain -= 6));
            } else {
                bytesOut[offsetOut++] = (byte) (temp | (v >> (6 - remain)));
                temp = v << (remain += 2);
            }
        }
        return lengthOut;
    }

    public boolean isValidBaseSequence(char[] chars, int offset, int length) {
        if (length == 0) return true;
        if (length % 4 != 0) return false;
        int bound = offset + length;
        boolean findChar = false;
        for (int i = bound - 1, ir = bound - 3; i > ir; i--) {
            char c = chars[i];
            if (c == paddingChar) {
                if (findChar)
                    return false;
            } else {
                if (!indexMap.containsKey(c))
                    return false;
                findChar = true;
            }
        }
        for (int i = bound - 3; i >= offset; i--) {
            if (!indexMap.containsKey(chars[i]))
                return false;
        }
        return true;
    }
}