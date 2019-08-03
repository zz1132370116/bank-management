package com.zl.dc.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by 舍頭襘游泳 on 2018/12/17.
 */
@Data
@ConfigurationProperties(prefix = "sc.filter")
public class FilterProperties {

    private List<String> allowPaths;

}