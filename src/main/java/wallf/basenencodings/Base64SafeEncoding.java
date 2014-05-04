package wallf.basenencodings;

/**
 * Defines a Base64 encoding with URL and Filename Safe Alphabet.
 */
public class Base64SafeEncoding extends Base64Encoding {

    /**
     * Standard Alphabet.
     */
    public static final String STANDARD_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    /**
     * Standard Padding.
     */
    public static final char STANDARD_PADDING = '=';
    /**
     * Default Encoding Name.
     */
    public static final String DEFAULT_NAME = "Standard Base64 Encoding with URL and Filename Safe Alphabet";

    /**
     * Initializes a new instance that is a standard Base64 encoding(<a href="http://tools.ietf.org/rfc/rfc4648.txt">RFC 4648</a>) with URL and Filename Safe Alphabet.
     */
    public Base64SafeEncoding() {
        super(STANDARD_ALPHABET.toCharArray(), STANDARD_PADDING, DEFAULT_NAME, false);
    }

}