/**
 * ============LICENSE_START=======================================================
 * org.onap.aai
 * ================================================================================
 * Copyright © 2017-2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.aai.schemaservice.interceptors.post;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.onap.aai.logging.ErrorLogHelper;
import org.onap.aai.schemaservice.interceptors.AAIContainerFilter;
import org.onap.aai.schemaservice.interceptors.AAIHeaderProperties;
import org.onap.aai.util.AAIConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Priority(AAIResponseFilterPriority.RESPONSE_TRANS_LOGGING)
public class ResponseTransactionLogging extends AAIContainerFilter
    implements ContainerResponseFilter {

    private static final Logger TRANSACTION_LOGGER =
        LoggerFactory.getLogger(ResponseTransactionLogging.class);

    @Override
    public void filter(ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) throws IOException {

        this.transLogging(requestContext, responseContext);

    }

    private void transLogging(ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) {

        String logValue = AAIConfig.get("aai.transaction.logging", "true");
        String isGetTransactionResponseLoggingEnabled =
            AAIConfig.get("aai.transaction.logging.get", "false");

        String httpMethod = requestContext.getMethod();

        if (Boolean.parseBoolean(logValue)) {

            String transId = requestContext.getHeaderString(AAIHeaderProperties.TRANSACTION_ID);
            String fromAppId = requestContext.getHeaderString(AAIHeaderProperties.FROM_APP_ID);
            String fullUri = requestContext.getUriInfo().getRequestUri().toString();
            String requestTs =
                (String) requestContext.getProperty(AAIHeaderProperties.AAI_REQUEST_TS);

            String status = Integer.toString(responseContext.getStatus());

            String request = (String) requestContext.getProperty(AAIHeaderProperties.AAI_REQUEST);
            String response = this.getResponseString(responseContext);

            JsonObject logEntry = new JsonObject();
            logEntry.addProperty("transactionId", transId);
            logEntry.addProperty("status", status);
            logEntry.addProperty("rqstDate", requestTs);
            logEntry.addProperty("respDate", this.genDate());
            logEntry.addProperty("sourceId", fromAppId + ":" + transId);
            logEntry.addProperty("resourceId", fullUri);
            logEntry.addProperty("resourceType", httpMethod);
            logEntry.addProperty("rqstBuf", Objects.toString(request, ""));
            if (Boolean.parseBoolean(isGetTransactionResponseLoggingEnabled)
                || (!HttpMethod.GET.equals(httpMethod))) {
                logEntry.addProperty("respBuf", Objects.toString(response, ""));
            }

            try {
                TRANSACTION_LOGGER.debug(logEntry.toString());
            } catch (Exception e) {
                ErrorLogHelper.logError("AAI_4000", "Exception writing transaction log.");
            }
        }
    }

    private String getHttpServletResponseContentType() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletResponse response =
                ((ServletRequestAttributes) requestAttributes).getResponse();
            return response == null ? null : response.getContentType();
        }
        return null;
    }

    private String getResponseString(ContainerResponseContext responseContext) {
        JsonObject response = new JsonObject();
        response.addProperty("ID", responseContext.getHeaderString(AAIHeaderProperties.AAI_TX_ID));
        response.addProperty("Content-Type", getHttpServletResponseContentType());
        response.addProperty("Response-Code", responseContext.getStatus());
        response.addProperty("Headers", responseContext.getHeaders().toString());
        Optional<Object> entityOptional = Optional.ofNullable(responseContext.getEntity());
        if (entityOptional.isPresent()) {
            response.addProperty("Entity", entityOptional.get().toString());
        } else {
            response.addProperty("Entity", "");
        }
        return response.toString();
    }

}
