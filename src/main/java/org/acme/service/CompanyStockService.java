package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
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
    private final CompanyStockRepository companyStockRepository;
    private final CompanyService companyService;
    private final FinnhubService finnhubService;

    @Inject
    public CompanyStockService(
            CompanyStockRepository companyStockRepository,
            CompanyService companyService,
            @RestClient FinnhubService finnhubService
    ) {
        this.companyStockRepository = companyStockRepository;
        this.companyService = companyService;
        this.finnhubService = finnhubService;
    }

    @ConfigProperty(name = "finnhub.api.key")
    String apiKey;

    private CompanyStockDto getOrCreateTodayStock(Company company) {
        LocalDate today = LocalDate.now();
        CompanyStock stock = companyStockRepository.findByCompanyIdAndCurrentDate(company.getId(), today);
        if (stock != null){
            return new CompanyStockDto(company, stock);
        }
        FinnhubStockDto finnhubStock = finnhubService.getCompanyStock(company.getSymbol(), apiKey);
        CompanyStockDto newStockDto = addStock(
                company.getId(),
                finnhubStock.getMarketCapitalization(),
                finnhubStock.getShareOutstanding(),
                today
        );
        return newStockDto;
    }

    @Transactional
    public CompanyStockDto addStock(Long companyId, Long marketCapitalization, Long shareOutstanding, LocalDate date) {
        Company company = companyService.getCompanyEntityById(companyId);
        CompanyStock stock = new CompanyStock(company, marketCapitalization, shareOutstanding, date);
        companyStockRepository.persist(stock);
        companyStockRepository.flush();
        return new CompanyStockDto(company, stock);
    }

    @Transactional
    public CompanyStockDto getCompanyStock(Long companyId) {
        Company company = companyService.getCompanyEntityById(companyId);
        return getOrCreateTodayStock(company);
    }
}
