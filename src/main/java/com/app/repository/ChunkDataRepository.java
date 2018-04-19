package com.app.repository;

import com.app.entity.ChunkData;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ChunkDataRepository extends JpaRepository<ChunkData, Long> {
}