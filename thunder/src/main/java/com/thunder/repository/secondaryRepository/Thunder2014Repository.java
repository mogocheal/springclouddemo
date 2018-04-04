package com.thunder.repository.secondaryRepository;

import com.thunder.model.secondaryModel.Thunder2014;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface Thunder2014Repository extends CrudRepository<Thunder2014,String> {
    List<Thunder2014> findAllByDatetimeBetweenOrderByDatetimeAsc(Date sTime, Date eTime);

    List<Thunder2014> findAllByDatetimeBetweenAndTypeAndIntensBetweenOrderByDatetime(Date sTime, Date eTime,  String type, String minPower, String maxPower);
    List<Thunder2014> findAllByDatetimeBetweenAndProvinceAndTypeAndIntensBetweenOrderByDatetime(Date sTime, Date eTime, String province,String type, String minPower, String maxPower);
    List<Thunder2014> findAllByDatetimeBetweenAndProvinceAndDistrictAndTypeAndIntensBetweenOrderByDatetime(Date sTime, Date eTime, String province, String city, String type, String minPower, String maxPower);
    List<Thunder2014> findAllByDatetimeBetweenAndProvinceAndDistrictAndCountryAndTypeAndIntensBetweenOrderByDatetime(Date sTime, Date eTime, String province, String city, String county, String type, String minPower, String maxPower);

    List<Thunder2014> findAllByDatetimeBetweenAndIntensBetweenOrderByDatetime(Date sTime, Date eTime,   String minPower, String maxPower);
    List<Thunder2014> findAllByDatetimeBetweenAndProvinceAndIntensBetweenOrderByDatetime(Date sTime, Date eTime, String province, String minPower, String maxPower);
    List<Thunder2014> findAllByDatetimeBetweenAndProvinceAndDistrictAndIntensBetweenOrderByDatetime(Date sTime, Date eTime, String province, String city, String minPower, String maxPower);
    List<Thunder2014> findAllByDatetimeBetweenAndProvinceAndDistrictAndCountryAndIntensBetweenOrderByDatetime(Date sTime, Date eTime, String province, String city, String county,  String minPower, String maxPower);




}
