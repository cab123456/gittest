package com.game.gamecommunity.controller;

import com.game.gamecommunity.entity.AccessTokenDTO;
import com.game.gamecommunity.entity.GitHubUser;
import com.game.gamecommunity.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Controller
 * 1.作用：控制器类，处理由DispatcherServlet分发的请求，它把用户请求的数据经过业务处理层处理之后封装成一个Model ，然后再把该Model返回给对应的View进行展示
 * 2.相关：需要在spring的配置中指定controller的扫描路径范围
 * 3.使用方法：标记在类上即可，一般会和@RequestMapping组合使用，即给这个类访问加入一个根路径。
 * 常用的Controller层最终返回结果：
 *     1.返回json数据。可以搭配@ResponseBody注解使用
 *     2.返回字符串路径。走视图解析器
 */
@Controller//使用@Controller标记一个类是Controller
public class LoginController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client_id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    /**
     * 使用@RequestMapping和@RequestParam等一些注解用以定义URL
     * 请求和Controller方法之间的映射，这样的Controller就能被外界访问到。
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,//接收code参数
                           @RequestParam(name="state")String state, //接收state参数
                            HttpServletRequest request){
        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken=githubProvider.getAccessToken(accessTokenDTO);
        GitHubUser user=githubProvider.getUser(accessToken);
        if (user !=null){
            //登录成功
            request.getSession().setAttribute("user",user);//用session保留登录用户的用户名信息显示在登录按钮上
            return  "redirect:/";
        }else {
            //登录失败
            return  "redirect:/";
        }


    }
}
