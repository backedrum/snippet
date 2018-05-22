package com.backedrum.repository;

import com.backedrum.model.Screenshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot, Long> {

    @Query("SELECT s.tag FROM Screenshot s WHERE s.tag IN (select distinct ss.tag from Screenshot ss)")
    List<String> findAllTags();
}
