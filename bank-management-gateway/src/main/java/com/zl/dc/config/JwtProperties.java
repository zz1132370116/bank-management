package com.zl.dc.config;



import com.zl.dc.util.RasUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * Created by 舍頭襘游泳 on 2018/12/17.
 */
@Data
@ConfigurationProperties(prefix = "sc.jwt")
public class JwtProperties {

    private String pubKeyPath;// 公钥

    private PublicKey publicKey; // 公钥


    @PostConstruct
    public void init(){

            // 获取公钥和私钥
        try {
            this.publicKey = RasUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}