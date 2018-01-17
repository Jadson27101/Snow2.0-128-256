import java.math.BigInteger;

public class GFarithmetic {
    static long Zero_poly = 11;
    static BigInteger Zero_polyb = new BigInteger("11");
    static long zeroPoly8 = 285;
    static BigInteger zeroPoly8b = new BigInteger("285");
    static BigInteger getZeroPoly64 = new BigInteger("9223372036854775835");

    public static long add(long a, long b) {
        long result = a ^ b;
        return result;
    }

    public static BigInteger add(BigInteger a, BigInteger b) {
        BigInteger result = a.xor(b);
        return result;
    }


    public static long multiplication(long firsnumber, long b) {
        String secondnumber = Long.toBinaryString(b);
        StringBuffer sb = new StringBuffer(secondnumber);
        secondnumber = String.valueOf(sb.reverse());
        long pow = (long) Math.pow(2, zeroPoly8);
        long result = 0;
        for (int i = 0; i < secondnumber.length(); i++) {
            if (secondnumber.charAt(i) == '1') {
                long help = 0;
                if (i == 0) {
                    result = firsnumber;
                }
                for (int c = 0; c < i; c++) {
                    firsnumber = firsnumber << 1;
                    help = firsnumber;
                    if (firsnumber >= pow) {
                        firsnumber = firsnumber ^ zeroPoly8;
                        help = firsnumber;
                    }
                }
                result ^= help;
            }
        }
        return result;
    }

    public static BigInteger multiplication(BigInteger firsnumber, BigInteger b, int p) {
        String secondnumber = b.toString(2);
        StringBuffer sb = new StringBuffer(secondnumber);
        secondnumber = String.valueOf(sb.reverse());
        BigInteger bb = new BigInteger("2");
        BigInteger pow = new BigInteger(String.valueOf(bb.pow(p)));
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < secondnumber.length(); i++) {
            if (secondnumber.charAt(i) == '1') {
                BigInteger help = BigInteger.ZERO;
                if (i == 0) {
                    result = firsnumber;
                }
                for (int c = 0; c < i; c++) {
                    firsnumber = firsnumber.shiftLeft(1);
                    help = firsnumber;

                    if (firsnumber.compareTo(pow) >= 0) {
                        if (p == 32) {
                            firsnumber = firsnumber.xor(getZeroPoly64);
                            help = firsnumber;
                        } else {
                            firsnumber = firsnumber.xor(zeroPoly8b);
                        }
                    }
                }
                result = result.xor(help);
            }
        }
        return result;
    }

    public static long fastPow(long firstnumber, long secondnuber) {
        String secondnumber = Long.toBinaryString(secondnuber);

        long result = 1;
        for (int i = 0; i < secondnumber.length() - 1; i++) {
            if (secondnumber.charAt(i) == '1') {
                result = multiplication(result, firstnumber);
                result = multiplication(result, result);

            } else {
                result = multiplication(result, result);
            }
        }
        if (secondnumber.charAt(secondnumber.length() - 1) == '1') {
            result = multiplication(result, firstnumber);
        }
        return result;
    }

    public static BigInteger fastPow(BigInteger firstnumber, BigInteger secondnuber) {
        String secondnumber = secondnuber.toString(2);
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < secondnumber.length() - 1; i++) {
            if (secondnumber.charAt(i) == '1') {
                result = multiplication(result, firstnumber, 32);
                result = multiplication(result, result, 32);

            } else {
                result = multiplication(result, result, 32);
            }
        }
        if (secondnumber.charAt(secondnumber.length() - 1) == '1') {
            result = multiplication(result, firstnumber, 32);
        }
        return result;
    }


    public static long inverse_poly(long num) {
        long exponent = ((long) Math.pow(2, 8)) - 2;
        long result = fastPow(num, exponent);
        return result;
    }

    public static BigInteger inverse_poly(BigInteger num) {
        long exponent = ((long) Math.pow(2, 32)) - 2;
        BigInteger exp = BigInteger.valueOf(exponent);
        BigInteger result = fastPow(num, exp);
        return result;
    }

}
