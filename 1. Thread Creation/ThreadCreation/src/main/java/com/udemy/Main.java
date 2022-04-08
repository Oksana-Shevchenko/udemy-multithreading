package com.udemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static final int MAX_PASSWORD_VALUE = 9999;

    public static void main(String[] args) {
        Random random = new Random();
        Vault vault = new Vault(random.nextInt(MAX_PASSWORD_VALUE));
        List<Thread> threads = new ArrayList<>();
        threads.add(new AscendingHackerThread(vault));
        threads.add(new DescendingHackerThread(vault));
        threads.add(new PoliceThread());
        for (Thread thread : threads) {
            thread.start();
        }
    }

    private static class Vault {
        private final int password;

        public Vault(int password) {
            this.password = password;
        }

        public boolean isCorrectPassword(int guess) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.password == guess;
        }
    }

    private static abstract class GenericHackerThread extends Thread {
        private final Vault vault;

        public GenericHackerThread(Vault vault) {
            this.vault = vault;
            this.setName(this.getClass().getSimpleName());
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public synchronized void start() {
            System.out.println("Starting thread " + this.getName());
            super.start();
        }

        public Vault getVault() {
            return vault;
        }
    }

    private static class AscendingHackerThread extends GenericHackerThread {

        public AscendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = 0; i < MAX_PASSWORD_VALUE; i++) {
                if (getVault().isCorrectPassword(i)) {
                    System.out.println(this.getName() + " guess the password " + i);
                    System.exit(0);
                }
            }
        }
    }

    private static class DescendingHackerThread extends GenericHackerThread {

        public DescendingHackerThread(Vault vault) {
            super(vault);
        }

        @Override
        public void run() {
            for (int i = MAX_PASSWORD_VALUE; i > 0; i--) {
                if (getVault().isCorrectPassword(i)) {
                    System.out.println(this.getName() + " guess the password " + i);
                    System.exit(0);
                }
            }
        }
    }

    private static class PoliceThread extends Thread {
        @Override
        public void run() {
            for (int i = 10; i > 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
            System.out.println("Game over for you hackers");
            System.exit(0);
        }
    }
}
