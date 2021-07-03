package indi.simple.pms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:36
 * @Description:
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public HttpUtil() {
    }

    public static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;

        try {
            String urlNameString = url + "?" + param;
            logger.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while((line = in.readLine()) != null) {
                result.append(line);
            }

            logger.info("recv - {}", result);
        } catch (ConnectException var22) {
            logger.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, var22);
        } catch (SocketTimeoutException var23) {
            logger.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, var23);
        } catch (IOException var24) {
            logger.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, var24);
        } catch (Exception var25) {
            logger.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, var25);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception var21) {
                logger.error("调用in.close Exception, url=" + url + ",param=" + param, var21);
            }

        }

        return result.toString();
    }

    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            String urlNameString = url + "?" + param;
            logger.info("sendPost - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

            String line;
            while((line = in.readLine()) != null) {
                result.append(line);
            }

            logger.info("recv - {}", result);
        } catch (ConnectException var23) {
            logger.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, var23);
        } catch (SocketTimeoutException var24) {
            logger.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, var24);
        } catch (IOException var25) {
            logger.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, var25);
        } catch (Exception var26) {
            logger.error("调用HttpsUtil.sendPost Exception, url=" + url + ",param=" + param, var26);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException var22) {
                logger.error("调用in.close Exception, url=" + url + ",param=" + param, var22);
            }

        }

        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;

        try {
            logger.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init((KeyManager[])null, new TrustManager[]{new HttpUtil.TrustAnyTrustManager()}, new SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection)console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new HttpUtil.TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";

            while((ret = br.readLine()) != null) {
                if (ret != null && !ret.trim().equals("")) {
                    result.append(new String(ret.getBytes("ISO-8859-1"), "utf-8"));
                }
            }

            logger.info("recv - {}", result);
            conn.disconnect();
            br.close();
        } catch (ConnectException var10) {
            logger.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, var10);
        } catch (SocketTimeoutException var11) {
            logger.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, var11);
        } catch (IOException var12) {
            logger.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, var12);
        } catch (Exception var13) {
            logger.error("调用HttpsUtil.sendSSLPost Exception, url=" + url + ",param=" + param, var13);
        }

        return result.toString();
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        private TrustAnyHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        private TrustAnyTrustManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
