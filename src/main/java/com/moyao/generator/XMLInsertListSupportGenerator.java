package com.moyao.generator;


import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class XMLInsertListSupportGenerator extends XMLInsertSupportGenerator {

    @Override
    protected void setAttribute(XmlElement answer) {
        answer.addAttribute(new Attribute("id", "insertList"));
        answer.addAttribute(new Attribute("parameterType", "java.util.List"));
    }

    @Override
    protected void setValuesAttribute(XmlElement answer) {
        XmlElement outerForEachElement = new XmlElement("foreach");
        outerForEachElement.addAttribute(new Attribute("item", "record"));
        outerForEachElement.addAttribute(new Attribute("collection", "records"));
        outerForEachElement.addAttribute(new Attribute("separator", ","));
        setValuesWithPrefix("record.", outerForEachElement);
        answer.addElement(outerForEachElement);
    }
}
