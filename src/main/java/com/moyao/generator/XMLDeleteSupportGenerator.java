package com.moyao.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class XMLDeleteSupportGenerator extends AbstractXmlElementGenerator {

    protected boolean supportDeleted;

    public XMLDeleteSupportGenerator(boolean supportDeleted) {
        this.supportDeleted = supportDeleted;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        {
            XmlElement answer = supportDeleted ? new XmlElement("update") : new XmlElement("delete");

            setAttribute(answer);
            context.getCommentGenerator().addComment(answer);

            StringBuilder sb = new StringBuilder();

            if (supportDeleted) {
                sb.append("update  ");
            } else {
                sb.append("delete from ");
            }

            sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
            answer.addElement(new TextElement(sb.toString()));

            if (supportDeleted) {
                sb.setLength(0);
                sb.append("set deleted = 1, dx_modified = now()");
                answer.addElement(new TextElement(sb.toString()));
            }

            setWhereAttribute(answer);
            parentElement.addElement(answer);
        }
    }

    protected void setAttribute(XmlElement answer){
        answer.addAttribute(new Attribute("id", introspectedTable.getDeleteByPrimaryKeyStatementId()));
        String parameterClass = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
        answer.addAttribute(new Attribute("parameterType", parameterClass));
    }

    protected void setWhereAttribute(XmlElement answer){
        StringBuilder sb = new StringBuilder();
        sb.append(" where ");
        IntrospectedColumn introspectedColumn = introspectedTable.getColumn("id").get();
        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        sb.append(" = "); //$NON-NLS-1$
        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
        answer.addElement(new TextElement(sb.toString()));
    }

}
