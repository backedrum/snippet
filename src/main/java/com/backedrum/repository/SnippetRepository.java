package com.backedrum.repository;

import com.backedrum.model.SourceCodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnippetRepository extends JpaRepository<SourceCodeSnippet, Long> {

    @Query("SELECT s.tag FROM SourceCodeSnippet s WHERE s.tag IN (select distinct ss.tag from SourceCodeSnippet ss)")
    List<String> findAllTags();
}
