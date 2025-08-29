package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.CompanyStock;

import java.time.LocalDate;

@ApplicationScoped
public class CompanyStockRepository implements PanacheRepository<CompanyStock> {
    public CompanyStock findByCompanyIdAndCurrentDate(Long companyId, LocalDate date){
        return find("company.id = ?1 and stockCreatedAt = ?2", companyId, date).firstResult();
    }
}
