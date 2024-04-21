package com.atp.bdss.repositories;

import com.atp.bdss.entities.District;
import com.atp.bdss.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepositoryJPA extends JpaRepository<Province, Integer> {

    @Query(value = "select distinct new com.atp.bdss.entities.Province( " +
            "   p.id, " +
            "   p.name" +
            ")" +
            "from Province p " +
            "inner join District d on p.id  = d.province.id " +
            "WHERE SIZE(d.projects) > 0 "+
            "order by SIZE(d.projects) asc"
    )
    List<Province> getAllProvincesWithProject();
    // just get provinces had project

    @Query(value = "select distinct new com.atp.bdss.entities.Province( " +
            "   p.id, " +
            "   p.name" +
            ")" +
            "from Province p "
    )
    List<Province> getAllProvinces();

}
