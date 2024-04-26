package com.atp.bdss.repositories;

import com.atp.bdss.entities.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepositoryJPA extends JpaRepository<District, Integer> {

    @Query(value = "select new com.atp.bdss.entities.District( " +
            "   d.id, " +
            "   d.name, " +
            "   d.province " +
            ")" +
            "from District d " +
            "WHERE SIZE(d.projects) > 0 "+
            "order by SIZE(d.projects) asc"
    )
    List<District> getAllDistrict();
    // lay cac quan/ huyen co du an


    @Query(value = "select distinct new com.atp.bdss.entities.District( " +
            "   d.id, " +
            "   d.name, " +
            "   d.province " +
            ")" +
            "from District d " +
            "inner join d.province p " +
            "WHERE p.id = :provinceId and SIZE(d.projects) > 0 "+
            "order by SIZE(d.projects) asc"
    )
    List<District> getAllDistrictWithProjectByProvince(@Param("provinceId") int provinceId);
    // lay cac quan/ huyen thuoc tinh/thanh pho theo provinceId ma co du an


    @Query(value = "select new com.atp.bdss.entities.District( " +
            "   d.id, " +
            "   d.name, " +
            "   d.province " +
            ")" +
            "from District d " +
            "inner join d.province p " +
            "WHERE p.id = :provinceId "
    )
    List<District> getAllDistrictByProvince(@Param("provinceId") int provinceId);
}
