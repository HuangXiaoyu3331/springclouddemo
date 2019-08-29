package com.hxy.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


/**  
 * 属性文件工具类    
 * @ClassName: PropertyUtils    
 * @author 陈剑飞    
 * @date 2016年1月8日 下午5:18:24    
 * @version  v 1.0    
 */
@Slf4j
public class PropertyUtils {
	/**
	 * PropsUtil
	 */
	private static PropertyUtils instance;

	/**
	 * Properties
	 */
	private static Properties props = new Properties();

	/**
	 * PropsUtil Object
	 */
	private PropertyUtils(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		try {
			URL url = classLoader.getResource(fileName);
			if (url != null) {
				InputStream is = url.openStream();
				props.load(is);
				is.close();
			}
		} catch (Exception e) {
			log.debug("Load properties file failed!", e);
		}
	}
	
	/**
	 * Get Instance of PropsUtil
	 * 
	 * @return The instance of PropsUtil
	 */
	public static PropertyUtils getInstance(String fileName) {
		synchronized (PropertyUtils.class) {
			if (instance == null) {
				instance = new PropertyUtils(fileName);
			}
		}
		return instance;
	}
	
	/**
	 * If contains key
	 * 
	 * @param key
	 *            Key
	 * @return Boolean, false or true
	 */
	public boolean containsKey(String key) {
		return props.containsKey(key);
	}

	/**
	 * Get Value of key
	 * 
	 * @param key
	 *            Key
	 * @return The value of Key
	 */
	public String get(String key) {
		return props.getProperty(key);
	}

	/**
	 * Set value for key
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            The value of Key
	 */
	public void set(String key, String value) {
		props.setProperty(key, value);
	}

	/**
	 * Get a array of value of key
	 * 
	 * @param key
	 *            Key
	 * @return A array of value
	 */
	public String[] getArray(String key) {
		String value = get(key);
		if (value == null) {
			return new String[0];
		} else {
			return StringUtils.split(key,',');
		}
	}
}
