package wallf.basenencodings;

/**
 * Represents a Base-N data encoding. <br />
 * Defines the general properties and methods of BaseEncoding. <br />
 * Provides easy access to standard encodings of <a href="http://tools.ietf.org/rfc/rfc4648.txt">RFC 4648</a>.
 */
public abstract class BaseEncoding {


    /*
     *
     * static get properties of the standard encodings
     *
     */

    /**
     * Gets a standard encoding for the Base16 Data Encoding(RFC 4648).
     *
     * @see Base16Encoding
     */
    public static BaseEncoding getBase16() {
        return new Base16Encoding();
    }

    /**
     * Gets a standard encoding for the Base32Encoding Data Encoding(RFC 4648).
     *
     * @see Base32Encoding
     */
    public static BaseEncoding getBase32() {
        return new Base32Encoding();
    }

    /**
     * Gets a standard encoding for the Base32Encoding Data Encoding(RFC 4648) with Extended Hex Alphabet.
     *
     * @see Base32HexEncoding
     */
    public static BaseEncoding getBase32Hex() {
        return new Base32HexEncoding();
    }

    /**
     * Gets a standard encoding for the Base64 Data Encoding(RFC 4648).
     *
     * @see Base64Encoding
     */
    public static BaseEncoding getBase64() {
        return new Base64Encoding();
    }

    /**
     * Gets a standard encoding for the Base64 Data Encoding(RFC 4648) with URL and Filename Safe Alphabet.
     *
     * @see Base64SafeEncoding
     */
    public static BaseEncoding getBase64Safe() {
        return new Base64SafeEncoding();
    }


    /*
     *
     * abstract general properties
     *
     */

    /**
     * When overridden in a derived class, gets the human-readable description of the current encoding.
     */
    public abstract String getEncodingName();

    /**
     * When overridden in a derived class, gets the being used alphabet of the current encoding.
     */
    public abstract char[] getAlphabet();

    /**
     * When overridden in a derived class, gets a value indicating whether padding character is required of the current encoding.
     */
    public abstract boolean isPaddingRequired();

    /**
     * When overridden in a derived class, gets the being used padding character of the current encoding.
     */
    public abstract char getPaddingCharacter();


    /*
     *
     * virtual(could be overridden), easy accessibility
     *
     */

    /**
     * When overridden in a derived class, converts all the bytes in the specified byte array to its equivalent string representation that is encoded with base-n digits by current encoding.
     *
     * @param bytes The byte array containing the sequence of bytes to convert.
     * @return The string representation, in base-n, of contents of the specified byte array.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public String toBaseString(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("bytes is null");
        return new String(encodeWithoutArgumentsValidation(bytes, 0, bytes.length));
    }

    /**
     * When overridden in a derived class, converts all the bytes in the specified byte array to its equivalent string representation that is encoded with base-n digits by current encoding.
     *
     * @param bytes  The byte array containing the sequence of bytes to convert.
     * @param offset The index of the first byte to convert.
     * @param length The number of bytes to convert.
     * @return The string representation, in base-n, of contents of the specified byte array.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public String toBaseString(byte[] bytes, int offset, int length) {
        if (bytes == null)
            throw new IllegalArgumentException("bytes is null");
        if (!ArrayFunctions.validationInterval(bytes, offset, length))
            throw new IllegalArgumentException("offset and length can't reference an effective tuple of bytes");
        return new String(encodeWithoutArgumentsValidation(bytes, offset, length));
    }

    /**
     * When overridden in a derived class, converts all the characters in the specified string to its equivalent binary data representation that is decoded with base-n string by current encoding.
     *
     * @param s The string containing the characters to convert.
     * @return A byte array containing the results that is equivalent to s.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public byte[] fromBaseString(String s) {
        if (s == null)
            throw new IllegalArgumentException("s is null");
        return decodeWithoutArgumentsValidation(s.toCharArray(), 0, s.length());
    }

    /**
     * When overridden in a derived class, converts all the characters in the specified string to its equivalent binary data representation that is decoded with base-n string by current encoding. <br />
     * Safety version of fromBaseString(String), it ignores the execution of possible exceptions.
     *
     * @param s The string containing the characters to convert.
     * @return Returns data as byte[] if converted success, otherwise return null.
     * @throws IllegalArgumentException If s is null.
     * @see byte[] formBaseString(String)
     */
    public byte[] tryFromBaseString(String s) {
        if (s == null)
            throw new IllegalArgumentException("s is null");
        try {
            return decodeWithoutArgumentsValidation(s.toCharArray(), 0, s.length());
        } catch (RuntimeException e) {
            return null;
        }
    }


    /*
     *
     * virtual(could be overridden), typical encode methods
     *
     */

