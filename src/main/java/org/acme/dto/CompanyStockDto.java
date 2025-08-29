package org.acme.dto;

import org.acme.model.Company;
import org.acme.model.CompanyStock;

import java.time.LocalDateTime;

public class CompanyStockDto {
    private Long id;
    private String name;
    private String country;
    private String symbol;
    private LocalDateTime createdAt;
    private String website;
    private String email;

    private Long marketCapitalization;
    private Long shareOutstanding;

    public CompanyStockDto(Company company, CompanyStock stock) {
        this.id = company.getId();
        this.name = company.getName();
        this.country = company.getCountry();
        this.symbol = company.getSymbol();
        this.createdAt = company.getCreatedAt();
        this.website = company.getWebsite();
        this.email = company.getEmail();

        this.marketCapitalization = stock.getMarketCapitalization();
        this.shareOutstanding = stock.getShareOutstanding();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getWebsite() {
        return website;
    }

    public String getEmail() {
        return email;
    }

    public Long getMarketCapitalization(){
        return marketCapitalization;
    }

    public Long getShareOutstanding(){
        return shareOutstanding;
    }
}
