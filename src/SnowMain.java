import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static java.math.BigInteger.ZERO;

public class SnowMain {


    static long[] key_arr = new long[8];
    static long[] key_arr_sml = new long[4];
    static long[] vector_arr = new long[4];
    static long[][] s_arr = new long[2][];
    static BigInteger test_896_bit;
    static BigInteger test_512_bit;
    static long startTime;
    static long finishTime;
    public static void creare_arr128(BigInteger key, BigInteger vector) {

            key_arr_sml = SnowFunc.split_arrForInit(key, 4, 32);
        vector_arr = SnowFunc.split_arrForInit(vector, 4, 32);
    }
    public static void creare_arr256(BigInteger key, BigInteger vector){
        key_arr = SnowFunc.split_arrForInit(key, 8, 32);
        vector_arr = SnowFunc.split_arrForInit(vector, 4, 32);
    }
    public static void init(long[] key, long[] vec) {
        long[] val = new long[16];
        if (key.length == 4) {
                val[15] = key[3];
                val[11] = SnowFunc.inv(key[3]);
                val[7] = key[3];
                val[3] = SnowFunc.inv(key[3]);

                val[14] = key[2];
                val[10] = SnowFunc.inv(key[2]);
                val[6] = key[2];
                val[2] = SnowFunc.inv(key[2]);

                val[13] = key[1];
                val[9] = SnowFunc.inv(key[1]);
                val[5] = key[1];
                val[1] = SnowFunc.inv(key[1]);

                val[12] = key[0];
                val[8] = SnowFunc.inv(key[0]);
                val[4] = key[0];
                val[0] = SnowFunc.inv(key[0]);
            s_arr[0] = val;
        } else {
                val[15] = key[7];
                val[7] = SnowFunc.inv(key[7]);

                val[14] = key[6];
                val[6] = SnowFunc.inv(key[6]);

                val[13] = key[5];
                val[5] = SnowFunc.inv(key[5]);

                val[12] = key[4];
                val[4] = SnowFunc.inv(key[4]);

                val[11] = key[3];
                val[3] = SnowFunc.inv(key[3]);

                val[10] = key[2];
                val[2] = SnowFunc.inv(key[2]);

                val[9] = key[1];
                val[1] = SnowFunc.inv(key[1]);

                val[8] = key[0];
                val[0] = SnowFunc.inv(key[0]);
            s_arr[0] = val;
        }
        val[9] = val[9] ^ (vector_arr[3]);
        val[10] = val[10] ^ (vector_arr[2]);
        val[12] = val[12] ^ (vector_arr[1]);
        val[15] = val[15] ^ (vector_arr[0]);
        s_arr[0] = val;
        long[] zr = new long[2];
        zr[0] = 0;
        zr[1] = 0;
        s_arr[1] = zr;
    }

    static byte[] message;
    public static byte[] generationMSG(int leng) {
        Random r = new Random();
        message = new byte[leng];
        r.nextBytes(message);
        return message;
    }

    public static void init_next() {
            SnowFunc.NextForInit();
            SnowFunc.NextForInit();
            //System.out.println("s_arr[0] = " + Arrays.toString(s_arr[0]));
            //System.out.println("s_arr[1] = " + Arrays.toString(s_arr[1]));
    }

    public static void genKey128() {
        byte[] bytes = new byte[16];    // 512 bits
        new Random().nextBytes(bytes);
        bytes[0] |= 0x80;               // set the most significant bit
        test_896_bit = new BigInteger(1, bytes);
    }

    public static void genVector() {
        byte[] bytes = new byte[16];    // 128 bits
        new Random().nextBytes(bytes);
        bytes[0] |= 0x80;               // set the most significant bit
        test_512_bit = new BigInteger(1, bytes);
    }

    public static void genKey256() {
        byte[] bytes = new byte[32];    // 256 bits
        new Random().nextBytes(bytes);
        bytes[0] |= 0x80;               // set the most significant bit
        test_896_bit = new BigInteger(1, bytes);
    }

