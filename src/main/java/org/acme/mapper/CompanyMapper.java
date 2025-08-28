package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.dto.CompanyDto;
import org.acme.model.Company;

@ApplicationScoped
public class CompanyMapper {
    public CompanyDto toDTO(Company company) {
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
