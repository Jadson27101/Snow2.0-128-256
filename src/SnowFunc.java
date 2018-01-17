import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;

public class SnowFunc {
    static long s = 4294967295L;
    static long r2m;
    public static String zero_fill(String s, int size) {
        StringBuilder str = new StringBuilder(s);
        int aux = size - str.length();
        for (int i = 0; i < aux; i++) {
            str.insert(i, '0');
        }
        String strin = new String(str);
        return strin;
    }

    public static long[] split_arrForInit(BigInteger value, int amount, int size) {
        String val = value.toString(2);
        long[] valu = new long[amount];
        int aux = 0;
        String word = "";
        for (int i = 0; i < val.length(); i++) {
            word += val.charAt(i);
            if (word.length() == size) {
                valu[aux] = Long.parseLong(word,2);
                word = "";
                aux++;
            }
        }
        return valu;
    }
    static long res_arr[] = new long[4];
    static long res_arr_init[] = new long[8];
    public static long[] sp_arr(long value){
        //System.out.println("value of number: "+Long.toBinaryString(value));
        res_arr[0] = value >>24;
        //System.out.println("value 0: "+Long.toBinaryString(res_arr[0]));
        long help = value;
        for (int i =0; i<8;i++){
            help = help<<1;
            if(help>>32 == 1) {
                help ^= s + 1;
            }
        }
        res_arr[1] = help>>24;
        //System.out.println("value 1: "+Long.toBinaryString(res_arr[1]));
        help = value;
        for (int i =0; i<16;i++){
            help = help<<1;
            if(help>>32 == 1) {
                help ^= s + 1;
            }
        }
        res_arr[2]= help>>24;

        //System.out.println("value 2: "+Long.toBinaryString(res_arr[2]));
        help = value;
        for(int i = 0;i<24;i++){
            help = help<<1;
            if(help>>32 ==1) {
                help ^= s + 1;
            }
        }
        res_arr[3] = help>>24;

        //System.out.println("value 3: "+Long.toBinaryString(res_arr[3]));
        return res_arr;
    }

    public static long To64(long[] m) {
        long result = 0;
            result ^= m[0];
            result ^= m[1];
            result ^= m[2];
            result ^= m[3];
        return result;
    }

    public static long FSM(long x, long y, long z) {
        long q = ((x + y)% s) ^ z;
        return q;
    }

    public static long Strm() {
        long Z = FSM(SnowMain.s_arr[0][15], SnowMain.s_arr[1][0], SnowMain.s_arr[1][1]) ^ (SnowMain.s_arr[0][0]);
        return Z;
    }

    public static long[] Kalina(long[] w) {
        w[0] = SnowTables.snow_T0[(int) w[0]];

        w[1] = SnowTables.snow_T1[(int) w[1]];
        w[2] = SnowTables.snow_T2[(int) w[2]];
        w[3] = SnowTables.snow_T3[(int) w[3]];
        return w;

    }

