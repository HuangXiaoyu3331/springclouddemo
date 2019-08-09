package com.hxy.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * json工具类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: JsonUtil
 * @date 2019年07月10日 18:21:28
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    //初始化objectMapper
    static {
        //----------序列化配置------------
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为一下的日期格式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //--------反序列化配置------------
        //忽略在json字符串中存在，但在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转json字符串
     *
     * @param obj 对象
     * @param <T> 对象泛型
     * @return json字符串
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("对象解析为json字符串错误", e);
                return null;
            }
        }
    }

    /**
     * 对象转json字符串，并进行格式化
     *
     * @param obj 对象
     * @param <T> 对象泛型
     * @return 格式化后的json字符串
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (IOException e) {
                e.printStackTrace();
                log.warn("对象解析为json字符串错误", e);
                return null;
            }
        }
    }

    /**
     * json转对象
     * 这里有一个坑，例如List<User> list = stringObj(str,Class<List> data);这里会有问题
     *
     * @param str   json字符串
     * @param clazz 对象
     * @param <T>   对象泛型
     * @return 反序列化后的对象
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isBlank(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            log.warn("json字符串解析为对象错误", e);
            return null;
        }
    }

    /**
     * 复杂对象反序列化
     * 使用例子List<User> list = JsonUtil.string2Obj(str, new TypeReference<List<User>>() {});
     *
     * @param str           json对象
     * @param typeReference 引用类型
     * @param <T>           返回值类型
     * @return 反序列化对象
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (IOException e) {
            log.warn("json字符串解析为对象错误", e);
            return null;
        }
    }


    /**
     * 复杂对象反序列化
     * 使用例子List<User> list = JsonUtil.string2Obj(str, List.class, User.class);
     *
     * @param str             json对象
     * @param collectionClass 定义的class类型
     * @param elementClass    子元素的class类型
     * @param <T>             返回值类型
     * @return 反序列化对象
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("json字符串解析为对象错误", e);
            return null;
        }
    }
}
