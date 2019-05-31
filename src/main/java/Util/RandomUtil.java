package Util;

import java.util.Random;

public class RandomUtil {
    private static Random random=new Random();

    public static double nextDouble(double lowerBound, double upperBound) {
        return lowerBound + random.nextDouble() * (upperBound - lowerBound);
    }
}
