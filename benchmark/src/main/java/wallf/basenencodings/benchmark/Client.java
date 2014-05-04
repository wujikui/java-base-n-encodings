package wallf.basenencodings.benchmark;

import wallf.basenencodings.BaseEncoding;
import java.io.IOException;
import java.util.Random;

public class Client {

    public static void main(String args[]) {
        byte[][] source = { // 1M bytes
                new byte[0x10]
                , new byte[0x41]
                , new byte[0x100]
                , new byte[0x430]
                , new byte[0x1050]
                , new byte[0x4107]
                , new byte[0x17090]
                , new byte[0x40A0A]
                , new byte[0xA2E8E]};
        flushBytes(source);
        BaseEncoding[] encodings = {
                BaseEncoding.getBase16()
                , BaseEncoding.getBase64()
                , BaseEncoding.getBase64Safe()
                , BaseEncoding.getBase32()
                , BaseEncoding.getBase32Hex()
                , BaseEncoding.getBase16()};
        long times = 0, bytes = 0;
        byte[] rb = new byte[0xFFFFFC];
        char[] rc = new char[0xCFFFFF];
        System.out.println("BENCHMARK BEGIN");
        long beginSeconds = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            // Running 19*9*6 times ~= 1K
            for (int j = 0; j < 19; j++) {
                for (BaseEncoding encoding : encodings) {
                    for (byte[] bs : source) {
                        times++;
                        bytes += bs.length;
                        int offset = (int) (((i + j) % 2 == 0 ? 0xCC55 : 0xAA33) - bytes & 0xFFF);
                        int num = encoding.encode(bs, 0, bs.length, rc, offset);
                        encoding.decode(rc, offset, num, rb, offset * 2 + 1);
                        // encoding.decode(encoding.encode(bs));
                    }
                }
            }
            System.out.println("\tLOOP " + (i + 1) + "/" + 8);
        }
        long endSeconds = System.currentTimeMillis();
        System.out.println("Total:  " + (endSeconds - beginSeconds) + "ms (" + longToHex(times) + " TIMES, " + longToHex(bytes) + " BYTES)");
        System.out.println("BENCHMARK END");
        System.out.println("\nPress any key to exit");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void flushBytes(byte[][] bs) {
        Random r = new Random();
        for (byte[] t : bs)
            r.nextBytes(t);
    }

    static String longToHex(long l) {
        double d = (double) l;
        int i = 0;
        do {
            i++;
            d /= 1024;
        }
        while (d / 1024 > 1);
        return String.format("%1.1f", d) + " KMG".charAt(i);
    }
}