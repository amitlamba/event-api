package com.und.common.utils;

import eu.bitwalker.useragentutils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class SystemDetailExtractor {


    private static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }


    public HashMap<String, String> browser(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();

        String browserName = browser.getName();
        BrowserType browserType = browser.getBrowserType();


        short id = browser.getId();
        Manufacturer manufacturer = browser.getManufacturer();
        String manufacturerName = manufacturer.getName();
        short manufacturerId = manufacturer.getId();

        RenderingEngine renderingEngine = browser.getRenderingEngine();
        String renderingEngineName = renderingEngine.name();

        //or
        // String browserName = browser.getGroup().getName();
        Version browserVersion = userAgent.getBrowserVersion();
        String version = browserVersion.getVersion();

        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        DeviceType deviceType = operatingSystem.getDeviceType();
        deviceType.getName();
        String osName = operatingSystem.getName();
        String osManufacturer = operatingSystem.getManufacturer().getName();
        System.out.println("The user is using browser " + browserName + " - version " + browserVersion);

        return null;
    }
}
