package wallf.basenencodings.simple;

import wallf.basenencodings.BaseEncoding;
import java.util.Arrays;

public class Client {

    // see http://tools.ietf.org/html/rfc4648#section-10
    static final String[] TEST_VECTORS = {"", "f", "fo", "foo", "foob", "fooba", "foobar"};

    public static void main(String args[]) {
        BaseEncoding b64 = BaseEncoding.getBase64(), b64Safe = BaseEncoding.getBase64Safe(), b32 = BaseEncoding.getBase32(), b32Hex = BaseEncoding.getBase32Hex(), b16 = BaseEncoding.getBase16();
        for (String vector : TEST_VECTORS) {
            usage1("Base64", b64, vector);
            usage2("Base64Safe", b64Safe, vector);
            usage1("Base32", b32, vector);
            usage3("Base32Hex", b32Hex, vector);
            usage2("Base16", b16, vector);
        }
    }

    // ========= USAGE 1 =========
    // String BaseEncoding.toBaseString(byte[] bytes)
    // byte[] BaseEncoding.fromBaseString(String s)
    static void usage1(String testName, BaseEncoding encoding, String testVector) {
        byte[] origin = testVector.getBytes();
        String baseString = encoding.toBaseString(origin);
        byte[] bytes = encoding.fromBaseString(baseString);
        System.out.println("[" + testName + "]\tVector: " + testVector + "\tBaseString: "
                + baseString + "\t" + (arrayEquals(origin, bytes) ? "Success" : "failed"));
    }

    // ========= USAGE 2 =========
    // char[] BaseEncoding.encode(byte[] bytes, int offset, int length)
    // byte[] BaseEncoding.decode(char[] chars, int offset, int length)
    static void usage2(String testName, BaseEncoding encoding, String testVector) {
        byte[] origin = testVector.getBytes();
        char[] baseChars = encoding.encode(origin, 0, origin.length);
        byte[] bytes = encoding.decode(baseChars, 0, baseChars.length);
        System.out.println("[" + testName + "]\tVector: " + testVector + "\tBaseString: "
                + new String(baseChars) + "\t" + (arrayEquals(origin, bytes) ? "Success" : "failed"));
    }

    // ========= USAGE 3 =========
    // int BaseEncoding.getEncodeCount(int length)
    // int BaseEncoding.encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut)
    // int BaseEncoding.getDecodeCount(char[] chars, int offset, int length)
    // int BaseEncoding.decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut)
    static void usage3(String testName, BaseEncoding encoding, String testVector) {
        int encodeOffset = 99, decodeOffset = 199;
        byte[] origin = testVector.getBytes();
        byte[] originBuffer = wrapArray(origin, 33, 44);
        char[] baseCharsBuffer = new char[encoding.getEncodeCount(origin.length) + encodeOffset * 2];
        // == encode
        int encodeNum = encoding.encode(originBuffer, 33, origin.length, baseCharsBuffer, encodeOffset);
        // ==
        byte[] binBuffer = new byte[encoding.getDecodeCount(baseCharsBuffer, encodeOffset, encodeNum) + decodeOffset * 2];
        // == decode
        int decodeNum = encoding.decode(baseCharsBuffer, encodeOffset, encodeNum, binBuffer, decodeOffset);
        // ====== result
        char[] baseChars = subArray(baseCharsBuffer, encodeOffset, encodeNum);
        byte[] binData = subArray(binBuffer, decodeOffset, decodeNum);
        // == output
        System.out.println("[" + testName + "]\tVector: " + testVector + "\tBaseString: "
                + new String(baseChars) + "\t" + (arrayEquals(origin, binData) ? "Success" : "failed"));
    }

    static boolean arrayEquals(byte[] arr1, byte[] arr2) {
        if (arr1 == null || arr2 == null) return false;
        if (arr1.length != arr2.length) return false;
        for (int i = 0; i < arr1.length; i++)
            if (arr1[i] != arr2[i])
                return false;
        return true;
    }

    static byte[] subArray(byte[] arr, int index, int count) {
        return Arrays.copyOfRange(arr, index, index + count);
    }

    static char[] subArray(char[] arr, int index, int count) {
        return Arrays.copyOfRange(arr, index, index + count);
    }

    static byte[] wrapArray(byte[] arr, int leftPadNum, int rightPadNum) {
        byte[] r = new byte[arr.length + leftPadNum + rightPadNum];
        int index = 0;
        for (int i = leftPadNum, ir = leftPadNum + arr.length; i < ir; i++)
            r[i] = arr[index++];
        return r;
    }
}