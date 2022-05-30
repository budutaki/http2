import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;


public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        CloseableHttpClient httpClient = getCloseableHttpClient();
        Data data = jsonToData(getData(httpClient, "https://api.nasa.gov/planetary/apod?api_key=CnOMJsI1qRk13CZOQKty6Il18AqjGx7sdv4n7awD"));
        downloadFile(httpClient, data.getUrl());
    }

    private static CloseableHttpClient getCloseableHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

    private static void downloadFile(CloseableHttpClient httpClient, String uri) throws IOException {
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);
        response.getEntity().writeTo(new FileOutputStream("EclipseRays_Bouvier_960.jpg"));
    }


    private static String getData(CloseableHttpClient httpClient, String uri) throws IOException {
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);
        return new String(response.getEntity().getContent().readAllBytes(), Charset.defaultCharset());
    }

    private static Data jsonToData(String json) throws ParseException {
        JSONParser jp = new JSONParser();
        GsonBuilder gb = new GsonBuilder();
        Gson gson = gb.create();
        JSONObject jsonObject = (JSONObject) jp.parse(json);
        return gson.fromJson(String.valueOf(jsonObject), Data.class);
    }
}

