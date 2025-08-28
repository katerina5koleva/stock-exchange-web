package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Company;

@ApplicationScoped
public class CompanyRepository implements PanacheRepository<Company> {
    public Company findBySymbol(String symbol){
        return find("symbol", symbol).firstResult();
    }
}
