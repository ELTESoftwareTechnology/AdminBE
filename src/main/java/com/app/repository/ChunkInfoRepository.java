package com.app.repository;

import com.app.entity.ChunkData;
import com.app.entity.ChunkInfo;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ChunkInfoRepository extends JpaRepository<ChunkInfo, Long> {
}