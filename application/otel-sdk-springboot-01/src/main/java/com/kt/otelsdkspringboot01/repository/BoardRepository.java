package com.kt.otelsdkspringboot01.repository;

import com.kt.otelsdkspringboot01.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@RequiredArgsConstructor
@Transactional
public class BoardRepository {
    private final EntityManager em;


    public List<Board> all(){
        return em.createQuery("select b from Board b" , Board.class)
                .getResultList();
    }

    public List<Board> findAll(){
        return em.createQuery("select b from Board b" , Board.class)
                .getResultList();
    }
}
