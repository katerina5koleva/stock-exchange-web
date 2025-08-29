package org.acme.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.acme.dto.FinnhubStockDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "finnhub-service")
public interface FinnhubService {
    @GET
    @Path("/stock/profile2")
    @Produces(MediaType.APPLICATION_JSON)
    FinnhubStockDto getCompanyStock(
            @QueryParam("symbol") String symbol,
            @HeaderParam("X-Finnhub-Token") String token
    );
}
