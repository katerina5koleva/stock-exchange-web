package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.CompanyDto;
import org.acme.dto.CompanyStockDto;
import org.acme.mapper.CompanyMapper;
import org.acme.model.CompanyStock;
import org.acme.repository.CompanyRepository;
import org.acme.repository.CompanyStockRepository;
import org.acme.service.CompanyService;
import org.acme.service.CompanyStockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class CompanyStockResourceTest {
    @Inject
    public CompanyStockService companyStockService;
    @Inject
    public CompanyService companyService;

    @Inject
    CompanyRepository companyRepository;
    @Inject
    CompanyStockRepository companyStockRepository;

    @BeforeEach
    @Transactional
    public void cleanup() {
        companyStockRepository.deleteAll();
        companyRepository.deleteAll();
    }
    @Test
    public void getCompanyStock(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        CompanyDto company = companyService.addCompany(companyDto);
        CompanyStockDto companyStockDto = companyStockService.addStock(company.getId(), 1000L, 200L, LocalDate.now());
        given()
        .when()
                .get("/company-stocks/{companyId}", company.getId())
        .then()
                .statusCode(200)
                .body("marketCapitalization", equalTo(1000))
                .body("shareOutstanding", equalTo(200))
                .body("id", equalTo(company.getId().intValue()))
                .body("name", equalTo("Test"))
                .body("symbol", equalTo("TEST"));
    }

    @Test
    public void doNotGetCompanyStock(){
        given()
                .when()
                .get("/company-stocks/{companyId}", 1L)
        .then()
                .statusCode(404);
    }
}
