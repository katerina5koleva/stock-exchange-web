package org.acme;

import jakarta.inject.Inject;
import org.acme.dto.CompanyStockDto;
import org.acme.dto.FinnhubStockDto;
import org.acme.model.Company;
import org.acme.model.CompanyStock;
import org.acme.repository.CompanyStockRepository;
import org.acme.service.CompanyService;
import org.acme.service.CompanyStockService;
import org.acme.service.FinnhubService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CompanyStockServiceTest {
    @Mock
    CompanyStockRepository companyStockRepository;
    @Mock
    CompanyService companyService;
    @Mock
    FinnhubService finnhubService;
    @InjectMocks
    CompanyStockService companyStockService;

    @Test
    public void getCompanyStockExists() {
        Company company = Mockito.mock(Company.class);
        CompanyStock companyStock = new CompanyStock(company, 1000L, 200L, LocalDate.now());

        Mockito.when(company.getId()).thenReturn(1L);
        Mockito.when(company.getName()).thenReturn("Test");
        Mockito.when(companyService.getCompanyEntityById(1L)).thenReturn(company);
        Mockito.when(companyStockRepository.findByCompanyIdAndCurrentDate(1L, LocalDate.now())).thenReturn(companyStock);

        CompanyStockDto result = companyStockService.getCompanyStock(1L);

        assertNotNull(result);
        assertEquals(company.getName(), result.getName());
        assertEquals(companyStock.getMarketCapitalization(), result.getMarketCapitalization());

        verify(companyService).getCompanyEntityById(1L);
        verify(companyStockRepository).findByCompanyIdAndCurrentDate(1L, LocalDate.now());
        verify(finnhubService, never()).getCompanyStock(any(), any());
        verify(companyStockRepository, never()).persist((CompanyStock) any());
    }

    @Test
    public void getCompanyStockCreate() {
        Company company = Mockito.mock(Company.class);
        Mockito.when(company.getId()).thenReturn(1L);
        Mockito.when(company.getName()).thenReturn("Test");
        Mockito.when(company.getSymbol()).thenReturn("TEST");

        Mockito.when(companyService.getCompanyEntityById(1L)).thenReturn(company);
        Mockito.when(companyStockRepository.findByCompanyIdAndCurrentDate(1L, LocalDate.now())).thenReturn(null);

        FinnhubStockDto finnhubStockDto = new FinnhubStockDto(
                1000L,
                200L
        );
        Mockito.when(finnhubService.getCompanyStock(Mockito.anyString(), Mockito.any())).thenReturn(finnhubStockDto);

        CompanyStockDto result = companyStockService.getCompanyStock(1L);

        assertNotNull(result);
        assertEquals(company.getName(), result.getName());
        assertEquals(finnhubStockDto.getMarketCapitalization(), result.getMarketCapitalization());

        verify(companyService, times(2)).getCompanyEntityById(1L);
        verify(companyStockRepository).findByCompanyIdAndCurrentDate(1L, LocalDate.now());
        verify(finnhubService).getCompanyStock(anyString(), nullable(String.class));
        verify(companyStockRepository).persist(any(CompanyStock.class));
    }

    @Test
    public void addStockTest() {
        Company company = Mockito.mock(Company.class);
        Mockito.when(company.getId()).thenReturn(1L);
        Mockito.when(company.getName()).thenReturn("Test");

        Mockito.when(companyService.getCompanyEntityById(1L)).thenReturn(company);

        CompanyStockDto result = companyStockService.addStock(1L, 1000L, 200L, LocalDate.now());

        assertNotNull(result);
        assertEquals(1000L, result.getMarketCapitalization());
        assertEquals(200L, result.getShareOutstanding());
        assertEquals(company.getName(), result.getName());

        verify(companyService).getCompanyEntityById(1L);
        verify(companyStockRepository).persist(any(CompanyStock.class));
    }
}
