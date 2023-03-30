package com.infamousgc.loans.Utilities;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formatter {

    public static final String prefix = "&lLOANS »";

    public static String parse(String message) {
        final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            CharSequence input;
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String gradient(String message) {
        return gradient("005027", "01a451", message);
    }

    public static String gradient(String startHex, String endHex, String message) {
        if (!startHex.matches("[0-9a-fA-F]{6}") || !endHex.matches("[0-9a-fA-F]{6}")) {
            throw new IllegalArgumentException("Invalid hexadecimal color value");
        }

        StringBuilder result = new StringBuilder();
        int startRed = Integer.parseInt(startHex.substring(0,2), 16);
        int startGreen = Integer.parseInt(startHex.substring(2,4), 16);
        int startBlue = Integer.parseInt(startHex.substring(4,6), 16);
        int endRed = Integer.parseInt(endHex.substring(0,2), 16);
        int endGreen = Integer.parseInt(endHex.substring(2,4), 16);
        int endBlue = Integer.parseInt(endHex.substring(4,6), 16);

        String[] messageChars = message.split("(?<=.)"); // split the message into individual characters
        String formatCode = "";
        double redStep = (endRed - startRed) / (double)(messageChars.length - 1);
        double greenStep = (endGreen - startGreen) / (double)(messageChars.length - 1);
        double blueStep = (endBlue - startBlue) / (double)(messageChars.length - 1);

        int messageIndex = 0;
        for (int i = 0; i < messageChars.length; i++) {
            if (message.charAt(i) == '&') {
                if (isFormatCode(message.charAt(i+1))) { // check if the character is a formatting code
                    formatCode = String.valueOf(message.charAt(i)) + message.charAt(i + 1);
                    i++;
                    continue;
                }
                if (message.charAt(i+1) == 'r') {
                    formatCode = "";
                    i++;
                    continue;
                }
            }
            int currentRed = (int)(startRed + messageIndex * redStep);
            int currentGreen = (int)(startGreen + messageIndex * greenStep);
            int currentBlue = (int)(startBlue + messageIndex * blueStep);
            String hex = String.format("%02X%02X%02X", currentRed, currentGreen, currentBlue);
            result.append(String.format("#%s%s%s", hex, formatCode, messageChars[i]));
            messageIndex++;
        }

        return parse(result.toString());
    }

    private static boolean isFormatCode(char ch) {
        return ch == 'k' || ch == 'l' || ch == 'm' || ch == 'n' || ch == 'o';
    }

}
