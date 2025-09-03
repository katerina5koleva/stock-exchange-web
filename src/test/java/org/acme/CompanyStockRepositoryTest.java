package org.acme;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.acme.model.Company;
import org.acme.model.CompanyStock;
import org.acme.repository.CompanyRepository;
import org.acme.repository.CompanyStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class CompanyStockRepositoryTest {
    @Inject
    CompanyRepository companyRepository;
    @Inject
    CompanyStockRepository companyStockRepository;

    @Test
    public void findByCompanyIdAndCurrentDate(){
        Company company = new Company("Test1", "BG", "TEST", "", "");
        companyRepository.persist(company);
        companyRepository.flush();

        LocalDate date = LocalDate.now();
        CompanyStock companyStock = new CompanyStock(company, 1000L, 200L, date);
        companyStockRepository.persist(companyStock);
        companyStockRepository.flush();

        CompanyStock found = companyStockRepository.findByCompanyIdAndCurrentDate(company.getId(), date);
        assertNotNull(found);
        assertEquals(company.getId(), found.getCompany().getId());
        assertEquals(1000L, found.getMarketCapitalization());
        assertEquals(200L, found.getShareOutstanding());
        assertEquals(date, found.getStockCreatedAt());
    }

    @Test
    public void findByCompanyNoIdAndCurrentDate(){
        LocalDate date = LocalDate.now();
        CompanyStock companyStockNoId = companyStockRepository.findByCompanyIdAndCurrentDate(1L, date);
        assertNull(companyStockNoId);
    }

    @Test
    public void findByCompanyIdAndNotCurrentDate(){
        Company company = new Company("Test1", "BG", "TEST", "", "");
        companyRepository.persist(company);
        companyRepository.flush();

        CompanyStock companyStock = new CompanyStock(company, 1000L, 200L, LocalDate.of(2009, 11, 24));
        companyStockRepository.persist(companyStock);
        companyStockRepository.flush();

        LocalDate date = LocalDate.now();
        CompanyStock companyStockNotCurrentDate = companyStockRepository.findByCompanyIdAndCurrentDate(company.getId(), date);
        assertNull(companyStockNotCurrentDate);
    }

    @Test
    public void fuckedUpPersist(){
        Company company = new Company("Test1", "BG", "TEST", "", "");
        companyRepository.persist(company);
        companyRepository.flush();

        CompanyStock companyInvalidStock = new CompanyStock(company, null, 200L, LocalDate.of(2009, 11, 24));
        assertThrows(ConstraintViolationException.class, () -> {
            companyStockRepository.persist(companyInvalidStock);
            companyStockRepository.flush();
        });
    }
}
