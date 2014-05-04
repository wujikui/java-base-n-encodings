package wallf.basenencodings;

/**
 * Defines a Base32 encoding with Extended Hex Alphabet.
 */
public final class Base32HexEncoding extends Base32Encoding {

    /**
     * Standard Alphabet.
     */
    public static final String STANDARD_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUV";
    /**
     * Standard Padding.
     */
    public static final char STANDARD_PADDING = '=';
    /**
     * Default Encoding Name.
     */
    public static final String DEFAULT_NAME = "Standard Base32 Encoding with Extended Hex Alphabet";

    /**
     * Initializes a new instance that is a standard Base32 encoding(<a href="http://tools.ietf.org/rfc/rfc4648.txt">RFC 4648</a>) with Extended Hex Alphabet.
     */
    public Base32HexEncoding() {
        super(STANDARD_ALPHABET.toCharArray(), STANDARD_PADDING, DEFAULT_NAME, false);
    }

}