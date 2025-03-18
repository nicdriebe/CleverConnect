package com.example.application.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SamplePersonExternalRepository
        extends
        JpaRepository<SamplePersonExternal, Long>,
        JpaSpecificationExecutor<SamplePersonExternal> {

    @Query("select e from SamplePersonExternal e " +
            "where lower(e.firstName) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(e.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<SamplePersonExternal> search(@Param("searchTerm") String searchTerm);
}