package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CompanyDto;
import org.acme.mapper.CompanyMapper;
import org.acme.model.Company;
import org.acme.repository.CompanyRepository;

import java.util.List;

@ApplicationScoped
public class CompanyService {
    @Inject
    CompanyRepository companyRepository;
    @Inject
    CompanyMapper companyMapper;

    public List<CompanyDto> getAllCompanies() {
        return companyRepository.listAll()
                                .stream()
                                .map(companyMapper::toDTO)
                                .toList();
    }
    @Transactional
    public CompanyDto addCompany(CompanyDto companyDto) {
        if (companyRepository.findBySymbol(companyDto.getSymbol()) != null){
            throw new WebApplicationException("Company with this symbol already exists", Response.Status.CONFLICT);
        }
        Company company = companyMapper.toEntity(companyDto);
        companyRepository.persist(company);
        companyRepository.flush();
        return companyMapper.toDTO(company);
    }

    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id);
        if (company == null) {
            throw new NotFoundException();
        }
        return companyMapper.toDTO(company);
    }

    @Transactional
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        Company updateCompany = companyRepository.findById(id);
        if (updateCompany == null) {
            throw new NotFoundException("Company with id " + id + " not found");
        }
        Company symbolCompany = companyRepository.findBySymbol(companyDto.getSymbol());
        if (symbolCompany != null && !symbolCompany.getId().equals(updateCompany.getId())) {
            throw new WebApplicationException("Company with this symbol already exists", Response.Status.CONFLICT);
        }
        if (companyDto .getName() != null) updateCompany.setName(companyDto.getName());
        if (companyDto.getCountry() != null) updateCompany.setCountry(companyDto.getCountry());
        if (companyDto.getSymbol() != null) updateCompany.setSymbol(companyDto.getSymbol());
        if (companyDto.getWebsite() != null) updateCompany.setWebsite(companyDto.getWebsite());
        if (companyDto.getEmail() != null) updateCompany.setEmail(companyDto.getEmail());
        companyRepository.flush();
        return companyMapper.toDTO(updateCompany);
    }

}
