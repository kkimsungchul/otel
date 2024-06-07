package com.kt.otelsdkspringboot01.repository;


import com.kt.otelsdkspringboot01.domain.LogApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Transactional
public class LogRepository {
    private final EntityManager em;

    public void save(LogApi logApi){
        em.persist(logApi);
    }

    public List<LogApi> all(){
        return em.createQuery("select l from LogApi l" , LogApi.class)
                .getResultList();
    }

}
