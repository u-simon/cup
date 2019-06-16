package com.ca.ms.cup.common.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 *
 */
public final class AddressUtil {
    private static Logger logger = LoggerFactory.getLogger(AddressUtil.class);
    private static final Pattern IP_PATTERN = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3,}");

    public static String getHostIp() {
        InetAddress address = getHostAddress();
        return address == null ? null : address.getHostAddress();
    }

    public static String getHostName() {
        InetAddress address = getHostAddress();
        return address == null ? null : address.getHostName();
    }

    public static InetAddress getHostAddress() {
        InetAddress localAddress = null;

        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidHostAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable var6) {
            logger.warn("Failed to retriving local host ip address, try scan network card ip address. cause: " + var6.getMessage());
        }

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            if (e != null) {
                while (e.hasMoreElements()) {
                    try {
                        NetworkInterface e1 = (NetworkInterface) e.nextElement();
                        Enumeration addresses = e1.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress e2 = (InetAddress) addresses.nextElement();
                                    if (isValidHostAddress(e2)) {
                                        return e2;
                                    }
                                } catch (Throwable var5) {
                                    logger.warn("Failed to retriving network card ip address. cause:" + var5.getMessage());
                                }
                            }
                        }
                    } catch (Throwable var7) {
                        logger.warn("Failed to retriving network card ip address. cause:" + var7.getMessage());
                    }
                }
            }
        } catch (Throwable var8) {
            logger.warn("Failed to retriving network card ip address. cause:" + var8.getMessage());
        }

        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    private static boolean isValidHostAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String name = address.getHostAddress();
            return name != null && !"0.0.0.0".equals(name) && !"127.0.0.1".equals(name) && IP_PATTERN.matcher(name).matches();
        } else {
            return false;
        }
    }
}
