package com.moyao.generator;

import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import com.moyao.generator.runtime.NotSupportVersion;

public class XMLUpdateSupportGenerator extends AbstractXmlElementGenerator {

    private boolean updateWithVersion;

    public XMLUpdateSupportGenerator(boolean updateWithVersion) {
        this.updateWithVersion = updateWithVersion;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        IntrospectedColumn versionColumn = introspectedTable.getColumn(NotSupportVersion.fieldName).orElse(null);
        if (versionColumn == null && updateWithVersion) {
            return;
        }

        String idAttr = updateWithVersion ?  "updateByVersion" : "update";

        XmlElement answer = new XmlElement("update");

        answer.addAttribute(new Attribute("id", idAttr));
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("set ");
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);

        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns()).iterator();

        IntrospectedColumn lastIntrospectedColumn = null;

        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            if (introspectedColumn.getActualColumnName().equalsIgnoreCase("dx_modified")) {
                lastIntrospectedColumn = introspectedColumn;
                continue;
            }

            XmlElement ifXmlElement = new XmlElement("if");
            ifXmlElement.addAttribute(new Attribute("test", introspectedColumn.getJavaProperty(null) + " !=null"));

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(',');

            ifXmlElement.addElement(new TextElement(sb.toString()));
            answer.addElement(ifXmlElement);

            sb.setLength(0);
            OutputUtilities.xmlIndent(sb, 1);
        }

        assert lastIntrospectedColumn != null;

        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(lastIntrospectedColumn));
        sb.append(" = ");
        sb.append(MyBatis3FormattingUtilities.getParameterClause(lastIntrospectedColumn));
        answer.addElement(new TextElement(sb.toString()));

        List<IntrospectedColumn> columns = introspectedTable.getPrimaryKeyColumns();

        if(updateWithVersion){
            columns.add(versionColumn);
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : columns) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        parentElement.addElement(answer);
    }

}
