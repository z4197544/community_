package life.zxw.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.zxw.community.community.dto.AccessTokenDTO;
import life.zxw.community.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    //    获取授权登陆码
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String split = string.split("&")[0].split("=")[1];
            return split;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //    获取用户信息
    public GitHubUser getuser(String accesstoken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accesstoken)
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
//            string这时已拥有所有的信息，这时候githubuser如果想获取更多的信息，就可以增加相应的属性。
            GitHubUser gitHubUser = JSON.parseObject(string, GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}




