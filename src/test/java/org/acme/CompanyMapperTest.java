package org.acme;


import jakarta.ws.rs.NotFoundException;
import org.acme.dto.CompanyDto;
import org.acme.mapper.CompanyMapper;
import org.acme.model.Company;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CompanyMapperTest {

    private final CompanyMapper companyMapper = new CompanyMapper();

    @Test
    public void toDTO(){
        Company company = Mockito.mock(Company.class);
        Mockito.when(company.getId()).thenReturn(1L);
        Mockito.when(company.getName()).thenReturn("Test");
        Mockito.when(company.getCountry()).thenReturn("BG");
        Mockito.when(company.getSymbol()).thenReturn("TEST");
        Mockito.when(company.getEmail()).thenReturn("test@test.com");
        Mockito.when(company.getWebsite()).thenReturn("https://test.com");
        LocalDateTime now = LocalDateTime.now();
        Mockito.when(company.getCreatedAt()).thenReturn(now);

        CompanyDto companyDto = companyMapper.toDTO(company);

        assertNotNull(companyDto);
        assertEquals(1L, companyDto.getId());
        assertEquals("Test", companyDto.getName());
        assertEquals("BG", companyDto.getCountry());
        assertEquals("TEST", companyDto.getSymbol());
        assertEquals("test@test.com", companyDto.getEmail());
        assertEquals("https://test.com", companyDto.getWebsite());
        assertEquals(now, companyDto.getCreatedAt());
    }

    @Test
    public void toDTONull(){
        assertThrows(NotFoundException.class,
                () -> companyMapper.toDTO(null)
        );
    }

    @Test
    public void toEntity(){
        CompanyDto companyDto = new CompanyDto(
                null,
                "Test",
                "BG",
                "TEST",
                LocalDateTime.now(),
                "test@test.com",
                "https://test.com"
        );

        Company company = companyMapper.toEntity(companyDto);

        assertNotNull(company);
        assertEquals("Test", company.getName());
        assertEquals("BG", company.getCountry());
        assertEquals("TEST", company.getSymbol());
        assertEquals("test@test.com", company.getEmail());
        assertEquals("https://test.com", company.getWebsite());
    }

    @Test
    public void toEntityNull(){
        assertThrows(NotFoundException.class,
                () -> companyMapper.toEntity(null)
        );
    }
}
