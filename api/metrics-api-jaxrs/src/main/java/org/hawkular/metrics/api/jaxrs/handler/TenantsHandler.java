/*
 * Copyright 2014-2015 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.metrics.api.jaxrs.handler;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import static org.hawkular.metrics.api.jaxrs.util.ApiUtils.collectionToResponse;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.hawkular.metrics.api.jaxrs.ApiError;
import org.hawkular.metrics.api.jaxrs.observer.TenantCreatedObserver;
import org.hawkular.metrics.core.api.MetricsService;
import org.hawkular.metrics.core.api.Tenant;

import com.google.common.base.Throwables;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author Thomas Segismont
 */
@Path("/tenants")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Api(value = "/tenants", description = "Tenants related REST interface")
public class TenantsHandler {

    // TODO: add back retention settings

    @Inject
    private MetricsService metricsService;

    @POST
    @ApiOperation(value = "Create a new tenant. ", notes = "Clients are not required to create explicitly create a "
            + "tenant before starting to store metric data. It is recommended to do so however to ensure that there "
            + "are no tenant id naming collisions and to provide default data retention settings. ")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tenant has been succesfully created."),
            @ApiResponse(code = 400, message = "Missing or invalid retention properties. ",
                         response = ApiError.class),
            @ApiResponse(code = 409, message = "Given tenant id has already been created.",
                         response = ApiError.class),
            @ApiResponse(code = 500, message = "An unexpected error occured while trying to create a tenant.",
                         response = ApiError.class)
    })
    public void createTenant(
            @Suspended AsyncResponse asyncResponse, @ApiParam(required = true) Tenant params,
            @Context UriInfo uriInfo
    ) {
        if (params == null) {
            Response response = Response.status(Status.BAD_REQUEST).entity(new ApiError("Payload is empty")).build();
            asyncResponse.resume(response);
            return;
        }
        URI created = uriInfo.getBaseUriBuilder().path("/tenants").build();
        metricsService.createTenant(params).subscribe(new TenantCreatedObserver(asyncResponse, created));
    }

    @GET
    @ApiOperation(value = "Returns a list of tenants.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returned a list of tenants successfully."),
            @ApiResponse(code = 204, message = "No tenants were found."),
            @ApiResponse(code = 500, message = "Unexpected error occurred while fetching tenants.",
                         response = ApiError.class)
    })
    public void findTenants(@Suspended AsyncResponse asyncResponse) {
        metricsService.getTenants().toList().subscribe(
                tenants -> asyncResponse.resume(collectionToResponse(tenants)),
                error -> {
                    String msg = "Failed to perform operation due to an error: " + Throwables.getRootCause(error)
                                                                                             .getMessage();
                    asyncResponse.resume(Response.serverError().entity(new ApiError(msg)).build());
                }
        );
    }
}
