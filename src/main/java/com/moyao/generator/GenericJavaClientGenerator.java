package com.moyao.generator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.SimpleJavaClientGenerator;

import com.moyao.generator.runtime.GenericDao;
import com.moyao.generator.runtime.NotSupportDelete;
import com.moyao.generator.runtime.NotSupportVersion;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class GenericJavaClientGenerator extends SimpleJavaClientGenerator {


    private final static String versionClz = NotSupportVersion.class.getName();
    private final static String deleteClz = NotSupportDelete.class.getName();


    public GenericJavaClientGenerator() {
        super(null);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.17", //$NON-NLS-1$
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);

        String  returnType = introspectedTable.getBaseRecordType();

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(GenericDao.class.getName()+"<"+returnType+">");
        interfaze.addSuperInterface(fqjt);
        interfaze.addImportedType(fqjt);

        if (!introspectedTable.getColumn(NotSupportVersion.fieldName).isPresent()) {
            interfaze.addImportedType(new FullyQualifiedJavaType(versionClz));
            interfaze.addSuperInterface(new FullyQualifiedJavaType(versionClz));
        }
        if (!introspectedTable.getColumn(NotSupportDelete.fieldName).isPresent()) {
            interfaze.addImportedType(new FullyQualifiedJavaType(deleteClz));
            interfaze.addSuperInterface(new FullyQualifiedJavaType(deleteClz));
        }

        if (!introspectedTable.getBLOBColumns().isEmpty()) {
            interfaze.addStaticImport(returnType+ ".BASE_COLUMN");
        }
        interfaze.addStaticImport(returnType+ ".ALL_COLUMN");
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addAnnotation("@Mapper");

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().clientGenerated(interfaze, introspectedTable)) {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    @Override
    public String getProject() {
        return context.getJavaClientGeneratorConfiguration().getTargetProject();
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new GenericXMLMapperGenerator();
    }
}
