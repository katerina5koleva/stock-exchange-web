package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.CompanyStockDto;
import org.acme.dto.FinnhubStockDto;
import org.acme.model.Company;
import org.acme.model.CompanyStock;
import org.acme.repository.CompanyRepository;
import org.acme.repository.CompanyStockRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;

@ApplicationScoped
public class CompanyStockService {
    @Inject
    CompanyRepository companyRepository;
    @Inject
    CompanyStockRepository companyStockRepository;
    @Inject
    CompanyService companyService;
    @Inject
    @RestClient
    FinnhubService finnhubService;

    @ConfigProperty(name = "finnhub.api.key")
    String apiKey;

    private CompanyStock getOrCreateTodayStock(Company company) {
        LocalDate today = LocalDate.now();
        CompanyStock stock = companyStockRepository.findByCompanyIdAndCurrentDate(company.getId(), today);
        if (stock != null){
            return stock;
        }
        FinnhubStockDto finnhubStock = finnhubService.getCompanyStock(company.getSymbol(), apiKey);
        CompanyStock newStock = new CompanyStock(
                company,
                finnhubStock.getMarketCapitalization(),
                finnhubStock.getShareOutstanding(),
                LocalDate.now()
        );
        companyStockRepository.persist(newStock);
        return newStock;
    }

    @Transactional
    public CompanyStockDto getCompanyStock(Long companyId) {
        Company company = companyService.getCompanyEntityById(companyId);
        CompanyStock stock = getOrCreateTodayStock(company);
        return new CompanyStockDto(company, stock);
    }
}
