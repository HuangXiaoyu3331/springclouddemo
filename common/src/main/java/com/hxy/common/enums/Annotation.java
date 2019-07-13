package com.hxy.common.enums;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: Annotation
 * @date 2019年07月12日 15:16:35
 */
public enum Annotation {

    DATA("@Data", "lombok.Data"),
    Mapper("@Mapper", "org.apache.ibatis.annotations.Mapper"),
    Param("@Param", "org.apache.ibatis.annotations.Param"),
    ApiModel("@ApiModel", "io.swagger.annotations.ApiModel"),
    ApiModelProperty("@ApiModelProperty", "io.swagger.annotations.ApiModelProperty"),
    JsonFormat("@JsonFormat", "com.fasterxml.jackson.annotation.JsonFormat");

    private String annotation;

    private String clazz;

    Annotation(String annotation, String clazz) {
        this.annotation = annotation;
        this.clazz = clazz;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getClazz() {
        return clazz;
    }
}
