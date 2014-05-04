package wallf.basenencodings;

import java.util.HashSet;
import java.util.Set;


final class ArrayFunctions {

    public static boolean validationInterval(byte[] arr, int offset) {
        return offset >= 0 && offset <= arr.length;
    }

    public static boolean validationInterval(char[] arr, int offset) {
        return offset >= 0 && offset <= arr.length;
    }

    public static boolean validationInterval(byte[] arr, int offset, int length) {
        return offset >= 0 && length >= 0 && offset + length <= arr.length;
    }

    public static boolean validationInterval(char[] arr, int offset, int length) {
        return offset >= 0 && length >= 0 && offset + length <= arr.length;
    }

    public static boolean isArrayDuplicate(char[] chars) {
        Set<Character> set = new HashSet<Character>(chars.length);
        for (char c : chars) {
            if (set.contains(c))
                return true;
            else
                set.add(c);
        }
        return false;
    }

    public static boolean isArrayContains(char[] chars, char target) {
        for (char c : chars) {
            if (c == target)
                return true;
        }
        return false;
    }

}

final class TypeWrapper<T> {

    private T v;

    public T getValue() {
        return v;
    }

    public void setValue(T v) {
        this.v = v;
    }

    public TypeWrapper() {
    }

    public TypeWrapper(T v) {
        this.v = v;
    }
}