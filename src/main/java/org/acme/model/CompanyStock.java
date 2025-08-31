package org.acme.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name="company-stocks")
public class CompanyStock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name="companyId", nullable = false)
    private Company company;
    @NotNull
    private Long marketCapitalization;
    @NotNull
    private Long shareOutstanding;
    @NotNull
    private LocalDate stockCreatedAt;

    public CompanyStock() {}
    public CompanyStock(Company company, Long marketCapitalization, Long shareOutstanding, LocalDate stockCreatedAt){
        this.company = company;
        this.marketCapitalization = marketCapitalization;
        this.shareOutstanding = shareOutstanding;
        this.stockCreatedAt = stockCreatedAt;
    }

    public Long getId(){
        return id;
    }
    public Company getCompany(){
        return company;
    }
    public void setCompany(Company company){
        this.company = company;
    }
    public Long getMarketCapitalization(){
        return marketCapitalization;
    }
    public void setMarketCapitalization(Long marketCapitalization){
        this.marketCapitalization = marketCapitalization;
    }
    public Long getShareOutstanding(){
        return shareOutstanding;
    }
    public void setShareOutstanding(Long shareOutstanding){
        this.shareOutstanding = shareOutstanding;
    }
    public LocalDate getStockCreatedAt() {
        return stockCreatedAt;
    }
    public void setStockCreatedAt(LocalDate now) {}
}
