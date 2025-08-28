package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CompanyDto;
import org.acme.service.CompanyService;

import java.util.List;

@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {
    @Inject
    CompanyService companyService;

    @GET
    public List<CompanyDto> getAll() {
        return companyService.getAllCompanies();
    }

    @POST
    public Response create(CompanyDto companyDto) {
        CompanyDto createdCompany = companyService.addCompany(companyDto);
        return Response.status(Response.Status.CREATED)
                       .entity(createdCompany)
                       .build();
    }

    @PUT
    @Path("/{id}")
    public CompanyDto update(@PathParam("id") Long id, CompanyDto companyDto) {
        return companyService.updateCompany(id, companyDto);
    }
}
