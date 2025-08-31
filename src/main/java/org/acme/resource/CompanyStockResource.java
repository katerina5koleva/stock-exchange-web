package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.CompanyStockDto;
import org.acme.service.CompanyService;
import org.acme.service.CompanyStockService;

@Path("/company-stocks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyStockResource {
    private final CompanyStockService companyStockService;

    public CompanyStockResource(CompanyStockService companyStockService) {
        this.companyStockService = companyStockService;
    }

    @PUT
    @Path("/{companyId}")
    public CompanyStockDto getCompanyStock(@PathParam("companyId") Long companyId) {
        return companyStockService.getCompanyStock(companyId);
    }
}
