package test;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

/**
 * @author chenfuqian
 */
public class HttpRequester {

    private static HttpRequester requester;

    private CookieStore cookieStore;

    private HttpClient httpClient;

    private HttpRequester() {
        cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("examWeb", "E4FC081413EDD6BA7FB447440D770028");
        cookie.setVersion(0);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public static HttpRequester getInstance() {
        if (requester == null) {
            requester = new HttpRequester();
        }
        return requester;
    }

    public static void main(String[] agrs) {
        String result = new HttpRequester().getRequest("http://localhost:8081/api/v1/web/banner/list", "");
        System.out.println(result);
    }

    public String getRequest(String url) {
        return getRequest(url, "UTF-8");
    }

    public String getRequest(String url, String encode) {
        URI uri = URI.create(url);
        HttpGet request = new HttpGet(uri);
        InputStreamReader isr = null;
        try {
            StringBuffer sb = new StringBuffer();
            HttpResponse response = httpClient.execute(request);
            List<Cookie> cookies = cookieStore.getCookies();
            cookies.stream().forEach(k -> {
                System.out.println(k.getName() + "：" + k.getValue());
            });
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            isr = new InputStreamReader(content, "UTF-8");
            char[] c = new char[1024 * 4];
            int len = 0;
            while ((len = isr.read(c)) != -1) {
                sb.append(c, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeHttpClient() {
        try {
            ((CloseableHttpClient) httpClient).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
