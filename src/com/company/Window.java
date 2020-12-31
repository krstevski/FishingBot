package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Window {
    private final Rectangle rectangle;
    private final Robot robot = new Robot();
    private int[][] pixels;

    public Window(Rectangle rectangle) throws AWTException {
        this.rectangle = rectangle;
    }

    public BufferedImage screenShot() {
        return robot.createScreenCapture(rectangle);
    }

//    public void shiftPixels(int[][] image) {
//        int j = image.length;
//        int i = image
//
//        pixels = new int[image.length][image.getWidth()];
//        for (int y = 0; y < image.getHeight(); y++) {
//            for (int x = 0; x < image.getWidth(); x++) {
//                pixels[y][x] = (image.getRGB(x++, y) >> 24) & 0xff;
//                pixels[y][x] = (image.getRGB(x++, y) >> 16) & 0xff;
//                pixels[y][x] = (image.getRGB(x++, y) >> 8) & 0xff;
//                pixels[y][x] = (image.getRGB(x, y)) & 0xff;
//            }
//        }
//    }

    public int[][] getPixels() {
        return pixels;
    }

    public void printPixelARGB(int pixel) {
        int alpha = (pixel >> 24) & 0xff;
        int red = (pixel >> 16) & 0xff;
        int green = (pixel >> 8) & 0xff;
        int blue = (pixel) & 0xff;
        System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
    }

    public void marchThroughImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        System.out.println("width, height: " + w + ", " + h);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.println("x,y: " + j + ", " + i);
                int pixel = image.getRGB(j, i);
                printPixelARGB(pixel);
                System.out.println("");
            }
        }
    }
}
