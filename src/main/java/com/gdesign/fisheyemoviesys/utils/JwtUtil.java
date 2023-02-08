package com.gdesign.fisheyemoviesys.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ycy
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {
    /**
     * 过期时间
     */
    private long expire;
    /**
     * 加密算法所需的密钥
     */
    private String secret;
    /**
     * 头部
     * 包含令牌类型type和所使用的的加密算法
     */
    private String header;

    //生成JWT
    public String generateToken(String username) {

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        //这里其实就是new一个JwtBuilder，设置jwt的body
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                //jwt的签发时间
                .setIssuedAt(nowDate)
                //过期时间（这里是7天过期
                .setExpiration(expireDate)
                //设置签名使用的签名算法和签名使用的秘钥
                //注意这里是签名的加密算法，不是登录时的密码加密算法
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 解析JWT
    public Claims getClaimsByToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    // 判断JWT是否过期
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
