package com.moyao.generator;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public class XMLFindSupportGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        setAttribute(answer);
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$

        if (stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,"); //$NON-NLS-1$
        }

        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns()
                .iterator();
        while (iter.hasNext()) {
            sb.append(MyBatis3FormattingUtilities.getSelectListPhrase(iter
                    .next()));

            if (iter.hasNext()) {
                sb.append(", "); //$NON-NLS-1$
            }

            if (sb.length() > 80) {
                answer.addElement(new TextElement(sb.toString()));
                sb.setLength(0);
            }
        }

        if (sb.length() > 0) {
            answer.addElement(new TextElement(sb.toString()));
        }

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        setWhereAttribute(answer);

        parentElement.addElement(answer);
    }

    protected void setAttribute(XmlElement answer){
        answer.addAttribute(new Attribute("id", "findById"));
        answer.addAttribute(new Attribute("resultMap",  introspectedTable.getBaseResultMapId()));
        String parameterType  = introspectedTable.getPrimaryKeyColumns().get(0) .getFullyQualifiedJavaType().toString();
        answer.addAttribute(new Attribute("parameterType", parameterType));
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
