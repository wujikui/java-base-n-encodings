package wallf.basenencodings;

/**
 * Represents a Base16 encoding. <br />
 * Default constructor will create a standard Base16 encoding(RFC 4648).
 */
public class Base16Encoding extends BaseEncoding {

    /**
     * Standard Alphabet.
     */
    public static final String STANDARD_ALPHABET = "0123456789ABCDEF";
    /**
     * Default Encoding Name.
     */
    public static final String DEFAULT_NAME = "Standard Base16 Encoding";

    private final char[] alphabet;
    private final String encodingName;
    private final Base16 b;

    /**
     * Initializes a new instance that is a standard Base16 encoding(<a href="http://tools.ietf.org/rfc/rfc4648.txt">RFC 4648</a>).
     */
    public Base16Encoding() {
        this(STANDARD_ALPHABET.toCharArray(), DEFAULT_NAME, false);
    }

    /**
     * Initializes a new instance of the Base16Encoding class. Parameters specify the alphabet of encoding.
     *
     * @param alphabet Alphabet for current encoding.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public Base16Encoding(char[] alphabet) {
        this(alphabet, "Customized Base16 Encoding", true);
    }

    /**
     * Initializes a new instance of the Base16Encoding class. Parameters specify the alphabet and the name of encoding.
     *
     * @param alphabet     Alphabet for current encoding.
     * @param encodingName Name for current encoding.
     * @throws IllegalArgumentException Arguments error, see the source code.
     */
    public Base16Encoding(char[] alphabet, String encodingName) {
        this(alphabet, encodingName, true);
    }

    Base16Encoding(char[] alphabet, String encodingName, boolean verify) {
        if (verify) {
            if (alphabet == null)
                throw new IllegalArgumentException("alphabet is null");
            if (encodingName == null)
                throw new IllegalArgumentException("encodingName is null");
            if (alphabet.length != 16)
                throw new IllegalArgumentException("size of alphabet is not 16");
            if (ArrayFunctions.isArrayDuplicate(alphabet))
                throw new IllegalArgumentException("alphabet contains duplicated items");
        }
        this.alphabet = alphabet.clone();
        this.encodingName = encodingName;
        this.b = new Base16(this.alphabet);
    }


    /**
     * Gets the human-readable description of the current encoding.
     */
    @Override
    public String getEncodingName() {
        return encodingName;
    }

    /**
     * Gets the being used alphabet of the current encoding.
     */
    @Override
    public char[] getAlphabet() {
        return alphabet.clone();
    }

    /**
     * Return value is always false, because of padding is not required for Base16 Encoding.
     */
    @Override
    public boolean isPaddingRequired() {
        return false;
    }

    /**
     * Return value is always default character 0, because of padding is not required for Base16 Encoding.
     */
    @Override
    public char getPaddingCharacter() {
        return 0;
    }

    /**
     * @see int BaseEncoding.getEncodeCountWithoutArgumentsValidation(int length)
     */
    @Override
    protected int getEncodeCountWithoutArgumentsValidation(int length) {
        return b.encodeSize(length);
    }

    /**
     * @see char[] BaseEncoding.encodeWithoutArgumentsValidation(byte[] bytes, int offset, int length)
     */
    @Override
    protected char[] encodeWithoutArgumentsValidation(byte[] bytes, int offset, int length) {
        char[] r = new char[b.encodeSize(length)];
        b.encode(bytes, offset, length, r, 0, r.length);
        return r;
    }

    /**
     * @see int BaseEncoding.encodeWithoutArgumentsValidation(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut)
     */
    @Override
    protected int encodeWithoutArgumentsValidation(byte[] bytesIn, int offsetIn, int lengthIn, char[] charsOut, int offsetOut) {
        return b.encode(bytesIn, offsetIn, lengthIn, charsOut, offsetOut);
    }

    /**
     * @see int BaseEncoding.getDecodeCountWithoutArgumentsValidation(char[] chars, int offset, int length)
     */
    @Override
    protected int getDecodeCountWithoutArgumentsValidation(char[] chars, int offset, int length) {
        return b.decodeSize(length);
    }

    /**
     * @see byte[] BaseEncoding.decodeWithoutArgumentsValidation(char[] chars, int offset, int length)
     */
    @Override
    protected byte[] decodeWithoutArgumentsValidation(char[] chars, int offset, int length) {
        byte[] r = new byte[b.decodeSize(length)];
        b.decode(chars, offset, length, r, 0, r.length);
        return r;
    }

    /**
     * @see int BaseEncoding.decodeWithoutArgumentsValidation(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut)
     */
    @Override
    protected int decodeWithoutArgumentsValidation(char[] charsIn, int offsetIn, int lengthIn, byte[] bytesOut, int offsetOut) {
        return b.decode(charsIn, offsetIn, lengthIn, bytesOut, offsetOut);
    }

    /**
     * @see boolean BaseEncoding.isValidBaseSequenceWithoutArgumentsValidation(char[] chars, int offset, int length)
     */
    @Override
    protected boolean isValidBaseSequenceWithoutArgumentsValidation(char[] chars, int offset, int length) {
        return b.isValidBaseSequence(chars, offset, length);
    }
}