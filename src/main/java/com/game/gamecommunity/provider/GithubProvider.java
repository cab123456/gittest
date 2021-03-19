package com.game.gamecommunity.provider;

import com.alibaba.fastjson.JSON;
import com.game.gamecommunity.entity.AccessTokenDTO;
import com.game.gamecommunity.entity.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component//@component:标注一个类为Spring容器的Bean,（把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){//获取accesstoken授权码的方法
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string= response.body().string();

            String token=string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return  null;
    }
    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string= response.body().string();
            GitHubUser gitHubUser=JSON.parseObject(string,GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