    /**
     * When overridden in a derived class, calculates the number of characters produced by encoding the sequence of bytes specified length from byte array.
     *
     * @param length The number of bytes to encode.
     * @return The number of characters produced by encoding the sequence of bytes specified length.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public int getEncodeCount(int length) {
        if (length < 0)
            throw new IllegalArgumentException("length is less than 0");
        return getEncodeCountWithoutArgumentsValidation(length);
    }

    /**
     * When overridden in a derived class, encodes all the bytes in the specified byte array into a set of characters.
     *
     * @param bytes The byte array containing the sequence of bytes to encode.
     * @return A character array containing the results of encoding the specified sequence of bytes.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public char[] encode(byte[] bytes) {
        if (bytes == null)
            throw new IllegalArgumentException("bytes is null");
        return encodeWithoutArgumentsValidation(bytes, 0, bytes.length);
    }

    /**
     * When overridden in a derived class, encodes all the bytes in the specified byte array into a set of characters.
     *
     * @param bytes  The byte array containing the sequence of bytes to encode.
     * @param offset The index of the first byte to encode.
     * @param length The number of bytes to encode.
     * @return A character array containing the results of encoding the specified sequence of bytes.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public char[] encode(byte[] bytes, int offset, int length) {
        if (bytes == null)
            throw new IllegalArgumentException("bytes is null");
        if (!ArrayFunctions.validationInterval(bytes, offset, length))
            throw new IllegalArgumentException("offset and length can't reference an effective tuple of bytes");
        return encodeWithoutArgumentsValidation(bytes, offset, length);
    }

    /**
     * When overridden in a derived class, encodes a sequence of bytes from the specified byte array into the specified character array.
     *
     * @param bytesIn   The byte array containing the sequence of bytes to encode.
     * @param offsetIn  The index of the first byte to encode.
     * @param lengthIn  The number of bytes to encode.
     * @param charsOut  The character array to contain the resulting set of characters.
     * @param offsetOut The index at which to start writing the resulting set of characters.
     * @return The actual number of characters written into chars.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public int encode(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut) {
        if (bytesIn == null)
            throw new NullPointerException("bytesIn is null");
        if (!ArrayFunctions.validationInterval(bytesIn, offsetIn, lengthIn))
            throw new IllegalArgumentException("offsetIn and lengthIn can't reference an effective tuple of bytesIn");
        if (charsOut == null)
            throw new NullPointerException("charsOut is null");
        if (!ArrayFunctions.validationInterval(charsOut, offsetOut))
            throw new IllegalArgumentException("offsetOut is not an index of charsOut");
        return encodeWithoutArgumentsValidation(bytesIn, offsetIn, lengthIn, charsOut, offsetOut);
    }


    /*
     *
     * virtual(could be overridden), typical decode methods
     *
     */

    /**
     * When overridden in a derived class, calculates the number of bytes produced by decoding a set of characters from the specified character array.
     *
     * @param chars  The character array containing the set of characters to decode.
     * @param offset The index of the first character to decode.
     * @param length The number of characters to decode.
     * @return The number of bytes produced by decoding the specified characters.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public int getDecodeCount(char[] chars, int offset, int length) {
        if (chars == null)
            throw new IllegalArgumentException("chars is null");
        if (!ArrayFunctions.validationInterval(chars, offset, length))
            throw new IllegalArgumentException("offset and length can't reference an effective tuple of chars");
        return getDecodeCountWithoutArgumentsValidation(chars, offset, length);
    }

    /**
     * When overridden in a derived class, decodes all the characters in the specified character array into a sequence of bytes.
     *
     * @param chars The character array containing the characters to decode.
     * @return A byte array containing the results of decoding the specified set of characters.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public byte[] decode(char[] chars) {
        if (chars == null)
            throw new IllegalArgumentException("chars is null");
        return decodeWithoutArgumentsValidation(chars, 0, chars.length);
    }

    /**
     * When overridden in a derived class, decodes a set of characters from the specified character array into a sequence of bytes.
     *
     * @param chars  The character array containing the set of characters to decode.
     * @param offset The index of the first character to decode.
     * @param length The number of characters to decode.
     * @return A byte array containing the results of decoding the specified set of characters.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public byte[] decode(char[] chars, int offset, int length) {
        if (chars == null)
            throw new IllegalArgumentException("chars is null");
        if (!ArrayFunctions.validationInterval(chars, offset, length))
            throw new IllegalArgumentException("offset and length can't reference an effective tuple of chars");
        return decodeWithoutArgumentsValidation(chars, offset, length);
    }

    /**
     * When overridden in a derived class, decodes a set of characters from the specified character array into the specified byte array.
     *
     * @param charsIn   The character array containing the set of characters to decode.
     * @param offsetIn  The index of the first character to decode.
     * @param lengthIn  The number of characters to decode.
     * @param bytesOut  The byte array to contain the resulting sequence of bytes.
     * @param offsetOut The index at which to start writing the resulting sequence of bytes.
     * @return The actual number of bytes written into bytes.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public int decode(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut) {
        if (charsIn == null)
            throw new IllegalArgumentException("charsIn is null");
        if (!ArrayFunctions.validationInterval(charsIn, offsetIn, lengthIn))
            throw new IllegalArgumentException("offsetIn and lengthIn can't reference an effective tuple of charsIn");
        if (bytesOut == null)
            throw new IllegalArgumentException("bytesOut is null");
        if (!ArrayFunctions.validationInterval(bytesOut, offsetOut))
            throw new IllegalArgumentException("offsetOut is not an index of offsetOut");
        return decodeWithoutArgumentsValidation(charsIn, offsetIn, lengthIn, bytesOut, offsetOut);
    }


    /*
     *
     * virtual(could be overridden), utility methods
     *
     */

