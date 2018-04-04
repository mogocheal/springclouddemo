package com.thunder.repository.primaryRepository;

import com.thunder.model.primaryModel.Upar_adtd_chn_tab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Upar_adtd_chn_tabRepository extends CrudRepository<Upar_adtd_chn_tab, String> {
    List<Upar_adtd_chn_tab> findAllByDateTimeBetweenOrderByDateTimeAsc(String sTime, String eTime);

    List<Upar_adtd_chn_tab> findAllByDateTimeBetweenAndV73016BetweenOrderByDateTimeAsc(String sTime, String eTime, String minPower, String maxPower);
    List<Upar_adtd_chn_tab> findAllByDateTimeBetweenAndProvinceAndV73016BetweenOrderByDateTimeAsc(String sTime, String eTime, String province,  String minPower, String maxPower);
    List<Upar_adtd_chn_tab> findAllByDateTimeBetweenAndProvinceAndCityAndV73016BetweenOrderByDateTimeAsc(String sTime, String eTime, String province, String city, String minPower, String maxPower);
    List<Upar_adtd_chn_tab> findAllByDateTimeBetweenAndProvinceAndCityAndCountyAndV73016BetweenOrderByDateTimeAsc(String sTime, String eTime, String province, String city, String county, String minPower, String maxPower);

//    Upar_adtd_chn_tab findAllById(String id);
}
