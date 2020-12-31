package com.company;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

import java.io.IOException;


public class Main {
    static int counter = 0;
    static int fishCaught = 0;
    static final int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    static final int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    static final Rectangle fieldOfActionRectangle = new Rectangle(400, screenHeight / 2 - 200, screenWidth - 800, screenHeight / 2 - 300);

    static Point objectPoint() throws Exception {
        Window window = new Window(fieldOfActionRectangle);
        BufferedImage screen = window.screenShot();
        Point objectPoint = new Point(400, screenHeight / 2 - 200);

        int[][] pixels = ((DataBufferInt) screen.getData().getDataBuffer()).getBankData();
        int r, g, b;
        int[] pixel = pixels[0];
        int y = screenHeight / 2 - 200;
        for (int j = 0; j < pixel.length; j++) {
            if (j % (pixels[0].length / fieldOfActionRectangle.height) == 0) {
                y++;
            }
            r = (pixel[j] >> 16) & 0xff;
            g = (pixel[j] >> 8) & 0xff;
            b = (pixel[j]) & 0xff;
            if (isMatch(r, g, b)) {
                int x = 400 + (j % (pixels[0].length / fieldOfActionRectangle.height));
                objectPoint.setLocation(x, y);
            }
        }


        return objectPoint;
    }

    static BufferedImage takeSs() throws Exception {
        Robot robot = new Robot();
        int spacer = 15;
        PointerInfo m = MouseInfo.getPointerInfo();
        Point mousePoint = m.getLocation();
        int x = (int) mousePoint.getX();
        int y = (int) mousePoint.getY();

        BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(x - (spacer * 2), y, 25, 25));
        counter++;

        return screenCapture;
    }


    static float percentageOfSimilarity(BufferedImage imageA, BufferedImage imageB) throws IOException {
        float percentage = 0;
        DataBuffer dbA = imageA.getData().getDataBuffer();
        int sizeA = dbA.getSize();
        DataBuffer dbB = imageB.getData().getDataBuffer();

        int count = 0;

        for (int i = 0; i < sizeA; i++) {
            if (dbA.getElem(i) == dbB.getElem(i)) {
                count++;
            }
        }
        percentage = (float) count * 100 / sizeA;


        return percentage;
    }

    static boolean locateObject() throws Exception {
        Point point;
        Robot robot = new Robot();

        point = objectPoint();
        if (point.getX() > 400) {
            robot.mouseMove((int) point.getX(), (int) point.getY());
            return true;
        }

        return false;
    }

    static void mouseClickOnObject() throws Exception {
        Robot robot = new Robot();
        Thread.sleep(200);

        for (int i = 0; i < 240; i++) {
            BufferedImage first = takeSs();

            Thread.sleep(50);
            BufferedImage second = takeSs();
            float percent = percentageOfSimilarity(first, second);

            if (percent > 0.00f && percent < 0.4f) {
                fishCaught++;
                System.out.println(fishCaught + " fish caught");
                System.out.println(percent + "%");
                robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                Thread.sleep(100);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                break;
            }
        }
    }

    static void press2() throws Exception {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_2);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_2);
    }

    static void press3() throws Exception {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_3);
        Thread.sleep(100);
        robot.keyRelease(KeyEvent.VK_3);
    }

    static boolean isMatch(int red, int green, int blue) {
        return isBigger(red, green) && isBigger(red, blue) && areClose(blue, green);
    }

    static boolean isBigger(int red, int other) {
        return (red * 0.50 > other);
    }

    static boolean areClose(int color1, int color2) {
        int max = Math.max(color1, color2);
        int min = Math.min(color1, color2);

        return min * 2 > max - 20;
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        boolean stopper = false;
        long startTime = System.currentTimeMillis();
        long baitTimer = System.currentTimeMillis();
        long fishingTime = 0, timer;
        long elapsedTime;

        Thread.sleep(4000);
        press3();
        Thread.sleep(3000);
        press2();
        timer = System.currentTimeMillis();

        while (!stopper) {
            if ((System.currentTimeMillis() - baitTimer) / 1000 > 600) {
                press3();
                Thread.sleep(3000);
                baitTimer = System.currentTimeMillis();
            }
            if (locateObject()) {
                Thread.sleep(250);
                mouseClickOnObject();
                Thread.sleep(5000);
                press2();
                timer = System.currentTimeMillis();
            } else {
                fishingTime = (System.currentTimeMillis() - timer) / 1000;
            }
            if (fishingTime > 12) {
                timer = System.currentTimeMillis();
                press2();
            }
        }

        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println(fishCaught + " fish caught " + elapsedTime / 1000F + "s elapsed");
        robot.mouseMove(300, 300);
    }
}