    /**
     * When overridden in a derived class, gets a value indicating whether a set of characters from the specified character array is actually valid by current encoding.
     *
     * @param chars  The character array containing the set of characters to validate.
     * @param offset The index of the first character to validate.
     * @param length The number of characters to validate.
     * @return Returns true if the specified character array is valid.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public boolean isValidBaseSequence(char[] chars, int offset, int length) {
        if (chars == null)
            throw new IllegalArgumentException("chars is null");
        if (!ArrayFunctions.validationInterval(chars, offset, length))
            throw new IllegalArgumentException("offset and length can't reference an effective tuple of chars");
        return isValidBaseSequenceWithoutArgumentsValidation(chars, offset, length);
    }

    /**
     * When overridden in a derived class, gets a value indicating whether a set of characters from the specified string is actually valid by current encoding.
     *
     * @param s The string containing the set of characters to validate.
     * @return Returns true if the specified character array is valid.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public boolean isValidBaseString(String s) {
        if (s == null)
            throw new IllegalArgumentException("s is null");
        return isValidBaseSequenceWithoutArgumentsValidation(s.toCharArray(), 0, s.length());
    }


    /*
     *
     * abstract, core methods
     *
     */

    /**
     * When overridden in a derived class, calculates the number of characters produced by encoding the sequence of bytes specified length from byte array. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param length The number of bytes to encode.
     * @return The number of characters produced by encoding the sequence of bytes specified length.
     */
    protected abstract int getEncodeCountWithoutArgumentsValidation(int length);

    /**
     * When overridden in a derived class, encodes all the bytes in the specified byte array into a set of characters. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param bytes  The byte array containing the sequence of bytes to encode.
     * @param offset The index of the first byte to encode.
     * @param length The number of bytes to encode.
     * @return A character array containing the results of encoding the specified sequence of bytes.
     */
    protected abstract char[] encodeWithoutArgumentsValidation(byte[] bytes, int offset, int length);

    /**
     * When overridden in a derived class, encodes a sequence of bytes from the specified byte array into the specified character array. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param bytesIn   The byte array containing the sequence of bytes to encode.
     * @param offsetIn  The index of the first byte to encode.
     * @param lengthIn  The number of bytes to encode.
     * @param charsOut  The character array to contain the resulting set of characters.
     * @param offsetOut The index at which to start writing the resulting set of characters.
     * @return The actual number of characters written into chars.
     */
    protected abstract int encodeWithoutArgumentsValidation(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut);

    /**
     * When overridden in a derived class, calculates the number of bytes produced by decoding a set of characters from the specified character array. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param chars  The character array containing the set of characters to decode.
     * @param offset The index of the first character to decode.
     * @param length The number of characters to decode.
     * @return The number of bytes produced by decoding the specified characters.
     */
    protected abstract int getDecodeCountWithoutArgumentsValidation(char[] chars, int offset, int length);

    /**
     * When overridden in a derived class, decodes a set of characters from the specified character array into a sequence of bytes. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param chars  The character array containing the set of characters to decode.
     * @param offset The index of the first character to decode.
     * @param length The number of characters to decode.
     * @return A byte array containing the results of decoding the specified set of characters.
     */
    protected abstract byte[] decodeWithoutArgumentsValidation(char[] chars, int offset, int length);

    /**
     * When overridden in a derived class, decodes a set of characters from the specified character array into the specified byte array. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param charsIn   The character array containing the set of characters to decode.
     * @param offsetIn  The index of the first character to decode.
     * @param lengthIn  The number of characters to decode.
     * @param bytesOut  The byte array to contain the resulting sequence of bytes.
     * @param offsetOut The index at which to start writing the resulting sequence of bytes.
     * @return The actual number of bytes written into bytes.
     */
    protected abstract int decodeWithoutArgumentsValidation(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut);

    /**
     * When overridden in a derived class, gets a value indicating whether a set of characters from the specified character array is actually valid by current encoding. <br />
     * No need to verify the correctness of the arguments.
     *
     * @param chars  The character array containing the set of characters to validate.
     * @param offset The index of the first character to validate.
     * @param length The number of characters to validate.
     * @return Returns true if the specified character array is valid.
     */
    protected abstract boolean isValidBaseSequenceWithoutArgumentsValidation(char[] chars, int offset, int length);

}