package com.moyao.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class XMLFindListSupportGenerator  extends XMLFindSupportGenerator{

    @Override
    protected void setAttribute(XmlElement answer){
        answer.addAttribute(new Attribute("id", "findByIdList"));
        answer.addAttribute(new Attribute("resultMap",  introspectedTable.getBaseResultMapId()));
        String parameterType  = introspectedTable.getPrimaryKeyColumns().get(0) .getFullyQualifiedJavaType().toString();
        answer.addAttribute(new Attribute("parameterType", parameterType));
    }

    @Override
    protected void setWhereAttribute(XmlElement answer){
        IntrospectedColumn introspectedColumn = introspectedTable.getColumn("id").get();
        answer.addElement(new TextElement("where " + MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn) + " in"));
        XmlElement outerForEachElement = new XmlElement("foreach");
        outerForEachElement.addAttribute(new Attribute("item", "id"));
        outerForEachElement.addAttribute(new Attribute("collection", "idList"));
        outerForEachElement.addAttribute(new Attribute("separator", ","));
        outerForEachElement.addAttribute(new Attribute("open", "("));
        outerForEachElement.addAttribute(new Attribute("close", ")"));
        outerForEachElement.addElement(new TextElement(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn)));
        answer.addElement(outerForEachElement);
    }
}
