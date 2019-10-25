package com.softpark.util;

import java.awt.*;

public class Dimensions {
        private static Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        private static double width = screenSize.getWidth();
        private static double height = screenSize.getHeight();

        public static double getHeight() {
            return height;
        }

        public static double getWidth() {
            return width;
        }

}
