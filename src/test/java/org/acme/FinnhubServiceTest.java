package org.acme;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.dto.FinnhubStockDto;
import org.acme.service.FinnhubService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FinnhubServiceTest {

    static WireMockServer wireMockServer;

    @Inject
    @RestClient
    public FinnhubService finnhubService;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        configureFor("localhost", 8089);
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void getCompanyStock() {
        stubFor(get(urlPathEqualTo("/stock/profile2"))
                .withQueryParam("symbol", equalTo("MSFT"))
                .withHeader("X-Finnhub-Token", matching(".*"))
                .willReturn(okJson("{ \"marketCapitalization\": 1000, \"shareOutstanding\": 200 }")));

        FinnhubStockDto finnhubStockDto = finnhubService.getCompanyStock("MSFT", "mocking");

        assertNotNull(finnhubStockDto);
        assertEquals(1000L, finnhubStockDto.getMarketCapitalization());
        assertEquals(200L, finnhubStockDto.getShareOutstanding());

        verify(getRequestedFor(urlPathEqualTo("/stock/profile2"))
                .withQueryParam("symbol", equalTo("MSFT"))
                .withHeader("X-Finnhub-Token", matching(".*")));
    }
}
