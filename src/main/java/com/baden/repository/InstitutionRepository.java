package com.baden.repository;

import com.baden.entity.InstitutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<InstitutionEntity, Integer> {

    // custom methods to fetch and manipulate data in the database

}
