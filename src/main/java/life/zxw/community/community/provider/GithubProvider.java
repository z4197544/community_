package life.zxw.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.zxw.community.community.dto.AccessTokenDTO;
import life.zxw.community.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string =  response.body().string();
                String split = string.split("&")[0].split("=")[1];
                return split;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

    public GitHubUser getuser(String accesstoken) {
        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token="+ accesstoken)
                    .addHeader("Accept","application/json")
                    .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GitHubUser gitHubUser = JSON.parseObject(string,GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}




