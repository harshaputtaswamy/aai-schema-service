/**
 * ============LICENSE_START=======================================================
 * org.onap.aai
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.aai.schemagen.genxsd;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XSDJavaType extends XSDElement {

    public XSDJavaType(Element javaTypeElement) {
        super(javaTypeElement);
    }

    /*
     * public XSDJavaType(XSDElement javaTypeElement, StringBuffer pathSb, StringBuffer
     * definitionsSb,
     * StringBuffer pathParams) {
     * super(javaTypeElement);
     * this.pathSb = pathSb;
     * this.definitionsSb = definitionsSb;
     * this.pathParams = pathParams;
     * }
     */
    public String getItemName() {
        NodeList parentNodes = this.getElementsByTagName("java-attributes");
        if (parentNodes.getLength() == 0) {
            return null;
        }
        Element parentElement = (Element) parentNodes.item(0);
        NodeList xmlElementNodes = parentElement.getElementsByTagName("xml-element");
        XSDElement xmlElementElement = new XSDElement((Element) xmlElementNodes.item(0));
        return xmlElementElement.getAttribute("name");
    }

    public String getArrayType() {
        NodeList parentNodes = this.getElementsByTagName("java-attributes");
        if (parentNodes.getLength() == 0) {
            return null;
        }
        Element parentElement = (Element) parentNodes.item(0);
        NodeList xmlElementNodes = parentElement.getElementsByTagName("xml-element");
        XSDElement xmlElementElement = new XSDElement((Element) xmlElementNodes.item(0));
        if (xmlElementElement.hasAttribute("container-type")
            && xmlElementElement.getAttribute("container-type").equals("java.util.ArrayList")) {
            return xmlElementElement.getAttribute("name");
        }
        return null;
    }
}
