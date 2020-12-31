package com.company;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Window {
    private final Rectangle rectangle;
    private final Robot robot = new Robot();

    public Window(Rectangle rectangle) throws AWTException {
        this.rectangle = rectangle;
    }

    public BufferedImage screenShot() {
        return robot.createScreenCapture(rectangle);
    }

}
