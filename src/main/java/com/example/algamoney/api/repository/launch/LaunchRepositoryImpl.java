package com.example.algamoney.api.repository.launch;


import com.example.algamoney.api.model.Launch;
import com.example.algamoney.api.model.Launch_;
import com.example.algamoney.api.repository.filter.LaunchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LaunchRepositoryImpl implements LaunchRepositoryQuery{

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Launch> filter(LaunchFilter launchFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Launch> criteria = builder.createQuery(Launch.class);
        Root<Launch> root = criteria.from(Launch.class);

        Predicate[] predicates = createRestrictions(launchFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Launch> query = manager.createQuery(criteria);
        addPaginationRestrictions(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(launchFilter));
    }

    private Predicate[] createRestrictions(LaunchFilter launchFilter, CriteriaBuilder builder,
                                        Root<Launch> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(launchFilter.getDescription())){
            predicates.add(builder.like(
                    builder.lower(root.get(Launch_.description)), "%" + launchFilter.getDescription().toLowerCase() + "%"));
        }

        if (Objects.nonNull(launchFilter.getDueDateTo())) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get(Launch_.dueDate), launchFilter.getDueDateTo()));
        }

        if (Objects.nonNull(launchFilter.getDueDateUntil())) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get(Launch_.dueDate), launchFilter.getDueDateUntil()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void addPaginationRestrictions(TypedQuery<Launch> query, Pageable pageable){
        int atualPage = pageable.getPageNumber();
        int totalResgistriesPerPage = pageable.getPageSize();
        int firstRegistryOfPage = atualPage * totalResgistriesPerPage;

        query.setFirstResult(firstRegistryOfPage);
        query.setMaxResults(totalResgistriesPerPage);
    }

    private Long total(LaunchFilter launchFilter){
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Launch> root = criteria.from(Launch.class);

        Predicate[] predicates = createRestrictions(launchFilter, builder, root);
        criteria.where(predicates);
        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }
}
