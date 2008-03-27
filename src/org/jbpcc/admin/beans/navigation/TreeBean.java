/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpcc.admin.beans.navigation;

import java.io.File;
import java.io.FileInputStream;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.jbpcc.admin.components.tree.TreeNodeSelectionHandler;
import org.jbpcc.admin.components.tree.UrlNodeUserObject;
import org.jbpcc.admin.constants.SessionObjectKey;
import org.jbpcc.admin.jsf.JsfUtil;
import org.jbpcc.admin.util.ResourceBundleProvider;
import org.jbpcc.admin.util.ResourceBundleUtil;

/**
 * The backing bean for the tree navigation menu. The tree is configured via the configuration file
 * <code>treeMenu.xml</code>. Note that a user's access rights is only checked upon session creation. Should a user's
 * access rights change while the user is logged in, this menu will not be recreated until a subsequent login.
 */
public class TreeBean implements TreeNodeSelectionHandler {

    private static final String TREE_MENU_CONFIG_FILE = File.separator + "WEB-INF" + File.separator + "treeMenu.xml";
    private static final String ELEM_APP = "appMenu";
    private static final String ELEM_MODULE = "module";
    private static final String ELEM_PAGE = "page";
    private static final String ATTR_ICON = "icon";
    private static final String ATTR_URL = "url";
    private static final String ATTR_LABEL = "label";

    private DefaultTreeModel model;
    private UrlNodeUserObject currentNode;
    private UrlNodeUserObject rootNodeObject;
    private ResourceBundleProvider resourceBundle = new ResourceBundleUtil();

    /**
     * Initialises tree root and calls method to build tree of configuration schema changes
     */
    public TreeBean() {

        DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode();
        UrlNodeUserObject rootObject = new UrlNodeUserObject(rootTreeNode, this);
        rootObject.setText("Empty root");
        rootObject.setExpanded(true);

        Document dom = readDOM();
        Element docElement = dom.getDocumentElement(); // get root element

        if (!docElement.getNodeName().equals(ELEM_APP)) {
            throw new IllegalArgumentException("Illegal tree menu configuration");
        }

        rootObject.setUrl(docElement.getAttribute(ATTR_URL));
        rootObject.setIconUrl(docElement.getAttribute(ATTR_ICON));
        rootObject.setSelectedNode(true);
        rootTreeNode.setUserObject(rootObject);
        rootNodeObject = (UrlNodeUserObject) rootTreeNode.getUserObject();
        rootNodeObject.setSelectedNode(true);

        model = new DefaultTreeModel(rootTreeNode);

        buildTree(rootTreeNode, docElement);
    }

    /**
     * Sets current node as selected when a tree node is selected.
     * 
     * @param newNode
     *            the current node selection
     */
    public void setSelectedNode(UrlNodeUserObject newNode) {
        rootNodeObject.setSelectedNode(false);

        if (currentNode != null) {
            currentNode.setSelectedNode(false);
        }
        currentNode = newNode;
        currentNode.setSelectedNode(true);
    }

    public DefaultTreeModel getModel() {
        return model;
    }

    private Document readDOM() {
        Document dom = null;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String filename = servletContext.getRealPath(TREE_MENU_CONFIG_FILE);

        try {
            FileInputStream istream = new FileInputStream(filename);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            dom = docBuilder.parse(istream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dom;
    }

    private void buildTree(DefaultMutableTreeNode rootTreeNode, Element docElement) {
        ((UrlNodeUserObject) rootTreeNode.getUserObject()).setText(getLabelResource(docElement));

        // get nodelist of elements & loop through items
        NodeList nodeList = docElement.getElementsByTagName(ELEM_MODULE);
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element itemElement = (Element) nodeList.item(i);
                DefaultMutableTreeNode itemNode = createParentMenuItem(itemElement);

                if (itemElement.hasChildNodes()) {
                    NodeList childNodeList = itemElement.getChildNodes();
                    for (int j = 0; j < childNodeList.getLength(); j++) {
                        if (childNodeList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element childElement = (Element) childNodeList.item(j);
                            if (ELEM_PAGE.equals(childElement.getNodeName())) {
                                createChildMenuItem(itemNode, childElement);
                            }
                        }
                    }
                }
                rootTreeNode.add(itemNode);
            }
        }
    }

    private DefaultMutableTreeNode createParentMenuItem(Element itemElement) {
        DefaultMutableTreeNode menuItem = new DefaultMutableTreeNode();
        UrlNodeUserObject menuObject = new UrlNodeUserObject(menuItem, this);

        if (itemElement.hasChildNodes()) {
            menuObject.setExpanded(true);
        } else {
            menuObject.setLeaf(true);
        }

        menuObject.setText(getLabelResource(itemElement));

        String value = itemElement.getAttribute(ATTR_URL);
        if (StringUtils.isNotBlank(value)) {
            menuObject.setUrl(value);
        }

        value = itemElement.getAttribute(ATTR_ICON);
        if (StringUtils.isNotBlank(value)) {
            menuObject.setIconUrl(value);
        }

     
        menuItem.setUserObject(menuObject);

        return menuItem;
    }

    private void createChildMenuItem(DefaultMutableTreeNode parentNode, Element childElement) {
        DefaultMutableTreeNode subNode = createParentMenuItem(childElement);
        NodeList childNodeList = childElement.getChildNodes();

        if (childElement.hasChildNodes()) {
            childNodeList = childElement.getChildNodes();
            for (int i = 0; i < childNodeList.getLength(); i++) {
                if (childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    createChildMenuItem(subNode, (Element) childNodeList.item(i));
                }
            }
        }

        parentNode.add(subNode);
    }

    private String getLabelResource(Element labelElement) {
        return resourceBundle.getLocalizedString(labelElement.getAttribute(ATTR_LABEL));
    }
}
