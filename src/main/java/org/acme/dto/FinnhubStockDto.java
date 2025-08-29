package org.acme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinnhubStockDto {
    @JsonProperty("marketCapitalization")
    private Long marketCapitalization;

    @JsonProperty("shareOutstanding")
    private Long shareOutstanding;

    public FinnhubStockDto() {}
    public FinnhubStockDto(Long marketCapitalization, Long shareOutstanding){
        this.marketCapitalization = marketCapitalization;
        this.shareOutstanding = shareOutstanding;
    }
    public Long getMarketCapitalization(){
        return marketCapitalization;
    }

    public Long getShareOutstanding(){
        return shareOutstanding;
    }
}
