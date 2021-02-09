package com.moyao.generator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.codegen.mybatis3.model.SimpleModelGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getJavaBeansField;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class GenericModelGenerator extends SimpleModelGenerator {

    public GenericModelGenerator(String project) {
        super(project);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.8", table.toString()));
        Plugin plugins = context.getPlugins();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }

        addModelClassCommentAndAnnotation(topLevelClass, introspectedTable);

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();

        String rootClass = getRootClass();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (RootClassInfo.getInstance(rootClass, warnings)
                    .containsProperty(introspectedColumn)) {
                continue;
            }

            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass,
                    introspectedColumn, introspectedTable,
                    Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addField(field);
                topLevelClass.addImportedType(field.getType());
            }
        }

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass,
                introspectedTable)) {
            answer.add(topLevelClass);
        }

        if (!introspectedTable.getBLOBColumns().isEmpty()) {
           setColumns(topLevelClass, introspectedTable.getBaseColumns(), "BASE_COLUMN");
        }
        setColumns(topLevelClass, introspectedTable.getAllColumns(), "ALL_COLUMN");

        return answer;
    }

    private void setColumns(TopLevelClass topLevelClass, List<IntrospectedColumn> columns, String name){

       String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();

        StringBuilder sb = new StringBuilder();

        for (IntrospectedColumn column : columns) {

            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(column.getActualColumnName());
        }

        Field field = new Field(name, new FullyQualifiedJavaType("String"));
        field.setFinal(true);
        field.setInitializationString("\" select " + sb.toString() + " from "+ tableName +"\"");
        field.setStatic(true);
        field.setVisibility(JavaVisibility.PUBLIC);
        topLevelClass.addField(field);
    }

    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;
        String rootClass = getRootClass();
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        } else {
            superClass = null;
        }

        return superClass;
    }

    private void addModelClassCommentAndAnnotation(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        String remarks = introspectedTable.getRemarks();
        if (StringUtility.stringHasValue(remarks)) {
            topLevelClass.addJavaDocLine("/**");

            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for (String remarkLine : remarkLines) {
                topLevelClass.addJavaDocLine(" *   " + remarkLine);
            }
            topLevelClass.addJavaDocLine(" */");
        }

        topLevelClass.addImportedType("lombok.Builder");
        topLevelClass.addImportedType("lombok.Data");
        topLevelClass.addImportedType("lombok.EqualsAndHashCode");
        topLevelClass.addImportedType("lombok.ToString");
        topLevelClass.addImportedType("lombok.NoArgsConstructor");
        topLevelClass.addImportedType("lombok.AllArgsConstructor");

        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@EqualsAndHashCode(callSuper=true)");
        topLevelClass.addAnnotation("@ToString(callSuper=true)");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        topLevelClass.addAnnotation("@NoArgsConstructor");
    }
}
