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

package org.onap.aai.schemaservice.interceptors.post;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;

import org.onap.aai.schemaservice.interceptors.AAIContainerFilter;
import org.onap.aai.schemaservice.interceptors.AAIHeaderProperties;

@Priority(AAIResponseFilterPriority.HEADER_MANIPULATION)
public class ResponseHeaderManipulation extends AAIContainerFilter
    implements ContainerResponseFilter {

    private static final String DEFAULT_XML_TYPE = MediaType.APPLICATION_XML;
    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    @Override
    public void filter(ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) throws IOException {

        updateResponseHeaders(requestContext, responseContext);

    }

    private void updateResponseHeaders(ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) {

        responseContext.getHeaders().add(AAIHeaderProperties.AAI_TX_ID,
            requestContext.getProperty(AAIHeaderProperties.AAI_TX_ID));

        String responseContentType = responseContext.getHeaderString(CONTENT_TYPE_HEADER);

        if (responseContentType == null) {
            String acceptType = requestContext.getHeaderString("Accept");
            if (acceptType == null || "*/*".equals(acceptType)) {
                responseContext.getHeaders().putSingle(CONTENT_TYPE_HEADER, DEFAULT_XML_TYPE);
            } else {
                responseContext.getHeaders().putSingle(CONTENT_TYPE_HEADER, acceptType);
            }
        }

    }

}
