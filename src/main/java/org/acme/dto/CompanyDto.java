package org.acme.dto;

import java.time.LocalDateTime;

public class CompanyDto {
    private Long id;
    private String name;
    private String country;
    private String symbol;
    private LocalDateTime createdAt;
    private String website;
    private String email;

    public CompanyDto() {}
    public CompanyDto(Long id, String name, String country, String symbol, LocalDateTime createdAt, String website, String email) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.symbol = symbol;
        this.createdAt = createdAt;
        this.website = website;
        this.email = email;
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
}
