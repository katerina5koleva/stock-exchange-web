package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CompanyDto;
import org.acme.mapper.CompanyMapper;
import org.acme.model.Company;
import org.acme.repository.CompanyRepository;
import org.acme.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    @Mock
    CompanyRepository companyRepository;
    @Mock
    CompanyMapper companyMapper;
    @InjectMocks
    CompanyService companyService;

    @BeforeEach
    @Transactional
    void clean() {
        companyRepository.deleteAll();
    }

    @Test
    public void getAllCompanies() {
        Company company = new Company("Test", "BG", "TEST", "", "");

        Mockito.when(companyRepository.listAll()).thenReturn(List.of(company));
        Mockito.when(companyMapper.toDTO(company)).thenReturn(new CompanyDto(null, "Test", "BG", "TEST", null, "", ""));

        List<CompanyDto> allCompanies = companyService.getAllCompanies();
        assertEquals(1, allCompanies.size());
        assertEquals("Test", allCompanies.get(0).getName());
        verify(companyRepository).listAll();
        verify(companyMapper).toDTO(company);
    }

    @Test
    public void getAllCompaniesEmpty(){
        Mockito.when((companyRepository.listAll())).thenReturn(Collections.emptyList());

        List<CompanyDto> allNoCompanies = companyService.getAllCompanies();

        assertNotNull(allNoCompanies);
        assertTrue(allNoCompanies.isEmpty());
        verify(companyRepository).listAll();
    }

    @Test
    public void addCompany(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, "", "");
        Company company = new Company("Test", "BG", "TEST", "", "");
        CompanyDto returnedCompanyDto = new CompanyDto(1L, "Test", "BG", "TEST", null, "", "");

        Mockito.when(companyRepository.findBySymbol("TEST")).thenReturn(null);
        Mockito.when(companyMapper.toEntity(companyDto)).thenReturn(company);
        Mockito.when(companyMapper.toDTO(company)).thenReturn(returnedCompanyDto);

        CompanyDto result = companyService.addCompany(companyDto);

        assertNotNull(result);
        assertEquals("Test", result.getName());

        verify(companyRepository).findBySymbol("TEST");
        verify(companyRepository).persist(company);
        verify(companyRepository).flush();
        verify(companyMapper).toEntity(companyDto);
        verify(companyMapper).toDTO(company);
    }

    @Test
    public void addCompanySymbolExists(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, "", "");
        Company companyExists = new Company("Test", "BG", "TEST", "", "");

        Mockito.when(companyRepository.findBySymbol("TEST")).thenReturn(companyExists);

        WebApplicationException exceptionConflict = assertThrows(WebApplicationException.class,
                                                                    () -> companyService.addCompany(companyDto)
                                                                );

        assertEquals(409, exceptionConflict.getResponse().getStatus());
        verify(companyRepository).findBySymbol("TEST");
        verify(companyRepository, never()).persist((Company) any());
        verify(companyRepository, never()).flush();
    }

    @Test
    public void getCompanyById(){
        Company company = new Company("Test", "BG", "TEST", "", "");
        Mockito.when(companyRepository.findById(1L)).thenReturn(company);
        Mockito.when(companyMapper.toDTO(company)).thenReturn(new CompanyDto(null, "Test", "BG", "TEST", null, "", ""));

        CompanyDto result = companyService.getCompanyById(1L);

        assertNotNull(result);
        assertEquals("Test", result.getName());

        verify(companyRepository).findById(1L);
        verify(companyMapper).toDTO(company);
    }

    @Test
    public void getCompanyByNotExisitingId(){
        Mockito.when(companyRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> companyService.getCompanyById(1L)
        );

        verify(companyRepository).findById(1L);
        verify(companyMapper, never()).toDTO(any());
    }

    @Test
    public void getCompanyEnitityById(){
        Company company = new Company("Test", "BG", "TEST", "", "");
        Mockito.when(companyRepository.findById(1L)).thenReturn(company);

        Company result = companyService.getCompanyEntityById(1L);

        assertNotNull(result);
        assertEquals("Test", result.getName());

        verify(companyRepository).findById(1L);
    }

    @Test
    public void getCompanyEnitityByNotExisitingId(){
        Mockito.when(companyRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> companyService.getCompanyEntityById(1L)
        );

        verify(companyRepository).findById(1L);
    }

    @Test
    public void updateCompany(){
        Company company = Mockito.mock(Company.class);
        CompanyDto companyDto = new CompanyDto(null,"UpdatedTest", "BG", "TEST", null, "", "https://sth.com");
        //new Company("Test", "BG", "TEST", "", "");
        Mockito.when(company.getId()).thenReturn(1L);

        Mockito.when(companyRepository.findById(1L)).thenReturn(company);
        Mockito.when(companyRepository.findBySymbol("TEST")).thenReturn(company);
        Mockito.when(companyMapper.toDTO(company))
                .thenReturn(new CompanyDto(1L, "UpdatedTest", "BG", "TEST", null, "", "https://sth.com"));

        CompanyDto result = companyService.updateCompany(1L, companyDto);

        assertEquals("UpdatedTest", result.getName());
        assertEquals("https://sth.com", result.getWebsite());

        verify(companyRepository).findById(1L);
        verify(companyRepository).findBySymbol("TEST");
        verify(companyRepository, never()).persist((Company) any());
        verify(companyRepository).flush();
        verify(companyMapper).toDTO(company);
    }
    @Test
    public void updateCompanyOneField(){
        Company company = Mockito.mock(Company.class);
        CompanyDto companyDto = new CompanyDto(null, "UpdatedTest", null, null, null, null, null);
        //new Company("Test", "BG", "TEST", "", "");

        Mockito.when(companyRepository.findById(1L)).thenReturn(company);
        Mockito.when(companyRepository.findBySymbol(null)).thenReturn(null);
        Mockito.when(companyMapper.toDTO(company))
                .thenReturn(new CompanyDto(1L, "UpdatedTest", "BG", "TEST", null, "", ""));

        CompanyDto result = companyService.updateCompany(1L, companyDto);

        assertEquals("UpdatedTest", result.getName());

        verify(companyRepository).findById(1L);
        verify(companyRepository).findBySymbol(null);
        verify(companyRepository, never()).persist((Company) any());
        verify(companyRepository).flush();
        verify(companyMapper).toDTO(company);
    }
    @Test
    public void updateCompanyNoChanges(){
        Company company = Mockito.mock(Company.class);
        CompanyDto companyDto = new CompanyDto();
        //new Company("Test", "BG", "TEST", "", "");

        Mockito.when(companyRepository.findById(1L)).thenReturn(company);
        Mockito.when(companyRepository.findBySymbol(null)).thenReturn(null);
        Mockito.when(companyMapper.toDTO(company))
                .thenReturn(new CompanyDto(1L, "Test", "BG", "TEST", null, "", ""));

        CompanyDto result = companyService.updateCompany(1L, companyDto);

        assertEquals("Test", result.getName());
        assertEquals("BG", result.getCountry());

        verify(companyRepository).findById(1L);
        verify(companyRepository).findBySymbol(null);
        verify(companyRepository, never()).persist((Company) any());
        verify(companyRepository).flush();
        verify(companyMapper).toDTO(company);
    }

    @Test
    public void updateCompanyNotExisting(){
        CompanyDto companyDto = new CompanyDto(null, "Test", "BG", "TEST", null, "", "");
        Mockito.when(companyRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class,
                () -> companyService.updateCompany(1L, companyDto)
        );

        verify(companyRepository).findById(1L);
        verify(companyRepository, never()).findBySymbol(any());
        verify(companyRepository, never()).persist((Company) any());
        verify(companyRepository, never()).flush();
        verify(companyMapper, never()).toDTO(any());
    }

    @Test
    public void updateCompanySymbolExists(){
        Company company = Mockito.mock(Company.class);
        Company companyExists = Mockito.mock(Company.class);
        CompanyDto companyDto = new CompanyDto(null, "ErrorTest", "BG", "TEST2", null, "", "");

        Mockito.when(company.getId()).thenReturn(1L);
        Mockito.when(companyExists.getId()).thenReturn(2L);

        Mockito.when(companyRepository.findById(1L)).thenReturn(company);
        Mockito.when(companyRepository.findBySymbol("TEST2")).thenReturn(companyExists);

        WebApplicationException exceptionConflict = assertThrows(WebApplicationException.class,
                () -> companyService.updateCompany(1L, companyDto)
        );

        assertEquals(409, exceptionConflict.getResponse().getStatus());
        verify(companyRepository).findById(1L);
        verify(companyRepository).findBySymbol("TEST2");
        verify(companyRepository, never()).persist((Company) any());
        verify(companyRepository, never()).flush();
        verify(companyMapper, never()).toDTO(any());
    }
}
