java-base-n-encodings
==================

java-base-n-encodings is a general base16, base32, base64 encodings library for Java6+, which is according to [RFC 4648][1].

> It's a migration project of the [BaseNEncodings.Net][2].

Features
--------
- Represents a **general** Base-N data encoding as an abstract class.
- Implements **standard** encodings of **RFC 4648**.
    - Base 64 Encoding
    - Base 64 Encoding with URL and Filename Safe Alphabet
    - Base 32 Encoding
    - Base 32 Encoding with Extended Hex Alphabet
    - Base 16 Encoding
- Supports custom alphabet and padding for your Base-N Encoding.
- Includes the **simple** and informal **benchmark** subprojects.

Installation
------------
- Download the [package][3] (**recommend**) and depends it manually.

    ```
    java-base-n-encodings-1.0.0.pom
    java-base-n-encodings-1.0.0.jar
    java-base-n-encodings-1.0.0-javadoc.jar
    java-base-n-encodings-1.0.0-sources.jar
    ```

- Using [Maven][4] (not recommend).

    releases, **osc thridparty repositories**
    
    http://maven.oschina.net/content/repositories/thirdparty/wallf/java-base-n-encodings
    
    ```
    MavenURL: http://maven.oschina.net/content/repositories/thirdparty
    GroupId: wallf
    ArtifactId: java-base-n-encodings
    Version: 1.0.0
    Packaging: JAR
    ```
    
    **----NOTICE----**
    
    For some reasons, this project is published in [The OSChina ThirdParty Repositories][5] instead of [The Central Repository][6].
    
    Does **not** depends this library in your pom.xml directly, except you are using the mirrors of  OSC.
    
    **Depending on the jar file or your private maven repositories would be a better choice.**


Basic Usage
-----------
1. Adds a dependency of the jar file in you project.
2. Imports the wallf.basenencodings **package**.

        import wallf.basenencodings.BaseEncoding;

3. Gets **instance** of encodings.

        // standard encoding(RFC 4648)
        BaseEncoding encoding = BaseEncoding.getBase64();
        // custom encoding
        char[] alphabet =  {...};
        char padding = '.';
        BaseEncoding encoding = new Base64Encoding(alphabet, padding, "custom encoding");
        
4. **Converts** by the methods To/FromBaseString, Encode, Decode.

        BaseEncoding encoding = ...;
        byte[] bin = new byte[1024];
        String baseString = encoding.toBaseString(bin);
        byte[] data = encoding.fromBaseString(baseString);
        // bin and data contains the same elements

Documentation, Simple and Benchmark
------------------------------------
- Documentation is included in the javadoc.jar file.
- Repository includes the [simple][7] and [benchmark][8] subprojects.


  [1]: http://tools.ietf.org/html/rfc4648
  [2]: https://github.com/wallf/BaseNEncodings.Net
  [3]: https://github.com/wallf/java-base-n-encodings/releases/download/v1.0.0/java-base-n-encodings-1.0.0.zip
  [4]: http://maven.apache.org/
  [5]: http://maven.oschina.net/
  [6]: http://search.maven.org/
  [7]: https://github.com/wallf/java-base-n-encodings/tree/master/simple
  [8]: https://github.com/wallf/java-base-n-encodings/tree/master/benchmark