    public static void Streamlet128() {
        //genKey512();
        //genVector();
        creare_arr128(test_512_bit, test_512_bit);
        init(key_arr_sml, vector_arr);

        SnowFunc.NextForInit();
        SnowFunc.NextForInit();
        SnowFunc.NextForInit();
        SnowFunc.NextForInit();
    }

    public static void Streamlet256() {
        //genKey1024();
        //genVector();
        creare_arr256(test_896_bit, test_512_bit);
        init(key_arr, vector_arr);
        SnowFunc.NextForInit();
        SnowFunc.NextForInit();
        SnowFunc.NextForInit();
        SnowFunc.NextForInit();

    }

    private static void test1Gb256() {
        int u = ((1024 * 1024 * 1024) / 8);
        System.out.println("Тестирование на большом пакете: " + u + " bytes");
        System.out.println("Использовался 1 ключ");
        long result;
        double timeTest, readyTime = 0;
        byte[] a = generationMSG(u);
        int len = a.length;
        Streamlet256();
        for (int j = 0; j < u; j++) {
            timeTest = System.currentTimeMillis();
            SnowFunc.Next();
            result = SnowFunc.Strm() ^ 9223372036854775807L;
            timeTest = System.currentTimeMillis() - timeTest;
            readyTime += timeTest;
        }
        System.out.println("Final Time init took: " + ((readyTime / 1000)/4) + " ms");
        System.out.println("Final Speed init took: " + u/((readyTime * 1000)/4) + "bytes/pack");
    }

    private static void test1Gb128() {
        int u = ((1024 * 1024 * 1024) / 8);
        long result;
        double timeTest, readyTime = 0;
        byte[] a = generationMSG(u);
        int len = a.length;
        Streamlet128();
        for (int j = 0; j < len; j++) {
            timeTest = System.currentTimeMillis();
            SnowFunc.Next();
            result = SnowFunc.Strm() ^ a[j];
            timeTest = System.currentTimeMillis() - timeTest;
            readyTime += timeTest;
        }
        System.out.println("Final Time init took: " + ((readyTime / 1000)/4) + " ms");
        System.out.println("Final Speed init took: " + u/((readyTime * 1000)/4) + " ms");
    }

    private static void testSmallPackByte128(int bytes, long packages, long key) {
        int u = 1024 * 1024 * 1024 / 8;
        long result;
        double timeTest, readyTime = 0;
        long m = (bytes * packages)/4;
        //System.out.println(m);
        for (int r = 0; r < 3000; r++) {
            for (int y = 0; y < key; y++) {
                Streamlet128();
                for (int x = 0; x < m; x++) {
                    timeTest = System.currentTimeMillis();
                    SnowFunc.Next();
                    result = SnowFunc.Strm() ^ 9223372036854775807L;
                    timeTest = System.currentTimeMillis() - timeTest;
                    readyTime = readyTime + timeTest;

                }
            }
        }
        System.out.println("Final Time init took: " + ((readyTime) / 3) + " mks");
        System.out.println("Final Speed pack/mks took: " + packages/((readyTime/3)) + " mks");
        System.out.println("Final Speed byte/mks took: " + bytes/((readyTime/3)) + " mks");
    }

    private static void testSmallPackByte256(int bytes, long packages, long key) {
        int u = 1024 * 1024 * 1024 / 8;
        long result;
        double timeTest, readyTime = 0;
        long m = (bytes * packages)/4;
        for (int r = 0; r < 3000; r++) {
            for (int y = 0; y < key; y++) {
                Streamlet256();
                for (int x = 0; x < m; x++) {
                    timeTest = System.currentTimeMillis();
                    SnowFunc.Next();
                    result = SnowFunc.Strm() ^ 9223372036854775807L;
                    timeTest = System.currentTimeMillis() - timeTest;
                    readyTime = readyTime + timeTest;

                }
            }
        }
        System.out.println("Final Time init took: " + ((readyTime) / 3) + " mks");
        System.out.println("Final Speed pack/mks took: " + packages/((readyTime/3)) + " mks");
        System.out.println("Final Speed byte/mks took: " + bytes/((readyTime/3)) + " mks");
    }

