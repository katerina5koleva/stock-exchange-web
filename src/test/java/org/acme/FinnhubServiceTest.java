package org.acme;

//import com.github.tomakehurst.wiremock.WireMockServer;
//import io.quarkus.test.InjectMock;
//import io.quarkus.test.common.QuarkusTestResource;
//import io.quarkus.test.junit.QuarkusTest;
//import jakarta.inject.Inject;
//import jakarta.transaction.Transactional;
//import org.acme.dto.FinnhubStockDto;
//import org.acme.repository.CompanyRepository;
//import org.acme.repository.CompanyStockRepository;
//import org.acme.service.FinnhubService;
//import org.eclipse.microprofile.rest.client.inject.RestClient;
//import org.junit.jupiter.api.*;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@QuarkusTest
//class FinnhubServiceTest {
//
//    static WireMockServer wireMockServer;
//
//    @Inject
//    @RestClient
//    public FinnhubService finnhubService;
//
//    @Inject
//    CompanyRepository companyRepository;
//    @Inject
//    CompanyStockRepository companyStockRepository;
//    @BeforeAll
//    static void configureWireMock() {
//        wireMockServer = new WireMockServer(8089);
//        wireMockServer.start();
//        configureFor("localhost", 8089);
//
//        System.setProperty("quarkus.rest-client.finnhub-service.url", "http://localhost:8089");
//    }
//
//    @BeforeEach
//    @Transactional
//    public void cleanup() {
//        companyStockRepository.deleteAll();
//        companyRepository.deleteAll();
//    }
//
//    @AfterAll
//    static void stopWireMock() {
//        if (wireMockServer != null) {
//            wireMockServer.stop();
//        }
//        System.clearProperty("quarkus.rest-client.finnhub-service.url");
//    }
//
//    @Test
//    void getCompanyStock() {
//        stubFor(get(urlPathEqualTo("/stock/profile2"))
//                .withQueryParam("symbol", equalTo("KMS"))
//                .willReturn(okJson("{ \"marketCapitalization\": 1000, \"shareOutstanding\": 200 }")));
//
//        FinnhubStockDto finnhubStockDto = finnhubService.getCompanyStock("KMS", "mocking");
//
//        assertNotNull(finnhubStockDto);
//        assertEquals(1000L, finnhubStockDto.getMarketCapitalization());
//        assertEquals(200L, finnhubStockDto.getShareOutstanding());
//
//        verify(getRequestedFor(urlPathEqualTo("/stock/profile2"))
//                .withQueryParam("symbol", equalTo("KMS"))
//                .withHeader("X-Finnhub-Token", matching(".*")));
//    }
//}
