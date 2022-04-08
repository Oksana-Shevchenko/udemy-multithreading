package com.udemy.latency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String SOURCE_FILE = "./src/main/resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./src/main/out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();
        recolorMultipleThread(originalImage, resultImage, 2);
//        recolorSingleThread(originalImage, resultImage);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);
        System.out.println(duration);
    }

    public static void recolorMultipleThread(BufferedImage originalImage, BufferedImage resultImage, int numberOfThread) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThread;
        for (int i = 0; i < numberOfThread; i++) {
            final int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;
                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);

            });

            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void recolorSingleThread(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage,
                                    int leftCorner, int topCorner, int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }

    }

    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        int red = getRed(rgb);
        int blue = getBlue(rgb);
        int green = getGreen(rgb);

        int newRed;
        int newBlue;
        int newGreen;

        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        int newRGB = createRGBFromColor(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    public static boolean isShadeOfGray(int red, int green, int blue) {
        //check that all 3 components have the same color intensity
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;

    }

    public static int createRGBFromColor(int red, int green, int blue) {
        int rgb = 0;
        //right most value in rgb value we simply add it by applying logical or between rgb target value and the blue value
        rgb |= blue;
        //to add a green value we first apply a bit shift to the left in order to alien a green to the place in a pixel value
        // and then we add the result to rgb value by using the or function
        rgb |= green << 8;
        // to add a red value we first apply a bit shift 2 bytes to the left in order to add it to rgb value
        rgb |= red << 16;
        //set alpha value to the highest value to make the pixel
        rgb |= 0xFF000000;
        return rgb;
    }

    public static int getRed(int rgb) {
        //masks out the alpha, green and blue and shifting the red value 2 bytes (16 bits) to the right
        return (rgb & 0x00FF0000) >> 16;
    }

    public static int getGreen(int rgb) {
        //masks out the alpha, red and blue component but because the green component
        //is the second byte from the right we need to shift the value 8 bits to the right
        return (rgb & 0x0000FF00) >> 8;
    }

    public static int getBlue(int rgb) {
        //applying a bit mask on a pixel making all the components zero except for the right most byte
        //which is exactly the blue component
        return rgb & 0x000000FF;
    }
}
