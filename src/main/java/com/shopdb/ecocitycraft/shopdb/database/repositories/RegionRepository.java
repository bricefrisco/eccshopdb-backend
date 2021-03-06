package com.shopdb.ecocitycraft.shopdb.database.repositories;

import com.shopdb.ecocitycraft.shopdb.database.entities.Player;
import com.shopdb.ecocitycraft.shopdb.database.entities.Region;
import com.shopdb.ecocitycraft.shopdb.database.entities.enums.Server;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>, JpaSpecificationExecutor<Region> {
    Region findOneByServerAndNameIgnoreCase(Server server, String name);

    Page<Region> findByMayors(Player mayor, Pageable pageable);

    @Query(value = "SELECT DISTINCT name FROM region ORDER BY name", nativeQuery = true)
    List<String> findAllRegionNames();
    @Query(value = "SELECT DISTINCT name FROM region WHERE server = :server ORDER BY name", nativeQuery = true)
    List<String> findAllRegionsNamesByServer(@Param("server") String server);

    @Query(value = "SELECT DISTINCT name FROM region WHERE active = true ORDER BY name", nativeQuery = true)
    List<String> findActiveRegionNames();
    @Query(value = "SELECT DISTINCT name FROM region WHERE active = true AND server = :server ORDER BY name", nativeQuery = true)
    List<String> findActiveRegionNamesByServer(@Param("server") String server);

    @Query(value = "SELECT * FROM region WHERE " +
            "server = :server AND " +
            "i_x <= :x AND o_x >= :x AND " +
            "i_y <= :y AND o_y >= :y AND " +
            "i_z <= :z AND o_z >= :z", nativeQuery = true)
    List<Region> findByCoordinates(@Param("x") int x, @Param("y") int y, @Param("z") int z, @Param("server") String server);

    @Query(value = " SELECT * FROM region WHERE " +
            "server = :server AND " +
            "i_x >= :ix AND o_x <= :ox AND " +
            "i_z >= :iz AND o_z <= :oz", nativeQuery = true)
    List<Region> findRegionsInCoordinates(@Param("ix") int ix, @Param("ox") int ox, @Param("iz") int iz, @Param("oz") int oz, @Param("server") String server);
}
