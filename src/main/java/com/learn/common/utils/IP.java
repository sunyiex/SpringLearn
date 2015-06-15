package com.learn.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Yi on 2015/6/8.
 */
public class IP {

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }

    public static String getAddressByIP(String strIP)
    {
        try
        {
            URL url = new URL( "http://ip.qq.com/cgi-bin/searchip?searchip1=" + strIP);
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while((line = reader.readLine()) != null)
            {
                result.append(line);
            }
            reader.close();
            strIP = result.substring(result.indexOf( "该IP所在地为" ));
            strIP = strIP.substring(strIP.indexOf( "省") + 1);
            String province = strIP.substring(6, strIP.indexOf("市"));
            String city = strIP.substring(strIP.indexOf("市") + 1, strIP.indexOf("��"));
            return province+" "+city;
        }
        catch( IOException e)
        {
            return "获取失败";
        }

    }
}
