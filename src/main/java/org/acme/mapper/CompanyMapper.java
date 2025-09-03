package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.acme.dto.CompanyDto;
import org.acme.model.Company;

@ApplicationScoped
public class CompanyMapper {
    public CompanyDto toDTO(Company company) {
        if (company == null){
            throw new NotFoundException("Company cannot be null.");
        }
        CompanyDto companyDto = new CompanyDto(
                company.getId(),
                company.getName(),
                company.getCountry(),
                company.getSymbol(),
                company.getCreatedAt(),
                company.getEmail(),
                company.getWebsite()
        );
        return companyDto;
    }
    public Company toEntity(CompanyDto companyDto) {
        if (companyDto == null){
            throw new NotFoundException("CompanyDto cannot be null.");
        }
        Company company = new Company(
                companyDto.getName(),
                companyDto.getCountry(),
                companyDto.getSymbol(),
                companyDto.getEmail(),
                companyDto.getWebsite()
        );
        return company;
    }
}
