package com.example.demo.Task22.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

@Service
public class CityImageSearcher {
    public static String searchByCity(String cityName) throws UnsupportedEncodingException {

        String accessKey = "ВАШ_КЛЮЧ";
        String url = "https://api.unsplash.com/search/photos?query="
                + URLEncoder.encode(cityName, StandardCharsets.UTF_8)
                + " city&client_id=" + accessKey;

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpResponse response = client.execute(request)) {
            String json = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");
            if (!results.isEmpty()) {
                return results.getJSONObject(0).getJSONObject("urls").getString("regular");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;

    }
}
