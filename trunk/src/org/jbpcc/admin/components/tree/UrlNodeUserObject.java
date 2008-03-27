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

package org.jbpcc.admin.components.tree;


import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import com.icesoft.faces.component.tree.IceUserObject;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

import org.jbpcc.admin.beans.navigation.TreeBean;

/**
 * The UrlNodeUserObject object is responsible for storing extra data for a url. The url along with text is bound to a
 * ice:commanLink object which will launch a new browser window pointed to the url.
 */
public class UrlNodeUserObject extends IceUserObject {

    private String url; // url to show when a node is clicked
    private String iconUrl;
    private boolean selectedNode;
    private TreeNodeSelectionHandler treeBean;

    public TreeNodeSelectionHandler getTreeBean() {
        return treeBean;
    }

    public void setTreeBean(TreeNodeSelectionHandler treeBean) {
        this.treeBean = treeBean;
    }

    public UrlNodeUserObject(DefaultMutableTreeNode wrapper, TreeBean treeBean) {
        super(wrapper);
        this.treeBean = treeBean;
    }

    /**
     * Gets the url value of this IceUserObject.e
     * 
     * @return string representing a URL.
     */
    public void getUrl(ActionEvent action) {
        // System.err.println(">>> REDIRECT URL ="+ url);
        if (url != null) {
            treeBean.setSelectedNode(this);
            PersistentFacesState.getInstance().redirectTo(url);
        }
    }

    /**
     * Sets the URL.
     * 
     * @param url
     *            a valid URL with protocol information such as http://icesoft.com
     */
    public void setUrl(String url) {
        if (url != null || url.length() > 0) {
            this.url = url;
        }
    }

    /**
     * Gets the selected tree menu node.
     * 
     * @return status of selected node
     */
    public boolean isSelectedNode() {
        return selectedNode;
    }

    /**
     * Sets the selected tree menu node.
     * 
     * @param selectedNode
     *            the status of the node selected.
     */
    public void setSelectedNode(boolean selectedNode) {
        this.selectedNode = selectedNode;
    }

    /**
     * Gets the icon url of each clickable node.
     * 
     * @return icon url
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * Sets the icon url for each clickable node.
     * 
     * @param iconUrl
     *            the path url of the icon
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * Gets the availability status of the tree menu icon
     * 
     * @return icon status
     */
    public boolean isRenderIcon() {
        if (iconUrl != null && iconUrl.length() > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Gets the clickable status of the node.
     * 
     * @return clickable status
     */
    public boolean isClickable() {
        if (this.url != null) {
            return true;
        } else {
            return false;
        }
    }

}
