package com.udemy.join;

import java.math.BigInteger;

public class FactorialThread extends Thread {
    private final long inputNumber;
    private boolean isFinished = false;
    private BigInteger result = BigInteger.ZERO;

    public FactorialThread(long inputNumber) {
        this.inputNumber = inputNumber;
    }

    @Override
    public void run() {
        this.result = factorial(inputNumber);
        this.isFinished = true;
    }

    public BigInteger factorial(long n) {
        BigInteger tmpResult = BigInteger.ONE;
        for (long i = n; i > 0; i--) {
            tmpResult = tmpResult.multiply(new BigInteger(Long.toString(i)));
        }
        return tmpResult;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public BigInteger getResult() {
        return result;
    }
}
