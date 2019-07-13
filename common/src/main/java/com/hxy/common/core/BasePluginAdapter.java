package com.hxy.common.core;

import com.hxy.common.enums.Annotation;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 黄晓宇
 * @version v1.0
 * @ClassName: BasePluginAdapter
 * @date 2019年07月12日 15:14:41
 */
public class BasePluginAdapter extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        classAnnotation(topLevelClass);
        Set<FullyQualifiedJavaType> set = new HashSet<>();
        set.add(new FullyQualifiedJavaType(Annotation.DATA.getClazz()));
        topLevelClass.addImportedTypes(set);

        topLevelClass.addAnnotation(Annotation.DATA.getAnnotation() + "()");
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 类注释生成
     * @param topLevelClass
     *
     */
    private static void classAnnotation(TopLevelClass topLevelClass) {
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine("* @author 代码生成插件");
        topLevelClass.addJavaDocLine("* @version v1.0");
        topLevelClass.addJavaDocLine("* @ClassName: " + topLevelClass.getType().getShortName());
        topLevelClass.addJavaDocLine("* @date: " + new Date());
        topLevelClass.addJavaDocLine("*/");
    }
}
