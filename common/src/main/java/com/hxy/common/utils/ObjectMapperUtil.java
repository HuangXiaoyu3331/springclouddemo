package com.hxy.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean 映射工具类
 *
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: ObjectMapperUtil
 * @date 2019年08月09日 10:21:28
 */
@Slf4j
public class ObjectMapperUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper
                // 是否需要排序
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                // 忽略空bean转json的错误
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 取消默认转换timestamps形式
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 序列化的时候，过滤null属性
                .setSerializationInclusion(Include.NON_NULL)
                // 忽略在json字符串中存在，但在java对象中不存在对应属性的情况，防止错误
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                // 忽略空bean转json的错误
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    /**
     * 对象转json字符串
     *
     * @param obj 对象
     * @param <T> 对象泛型
     * @return json字符串
     */
    public static <T> String obj2Json(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("对象解析为json字符串异常", e);
        }
        return null;
    }

    /**
     * 对象转json字符串，并进行格式化
     *
     * @param obj 对象
     * @param <T> 对象泛型
     * @return 格式化后的json字符串
     */
    public static <T> String obj2JsonPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("对象解析为json字符串异常", e);
        }
        return null;
    }

    /**
     * json转对象
     * 这里有一个坑，例如List<User> list = stringObj(str,Class<List> data);这里会有问题
     *
     * @param jsonStr json字符串
     * @param clazz   对象
     * @param <T>     对象泛型
     * @return 反序列化后的对象
     */
    public static <T> T str2Obj(String jsonStr, Class<T> clazz) {
        if (StringUtils.isBlank(jsonStr) || null == clazz) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) jsonStr : objectMapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.warn("json字符串解析为对象异常", e);
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
    public static <T> T str2Obj(String str, TypeReference<T> typeReference) {
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
    public static <T> T str2Obj(String str, Class<?> collectionClass, Class<?>... elementClass) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClass);
        try {
            return objectMapper.readValue(str, javaType);
        } catch (IOException e) {
            log.warn("json字符串解析为对象错误", e);
            return null;
        }
    }

    /**
     * 对象转换 && 对象深复制
     *
     * @param obj   源对象
     * @param clazz 目标对象引用类型
     * @param <T>   返回值类型
     * @return T
     */
    public static <T> T convert(Object obj, TypeReference<T> clazz) {
        try {
            return objectMapper.convertValue(obj, clazz);
        } catch (Exception e) {
            log.warn("对象转换异常", e);
        }
        return null;
    }

    /**
     * 对象转换 && 对象深复制
     *
     * @param obj   源对象
     * @param clazz 目标对象类型
     * @param <T>   返回值类型
     * @return T
     */
    public static <T> T convert(Object obj, Class<T> clazz) {
        try {
            return objectMapper.convertValue(obj, clazz);
        } catch (Exception e) {
            log.warn("对象转换异常", e);
        }
        return null;
    }

    /**
     * 获取对象某个属性的值，并进行类型转换
     *
     * @param obj       源对像
     * @param fieldName 属性名称
     * @param clazz     目标类型
     * @param <T>       返回值类型
     * @return 属性的值
     */
    public static <T> T getValue(Object obj, String fieldName, Class<T> clazz) {
        try {
            return convert(Ognl.getValue(fieldName, obj), clazz);
        } catch (OgnlException e) {
            throw new RuntimeException("Ognl 获取字段'" + fieldName + "'的值失败", e);
        }
    }

    /**
     * 获取对象某个属性的值
     *
     * @param obj       源对像
     * @param fieldName 属性名称
     * @return 属性的值（Object类型）
     */
    public static Object getValue(Object obj, String fieldName) {
        return getValue(obj, fieldName, Object.class);
    }

    /**
     * 设置对象某个字段的值
     *
     * @param obj        目标对象
     * @param fieldName  属性名称
     * @param fieldValue 要设置的值
     */
    public static void setValue(Object obj, String fieldName, Object fieldValue) {
        try {
            Ognl.setValue(fieldName, obj, fieldValue);
        } catch (OgnlException e) {
            throw new RuntimeException("Ognl 设置字段'" + fieldName + "'的值'" + fieldValue + "'失败", e);
        }
    }

    /*
     * 注释掉下面代码的原因：
     * 对于需要全局反序列某个类型，建议直接配置ObjectMapperUtil，不要在代码中调用registerMapKeyClass，跟registerMapKeyClass方法，这样会导致
     * 还没执行到该方法的之前的类型都是按照默认的反序列化实现，而不是自定义的反序列化实现
     *
     * 如果想自定义粒度到某个类的对象属性上，可以在属性的set方法上使用@JsonDeserialize注解
     */
    /*

    private static final String CLASS_KEY = "@class";
    private static final String BASE_VALUE_KEY = "\"value\"";
    private static final String BASE_VALUE_NODE_KEY = "value";

    /**
     * 传入定制的Map key值的序列化与反序列化实现类
     *//*

    public static <T> void registerMapKeyClass(Class<T> mapKeyClazz, JsonSerializer<T> serializer, KeyDeserializer deserializer) {
        objectMapper.registerModule(new SimpleModule()
                .addKeySerializer(mapKeyClazz, serializer)
                .addKeyDeserializer(mapKeyClazz, deserializer)
        );
    }

    /**
     * 采用通用的Map key值的序列化与反序列化实现类
     *//*

    public static void registerMapKeyClass(Class<?> mapKeyClazz) {
        objectMapper
                .enableDefaultTypingAsProperty(DefaultTyping.NON_FINAL, CLASS_KEY)
                .registerModule(new SimpleModule()
                        .addKeySerializer(mapKeyClazz, new MapKeySerializer())
                        .addKeyDeserializer(mapKeyClazz, new MapKeyDeserializer())
                );
    }

    public static class MapKeyDeserializer extends KeyDeserializer {

        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = ObjectMapperUtil.objectMapper.readTree(key);
            JsonNode classNode = jsonNode.get(ObjectMapperUtil.CLASS_KEY);

            Class<?> clz;
            try {
                clz = Class.forName(classNode.asText());
            } catch (ClassNotFoundException e) {
                throw new IOException(e);
            }

            JsonNode valueNode = jsonNode.get(ObjectMapperUtil.BASE_VALUE_NODE_KEY);

            if (clz.equals(Integer.class)) {
                return Integer.parseInt(valueNode.asText());
            } else if (clz.equals(Boolean.class)) {
                return Boolean.parseBoolean(valueNode.asText());
            } else if (clz.equals(Byte.class)) {
                return Byte.parseByte(valueNode.asText());
            } else if (clz.equals(Short.class)) {
                return Short.parseShort(valueNode.asText());
            } else if (clz.equals(Long.class)) {
                return Long.parseLong(valueNode.asText());
            } else if (clz.equals(Float.class)) {
                return Float.parseFloat(valueNode.asText());
            } else if (clz.equals(Double.class)) {
                return Double.parseDouble(valueNode.asText());
            } else if (clz.equals(String.class)) {
                return valueNode.asText();
            } else if (clz.isEnum() || clz.isArray()) {
                return ObjectMapperUtil.objectMapper.readValue(valueNode.toString(), clz);
            }
            return ObjectMapperUtil.objectMapper.readValue(key, clz);
        }
    }


    public static class MapKeySerializer extends JsonSerializer<Object> {

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String keyString = objectMapper.writeValueAsString(value);
            boolean isFinal = Modifier.isFinal(value.getClass().getModifiers());

            StringBuilder key = new StringBuilder();
            if (isFinal) {
                key.append("{\"").append(ObjectMapperUtil.CLASS_KEY).append("\":\"").append(value.getClass().getName()).append("\",");

                if (value instanceof Integer || value instanceof Boolean
                        || value instanceof Byte || value instanceof Short
                        || value instanceof Long || value instanceof Float
                        || value instanceof Double || value instanceof String) {
                    key.append(ObjectMapperUtil.BASE_VALUE_KEY).append(":").append(keyString);
                } else if (value instanceof Enum) {
                    key.append(ObjectMapperUtil.BASE_VALUE_KEY).append(":").append(keyString);
                } else if (value.getClass().isArray()) {
                    key.append(ObjectMapperUtil.BASE_VALUE_KEY).append(":").append(keyString);
                } else if (keyString.startsWith("{")) {
                    key.append(keyString.substring(1, keyString.length() - 1));
                } else {
                    key.append(keyString);
                }

                key.append("}");
                keyString = key.toString();
            }

            gen.writeFieldName(keyString);
        }
    }
*/

}

