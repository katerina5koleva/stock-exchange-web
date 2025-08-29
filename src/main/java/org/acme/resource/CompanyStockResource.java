package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.CompanyStockDto;
import org.acme.service.CompanyStockService;

@Path("/company-stocks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyStockResource {
    @Inject
    CompanyStockService companyStockService;

    @PUT
    @Path("/{companyId}")
    public CompanyStockDto getCompanyStock(@PathParam("companyId") Long companyId) {
        return companyStockService.getCompanyStock(companyId);
    }
}
