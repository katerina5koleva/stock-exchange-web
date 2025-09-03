package org.acme;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.acme.model.Company;
import org.acme.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestTransaction
public class CompanyRepositoryTest {
    @Inject
    public CompanyRepository companyRepository;

    @BeforeEach
    @Transactional
    void clean() {
        companyRepository.deleteAll();
    }

    @Test
    public void listAll(){
        Company company1 = new Company("Test1", "BG", "TEST", "", "");
        Company company2 = new Company("Test2", "BG", "TEST2", "", "");

        companyRepository.persist(company1);
        companyRepository.persist(company2);
        companyRepository.flush();

        List<Company> companies = companyRepository.listAll();

        assertNotNull(companies);
        assertTrue(companies.size() >= 2);
    }

    @Test
    public void listAllEmpty(){
        List<Company> companiesEmpty = companyRepository.listAll();
        assertNotNull(companiesEmpty);
        assertTrue(companiesEmpty.isEmpty());
    }

    @Test
    public void findById(){
        Company company = new Company("Test1", "BG", "TEST", "", "");
        companyRepository.persist(company);
        companyRepository.flush();

        Company foudn = companyRepository.findById(company.getId());
        assertNotNull(foudn);
        assertEquals("Test1", foudn.getName());
    }

    @Test
    public void findByIdNotExisting(){
        Company foudn = companyRepository.findById(1L);
        assertNull(foudn);
    }

    @Test
    public void findBySymbol(){
        Company company = new Company("Test1", "BG", "TEST", "", "");
        companyRepository.persist(company);
        companyRepository.flush();

        Company foudn = companyRepository.findBySymbol(company.getSymbol());
        assertNotNull(foudn);
        assertEquals("TEST", foudn.getSymbol());
        assertEquals("Test1", foudn.getName());
    }

    @Test
    public void findBySymbolNotExisiting(){
        Company foudn = companyRepository.findBySymbol("SUN");
        assertNull(foudn);
    }

    @Test
    public void persistEmptyFieldMandatory() {
        Company company = new Company("Test", null, "TEST", "", "");

        assertThrows(ConstraintViolationException.class, () -> {
            companyRepository.persist(company);
            companyRepository.flush();
        });
    }
}
