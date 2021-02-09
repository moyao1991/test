package com.moyao.generator;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import com.moyao.generator.runtime.NotSupportDelete;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class GenericXMLMapperGenerator extends SimpleXMLMapperGenerator {

    @Override
    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString())); //$NON-NLS-1$
        XmlElement answer = new XmlElement("mapper"); //$NON-NLS-1$
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", //$NON-NLS-1$
                namespace));

        context.getCommentGenerator().addRootComment(answer);

        addResultMapElement(answer);
        addDeleteByPrimaryKeyElement(answer);
        addInsertElement(answer);
        addUpdateByPrimaryKeyElement(answer);
        addSelectByPrimaryKeyElement(answer);

        return answer;
    }

    @Override
    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator deleteElement = new XMLDeleteSupportGenerator(introspectedTable.getColumn(NotSupportDelete.fieldName).isPresent());
        initializeAndExecuteGenerator(deleteElement, parentElement);
        AbstractXmlElementGenerator deleteListElement = new XMLDeleteListSupportGenerator(introspectedTable.getColumn(NotSupportDelete.fieldName).isPresent());
        initializeAndExecuteGenerator(deleteListElement, parentElement);
    }

    @Override
    protected void addInsertElement(XmlElement parentElement) {
        AbstractXmlElementGenerator insertElement = new XMLInsertSupportGenerator();
        initializeAndExecuteGenerator(insertElement, parentElement);
        AbstractXmlElementGenerator insertListElement = new XMLInsertListSupportGenerator();
        initializeAndExecuteGenerator(insertListElement, parentElement);
    }

    @Override
    protected void addUpdateByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator updateElement = new XMLUpdateSupportGenerator(false);
        initializeAndExecuteGenerator(updateElement, parentElement);
        AbstractXmlElementGenerator updateVersionElement = new XMLUpdateSupportGenerator(true);
        initializeAndExecuteGenerator(updateVersionElement, parentElement);
    }

    @Override
    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator findElement = new XMLFindSupportGenerator();
        initializeAndExecuteGenerator(findElement, parentElement);
        AbstractXmlElementGenerator findListElement = new XMLFindListSupportGenerator();
        initializeAndExecuteGenerator(findListElement, parentElement);
    }
}
