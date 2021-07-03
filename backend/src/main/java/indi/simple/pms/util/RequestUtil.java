package indi.simple.pms.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:38
 * @Description:
 */
public class RequestUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);
    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    public RequestUtil() {
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public String removeParam(HttpServletRequest request, String paramName) {
        String queryString = "";
        Enumeration keys = request.getParameterNames();

        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            if (!key.equals(paramName)) {
                if ("".equals(queryString)) {
                    queryString = key + "=" + request.getParameter(key);
                } else {
                    queryString = queryString + "&" + key + "=" + request.getParameter(key);
                }
            }
        }

        return queryString;
    }

    public static String getBasePath(HttpServletRequest request) {
        StringBuffer basePath = new StringBuffer();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme);
        basePath.append("://");
        basePath.append(domain);
        if ("http".equalsIgnoreCase(scheme) && 80 != port) {
            basePath.append(":").append(String.valueOf(port));
        } else if ("https".equalsIgnoreCase(scheme) && port != 443) {
            basePath.append(":").append(String.valueOf(port));
        }

        return basePath.toString();
    }

    public static String getIP(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        } else {
            String ip = null;

            try {
                ip = request.getHeader("Cdn-Src-Ip");
                if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }

                if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Forwarded-For");
                }

                if (ip == null || ip.length() == 0 || " unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("x-forwarded-for");
                }

                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }

                if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }

                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_CLIENT_IP");
                }

                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                }

                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }

                if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
        }
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException var1) {
            return "127.0.0.1";
        }
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var1) {
            return "未知";
        }
    }

    public static String getLocation(String ip) {
        String rspStr = HttpUtil.sendPost("http://ip.taobao.com/service/getIpInfo.php", "ip=" + ip);
        if (StringUtils.isEmpty(rspStr)) {
            LOGGER.error("获取地理位置异常 {}", ip);
            return "";
        } else {
            JsonNode obj = null;

            try {
                obj = JsonUtil.getObjectMapper().readTree(rspStr);
            } catch (IOException var6) {
                var6.printStackTrace();
            }

            JsonNode data = obj.get("data");
            String region = data.get("region").asText();
            String city = data.get("city").asText();
            return region + " " + city;
        }
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> result = new HashMap();
        Enumeration parameterNames = request.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            String parameterName = (String)parameterNames.nextElement();
            result.put(parameterName, request.getParameter(parameterName));
        }

        return result;
    }
}
