package ru.otus;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.math.BigInteger;

public class HelloOtus {
    static LoadingCache<Integer, BigInteger> memo = CacheBuilder.newBuilder()
            .build(CacheLoader.from(HelloOtus::getFactorial));

    public static void main(String... args) {
        System.out.println("factorial result: " + getFactorial(-1));

    }
    public static BigInteger getFactorial(int n){
        if (n<=0)return BigInteger.ONE;
        return BigInteger.valueOf(n).multiply(memo.getUnchecked(n-1));
    }
}