    public static long inv(long value){
        String val = Long.toBinaryString(value);
        long mask = 1;
        for(int i = 1; i < val.length(); ++i)
            mask |= mask << 1;
        long result = ~value & mask;
        if (result ==0){
            result = value+1;
        }
        return result;
    }
    public static long mulA(long w){
        int index = Math.toIntExact(w >> 24);
        long a =0;
        for (int i = 0;i<8;i++){
             a = w<<1;
            if(a>>32 == 1){
                a^=s+1;
            }
        }

        //System.out.println(w);
        long result = (a^(SnowTables.snow_alpha_mul[index]));
        return result;
    }
    public static long mulAinv(long w){
        int index = Math.toIntExact(w & 0xff);
        long a = (w>>8);
        long result = ((a^(SnowTables.snow_alphainv_mul[index])));
                return result;
    }
    public static long NextForInit() {
        r2m = SnowMain.s_arr[1][1];
        //System.out.println(Arrays.toString(sp_arr(SnowMain.s_arr[1][0])));
        SnowMain.s_arr[1][1] = To64(Kalina(sp_arr(SnowMain.s_arr[1][0])));
        SnowMain.s_arr[1][0] = ((r2m+ SnowMain.s_arr[0][3])%(s));

        SnowMain.s_arr[0][0]=  SnowMain.s_arr[0][1];
        SnowMain.s_arr[0][1]=  SnowMain.s_arr[0][2];
        SnowMain.s_arr[0][2]=  SnowMain.s_arr[0][3];
        SnowMain.s_arr[0][3]=  SnowMain.s_arr[0][4];
        SnowMain.s_arr[0][4]=  SnowMain.s_arr[0][5];
        SnowMain.s_arr[0][5]=  SnowMain.s_arr[0][6];
        SnowMain.s_arr[0][6]=  SnowMain.s_arr[0][7];
        SnowMain.s_arr[0][7]=  SnowMain.s_arr[0][8];
        SnowMain.s_arr[0][8]=  SnowMain.s_arr[0][9];
        SnowMain.s_arr[0][9]=  SnowMain.s_arr[0][10];
        SnowMain.s_arr[0][10]=  SnowMain.s_arr[0][11];
        SnowMain.s_arr[0][11]=  SnowMain.s_arr[0][12];
        SnowMain.s_arr[0][12]=  SnowMain.s_arr[0][13];
        SnowMain.s_arr[0][13]=  SnowMain.s_arr[0][14];
        SnowMain.s_arr[0][14]=  SnowMain.s_arr[0][15];
        //System.out.println(Arrays.toString(SnowMain.s_arr[0]));
        SnowMain.s_arr[0][15] = mulA(SnowMain.s_arr[0][0]) ^ SnowMain.s_arr[0][2] ^ mulAinv(SnowMain.s_arr[0][11]) ^ FSM(SnowMain.s_arr[0][15],SnowMain.s_arr[1][0],SnowMain.s_arr[1][1]);
        //System.out.println(SnowMain.s_arr[0][15]);
        return SnowMain.s_arr[0][15];
    }
    public static long Next() {
        r2m = SnowMain.s_arr[1][1];
        //System.out.println(Arrays.toString(sp_arr(SnowMain.s_arr[1][0])));
        SnowMain.s_arr[1][1] = To64(Kalina(sp_arr(SnowMain.s_arr[1][0])));
        SnowMain.s_arr[1][0] = ((r2m+ SnowMain.s_arr[0][3])%(s));

        SnowMain.s_arr[0][0]=  SnowMain.s_arr[0][1];
        SnowMain.s_arr[0][1]=  SnowMain.s_arr[0][2];
        SnowMain.s_arr[0][2]=  SnowMain.s_arr[0][3];
        SnowMain.s_arr[0][3]=  SnowMain.s_arr[0][4];
        SnowMain.s_arr[0][4]=  SnowMain.s_arr[0][5];
        SnowMain.s_arr[0][5]=  SnowMain.s_arr[0][6];
        SnowMain.s_arr[0][6]=  SnowMain.s_arr[0][7];
        SnowMain.s_arr[0][7]=  SnowMain.s_arr[0][8];
        SnowMain.s_arr[0][8]=  SnowMain.s_arr[0][9];
        SnowMain.s_arr[0][9]=  SnowMain.s_arr[0][10];
        SnowMain.s_arr[0][10]=  SnowMain.s_arr[0][11];
        SnowMain.s_arr[0][11]=  SnowMain.s_arr[0][12];
        SnowMain.s_arr[0][12]=  SnowMain.s_arr[0][13];
        SnowMain.s_arr[0][13]=  SnowMain.s_arr[0][14];
        SnowMain.s_arr[0][14]=  SnowMain.s_arr[0][15];
        //System.out.println(SnowMain.s_arr[1][0]+" "+SnowMain.s_arr[1][1]);
        //System.out.println(Arrays.toString(SnowMain.s_arr[0]));
        SnowMain.s_arr[0][15] = mulA(SnowMain.s_arr[0][0]) ^ SnowMain.s_arr[0][2] ^ mulAinv(SnowMain.s_arr[0][11]);
        return SnowMain.s_arr[0][15];
    }
}
