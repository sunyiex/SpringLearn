package com.learn.common.utils;

import org.springframework.core.convert.Property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Yi on 2015/6/16.
 */
public class PropertyUtil {
    public static String getEmail(String name) {
        Properties properties = new Properties();
        InputStream inputStream = Property.class.getResourceAsStream("/email.properties");
        try {
            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(name).trim();
    }
    public static String getFilterURL(String name) {
        Properties properties = new Properties();
        InputStream inputStream = Property.class.getResourceAsStream("/filterURL.properties");
        try {
            properties.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(name).trim();
    }
}
