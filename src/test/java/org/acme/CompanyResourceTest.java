package org.acme;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.dto.CompanyDto;
import org.acme.repository.CompanyRepository;
import org.acme.repository.CompanyStockRepository;
import org.acme.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class CompanyResourceTest {
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
    public void getAllCompanies(){
        CompanyDto companyDto1 = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        CompanyDto companyDto2 = new CompanyDto(null, "Test2", "BG", "TEST2", null, null, null);
        companyService.addCompany(companyDto1);
        companyService.addCompany(companyDto2);

        given()
        .when()
                .get("/companies")
        .then()
                .statusCode(200)
                .body("$.size()", is(2))
                .body("[0].name", equalTo("Test"))
                .body("[1].symbol", equalTo("TEST2"));
    }

    @Test
    public void getAllNoCompanies(){
        List<CompanyDto> companies = companyService.getAllCompanies();
        assertTrue(companies.isEmpty());

        given()
        .when()
                .get("/companies")
        .then()
                .statusCode(200)
                .body("$.size()", is(0));
    }

    @Test
    public void create(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        given()
                .contentType(ContentType.JSON)
                .body(companyDto)
        .when()
                .post("/companies")
        .then()
                .statusCode(201)
                .body("name", equalTo("Test"))
                .body("symbol", equalTo("TEST"))
                .body("id", notNullValue());
    }

    @Test
    public void doNotCreateNull(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", null, null, null, null);
        given()
                .contentType(ContentType.JSON)
                .body(companyDto)
        .when()
                .post("/companies")
        .then()
                .statusCode(400);
    }

    @Test
    public void doNotCreateExists(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        given()
                .contentType(ContentType.JSON)
                .body(companyDto)
        .when()
                .post("/companies")
        .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(companyDto)
        .when()
                .post("/companies")
        .then()
                .statusCode(409);
    }

    @Test
    public void update(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        CompanyDto company = companyService.addCompany(companyDto);
        CompanyDto updatedCompanyDto = new CompanyDto(null, "TestUpdated", null, null, null, null, null);

        given()
                .contentType(ContentType.JSON)
                .body(updatedCompanyDto)
        .when()
                .put("/companies/{id}", company.getId())
        .then()
                .statusCode(200)
                .body("name", equalTo("TestUpdated"))
                .body("symbol", equalTo("TEST"));

    }

    @Test
    public void doNotUpdateIdNotExisting(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        given()
                .contentType(ContentType.JSON)
                .body(companyDto)
        .when()
                .put("/companies/{id}", 1L)
        .then()
                .statusCode(404);
    }

    @Test
    public void doNotUpdateFuckedInput(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        CompanyDto company = companyService.addCompany(companyDto);
        CompanyDto updatedCompanyDto = new CompanyDto(null, "TestUpdated", "", null, null, null, null);

        given()
                .contentType(ContentType.JSON)
                .body(updatedCompanyDto)
        .when()
                .put("/companies/{id}", company.getId())
        .then()
                .statusCode(400);
    }
    @Test
    public void doNotUpdateSymbolExists(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, null, null);
        CompanyDto company = companyService.addCompany(companyDto);
        CompanyDto companyDtoSymbolExists = new CompanyDto(null, "Test", "BG", "SUN", null, null, null);
        CompanyDto companySymbolExists = companyService.addCompany(companyDtoSymbolExists);
        CompanyDto updatedCompanyDto = new CompanyDto(null, "TestUpdated", "", "SUN", null, null, null);

        given()
                .contentType(ContentType.JSON)
                .body(updatedCompanyDto)
                .when()
                .put("/companies/{id}", company.getId())
                .then()
                .statusCode(409);
    }
}
