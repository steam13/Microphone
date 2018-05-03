package nuance.com;

import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONTokener;

class RestClient {
    RestClient() {
    }

    protected static double getDouble(HttpResponse resp) {
        return Double.valueOf(convertResponseToRawString(resp)).doubleValue();
    }

    protected static long getLong(HttpResponse resp) {
        String sResp = convertResponseToRawString(resp);
        if (sResp.contains("\"")) {
            sResp = sResp.substring(1, sResp.length() - 1);
        }
        return Long.valueOf(sResp).longValue();
    }

    protected static int getInt(HttpResponse resp) {
        return Integer.valueOf(convertResponseToRawString(resp)).intValue();
    }

    protected static String getString(HttpResponse resp) {
        return convertJSONResponseToString(resp);
    }

    protected static String convertJSONResponseToString(HttpResponse resp) {
        String raw = convertResponseToRawString(resp);
        if (raw.charAt(0) != '\"') {
            raw = new StringBuilder(String.valueOf('\"')).append(raw).append('\"').toString();
        }
        String sResp = "";
        try {
            Object obj = new JSONTokener(raw).nextValue();
            if (obj instanceof String) {
                return (String) obj;
            }
            if (obj instanceof Integer) {
                return ((Integer) obj).toString();
            }
            return sResp;
        } catch (Exception e) {
            return sResp;
        }
    }

    protected static String convertResponseToRawString(HttpResponse resp) {
        String sResp = "";
        HttpEntity entity = resp.getEntity();
        if (entity != null) {
            InputStream inStream = null;
            try {
                inStream = entity.getContent();
                sResp = convertStreamToString(inStream);
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (Exception e3) {
                    }
                }
            } catch (Throwable th) {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (Exception e4) {
                    }
                }
            }
        }
        return sResp;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static java.lang.String convertStreamToString(java.io.InputStream r5) {
        /*
        r1 = new java.io.BufferedReader;
        r3 = new java.io.InputStreamReader;
        r3.<init>(r5);
        r1.<init>(r3);
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r0 = 0;
    L_0x0010:
        r0 = r1.readLine();	 Catch:{ IOException -> 0x0022, all -> 0x0029 }
        if (r0 != 0) goto L_0x001e;
    L_0x0016:
        r5.close();	 Catch:{ IOException -> 0x0030 }
    L_0x0019:
        r3 = r2.toString();
        return r3;
    L_0x001e:
        r2.append(r0);	 Catch:{ IOException -> 0x0022, all -> 0x0029 }
        goto L_0x0010;
    L_0x0022:
        r3 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x0027 }
        goto L_0x0019;
    L_0x0027:
        r3 = move-exception;
        goto L_0x0019;
    L_0x0029:
        r3 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x002e }
    L_0x002d:
        throw r3;
    L_0x002e:
        r4 = move-exception;
        goto L_0x002d;
    L_0x0030:
        r3 = move-exception;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: nuance.com.RestClient.convertStreamToString(java.io.InputStream):java.lang.String");
    }
}
