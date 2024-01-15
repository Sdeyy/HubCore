package hardling.us.hub.util.Files;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
@Getter
public class License {

    private final Gson gson = new Gson();
    private final Plugin plugin;
    private final String host;
    private final String licenseKey;
    private final String apiKey;
    private int statusCode;
    private String discordName;
    private String discordID;
    private String statusMsg;

    public boolean verify() {
        HttpPost post = new HttpPost(host);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("licensekey", licenseKey));
        urlParameters.add(new BasicNameValuePair("product", plugin.getName()));
        urlParameters.add(new BasicNameValuePair("version", plugin.getDescription().getVersion()));
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return false;
        }
        post.setHeader("Authorization", apiKey);
        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post)) {
            String data = EntityUtils.toString(response.getEntity());
            JsonObject json = gson.fromJson(data, JsonElement.class).getAsJsonObject();
            if(!json.has("status_msg") || !json.has("status_id")) return false;
            statusCode = json.get("status_code").getAsInt();
            statusMsg = json.get("status_msg").getAsString();
            if(json.get("status_overview").getAsString() == null) return false;
            discordName = json.get("clientname").getAsString(); // You can set discord_username too!
            discordID = json.get("discord_id").getAsString();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
