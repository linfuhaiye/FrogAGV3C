package test;

import com.furongsoft.base.misc.Tracker;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author chenfuqian
 */
public class HttpClientTest {
    private final static int TIMEOUT = 30000;

    public static String doPostJson(final String url, final String body) {
        CloseableHttpClient client = null;
        CloseableHttpResponse res = null;
        InputStream in = null;
        InputStreamReader isr = null;
        try {
            StringBuffer sb = new StringBuffer();
            client = HttpClients.createDefault();
            //HttpClients.custom().setDefaultCookieStore().build();
            HttpPost post = new HttpPost(url);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(TIMEOUT).setConnectionRequestTimeout(TIMEOUT).setSocketTimeout(TIMEOUT).build();
            post.setConfig(config);
            post.addHeader("charset", "UTF-8");
            post.addHeader("Content-Type", "application/json");
            post.addHeader("Cookies", "Hm_lvt_6da605d5179851cd3e688506c96b7613=1532758792; _ga=GA1.1.1222187795.1534950373; Idea-a0b644c5=85ca942e-1767-4f65-9066-7c6923ceca15; examWeb=E4FC081413EDD6BA7FB447440D770028");
            post.addHeader("Set-Cookie", "examWeb=E4FC081413EDD6BA7FB447440D770028; Path=/; HttpOnly");

            post.setEntity(new StringEntity(body, "UTF-8"));

            res = client.execute(post);
            HttpEntity entity = res.getEntity();
            in = entity.getContent();
            isr = new InputStreamReader(in, "UTF-8");
            char[] c = new char[1024 * 4];
            int len = 0;
            while ((len = isr.read(c)) != -1) {
                sb.append(c, 0, len);
            }
            return sb.toString();
        } catch (Exception e) {
            if (e instanceof HttpHostConnectException) {
                Tracker.info("HttpHostConnectException====");
            } else {
                Tracker.error(e);
            }
        } finally {
            try {
                if (isr != null) {
                    isr.close();
                }
                if (in != null) {
                    in.close();
                }
                if (res != null) {
                    res.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                Tracker.error(e);
            }
        }
        return null;
    }

    public static void main(String[] agrs) {
        String result = doPostJson("http://localhost:8081//api/v1/web/banner/list", "");
        System.out.println(result);
    }
}
