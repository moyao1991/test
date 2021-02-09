package com.moyao.generator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;

/**
 * 自定插件
 */
public class IntrospectedTableMyBatis3GenericImpl extends IntrospectedTableMyBatis3SimpleImpl {

    // 统一方法名
    @Override
    protected void calculateXmlAttributes() {
        super.calculateXmlAttributes();
        setSelectByPrimaryKeyStatementId("findById");
        setDeleteByPrimaryKeyStatementId("delete");
        setUpdateByPrimaryKeyStatementId("update");
    }

    // 支持Lombok注解
    @Override
    protected void calculateJavaModelGenerators(List<String> warnings,
                                                ProgressCallback progressCallback) {

        AbstractJavaGenerator javaGenerator = new GenericModelGenerator(getModelProject());
        initializeAbstractGenerator(javaGenerator, warnings,
                progressCallback);
        javaGenerators.add(javaGenerator);
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    false, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }

        return answer;
    }
}
