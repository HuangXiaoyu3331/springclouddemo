package com.hxy.common.bean;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ObjectMapperTestBean
 * @date 2019年08月09日 14:03:10
 */
@Data
@ToString
public class ObjectMapperTestBean {
    private Long id;
    private String name;
    private List<String> strList;
    private Map<String, Object> map;
    private List<ObjectMapperItem> beanList;
}