    private static void keyInit128_7000(long keys) {
        double timeTest, readyTime = 0;
        for (int z = 0; z < 3000; z++) {
            for (int i = 0; i < keys; i++) {
                timeTest = System.currentTimeMillis();
                Streamlet128();
                timeTest = System.currentTimeMillis() - timeTest;
                readyTime += timeTest;
            }
        }
        readyTime /= 3000000;
        System.out.println("     Algorithm: Snow | Initialization speed: " + readyTime + " mks | " +
                7000/readyTime + " cycles/setting | " + 7000 / readyTime * 1000 + "setting/sec");
    }

    private static void keyInit256_7000(long keys) {
        double timeTest, readyTime = 0;
        for (int z = 0; z < 3000; z++) {
            for (int i = 0; i < keys; i++) {
                timeTest = System.currentTimeMillis();
                Streamlet256();
                timeTest = System.currentTimeMillis() - timeTest;
                readyTime += timeTest;
            }
        }
        readyTime /= 3000000;
        System.out.println("     Algorithm: Snow | Initialization speed: " + readyTime + " mks | " +
                7000/readyTime + " cycles/setting | " + 7000 / readyTime * 1000 + "setting/sec");
    }
    private static void keyInit256_500(long keys) {
        double timeTest, readyTime = 0;
        for (int z = 0; z < 3000; z++) {
            for (int i = 0; i < keys; i++) {
                timeTest = System.currentTimeMillis();
                Streamlet256();
                timeTest = System.currentTimeMillis() - timeTest;
                readyTime += timeTest;
            }
        }
        readyTime /= 3000;
        System.out.println("     Algorithm: Snow | Initialization speed: " + readyTime + " mks | " +
                500/readyTime + " cycles/setting | " + 7000 / readyTime * 1000 + "setting/sec");
    }
    private static void keyInit128_500(long keys) {
        double timeTest, readyTime = 0;
        for (int z = 0; z < 3000; z++) {
            for (int i = 0; i < keys; i++) {
                timeTest = System.currentTimeMillis();
                Streamlet256();
                timeTest = System.currentTimeMillis() - timeTest;
                readyTime += timeTest;
            }
        }
        readyTime /= 3000;
        System.out.println("     Algorithm: Snow | Initialization speed: " + readyTime + " mks | " +
                500/readyTime + " cycles/setting | " + 7000 / readyTime * 1000 + "setting/sec");
    }
    public static void main(String[] args) {
        genKey128();
        genVector();
        genKey256();
        System.out.println("1gb on 128 bit key");
        test1Gb128();
        System.out.println();
        System.out.println("40 bytes, 35 packages, 10 keys: KEY - 128bit");
        testSmallPackByte128(40, 35, 10);
        System.out.println();
        System.out.println("576 bytes, 12 packages, 10 keys: KEY - 128bit");
        testSmallPackByte128(576, 12, 10);
        System.out.println();
        System.out.println("1500 bytes, 50 packages, 1 key: KEY - 128bit");
        testSmallPackByte128(1500, 50, 1);
        System.out.println();
        System.out.println("init 7000 keys: KEY - 128bit");
        keyInit128_7000(7000);
        System.out.println();
        System.out.println("init 500 keys: KEY - 128bit");
        keyInit128_500(500);
        System.out.println("------------------------------------------Starting 256 bit key--------------------------------------------");
        System.out.println("1gb on 256 bit key");
        test1Gb256();
        System.out.println();
        System.out.println("40 bytes, 35 packages, 10 keys: KEY - 256bit");
        testSmallPackByte256(40, 35, 10);
        System.out.println();
        System.out.println("576 bytes, 12 packages, 10 keys: KEY - 256bit");
        testSmallPackByte256(576, 12, 10);
        System.out.println();
        System.out.println("1500 bytes, 50 packages, 1 key: KEY - 256bit");
        testSmallPackByte256(1500, 50, 1);
        System.out.println();
        System.out.println("init 7000 keys: KEY - 256bit");
        keyInit256_7000(7000);
        System.out.println();
        System.out.println("init 500 keys: KEY - 256bit");
        keyInit256_500(500);    }
}

