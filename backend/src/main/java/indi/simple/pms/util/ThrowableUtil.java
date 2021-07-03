package indi.simple.pms.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author: wanglunhui
 * @Date: 2021/4/13 22:40
 * @Description:
 */
public class ThrowableUtil {
    public ThrowableUtil() {
    }

    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            Throwable var3 = null;

            String var4;
            try {
                throwable.printStackTrace(pw);
                var4 = sw.toString();
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (pw != null) {
                    if (var3 != null) {
                        try {
                            pw.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        pw.close();
                    }
                }

            }

            return var4;
        }
    }

    public static String getLocalStackStrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            try {
                StackTraceElement[] stackTraceElements = throwable.getStackTrace();
                pw.println(throwable.toString());
                if (throwable.getMessage() != null) {
                    pw.println(throwable.getMessage());
                }

                StackTraceElement[] var4 = stackTraceElements;
                int var5 = stackTraceElements.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    StackTraceElement stackTraceElement = var4[var6];
                    if (stackTraceElement.getClassName().startsWith("com.gzport.investmentfiling")) {
                        pw.println(stackTraceElement.toString());
                    }
                }

                String var11 = sw.toString();
                return var11;
            } finally {
                pw.close();
            }
        }
    }
}
