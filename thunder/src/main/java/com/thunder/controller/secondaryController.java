package com.thunder.controller;

import com.thunder.model.StatusData;
import com.thunder.repository.secondaryRepository.Thunder2014Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
public class secondaryController {
    @Autowired
    private Thunder2014Repository thunder2014Repository;

    @GetMapping("/get3thunderByPreHour")
    public StatusData findAllByDatetimeBetweenOrderByDatetimeAsc(@RequestParam(required = false, defaultValue = "1") Integer preHour) {
        StatusData statusData = new StatusData();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        Date eTime = calendar.getTime();
        Date sTime = null;
        statusData.setStatus("success");
        if ("".equals(preHour) || preHour.equals(1)) {
            calendar.add(Calendar.HOUR, -1);
            sTime = calendar.getTime();
        } else {
            calendar.add(Calendar.HOUR, -preHour);
            sTime = calendar.getTime();
        }
        statusData.setData(thunder2014Repository.findAllByDatetimeBetweenOrderByDatetimeAsc(sTime, eTime));
        return statusData;
    }

    @GetMapping("/get3thunder")
    public StatusData get3Thunder(String sTime, String eTime,
                                  String province, @RequestParam(required = false) String city,
                                  @RequestParam(required = false) String county, String type,
                                  String minPower, String maxPower) {
        StatusData statusData = new StatusData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date s = sdf.parse(sTime);
            Date t = sdf.parse(eTime);
            statusData.setStatus("success");
            try {
                if ("all".equals(province)) {
                    if ("all".equals(type)) {
                        statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndIntensBetweenOrderByDatetime(s, t, minPower, maxPower));
                    } else {
                        statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndTypeAndIntensBetweenOrderByDatetime(s, t, type, minPower, maxPower));
                    }
                } else {
                    if ("all".equals(city)) {
                        if ("all".equals(type)) {
                            statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndProvinceAndIntensBetweenOrderByDatetime(s, t, province, minPower, maxPower));
                        } else {
                            statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndProvinceAndTypeAndIntensBetweenOrderByDatetime(s, t, province, type, minPower, maxPower));
                        }
                    } else {
                        if ("all".equals(county)) {
                            if ("all".equals(type)) {
                                statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndProvinceAndDistrictAndIntensBetweenOrderByDatetime(s, t, province, city, minPower, maxPower));
                            } else {
                                statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndProvinceAndDistrictAndTypeAndIntensBetweenOrderByDatetime(s, t, province, city, type, minPower, maxPower));
                            }
                        } else {
                            if ("all".equals(type)) {
                                statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndProvinceAndDistrictAndCountryAndIntensBetweenOrderByDatetime(s, t, province, city, county, minPower, maxPower));

                            } else {
                                statusData.setData(thunder2014Repository.findAllByDatetimeBetweenAndProvinceAndDistrictAndCountryAndTypeAndIntensBetweenOrderByDatetime(s, t, province, city, county, type, minPower, maxPower));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                statusData.setStatus("查询失败");
            }
            return statusData;
        } catch (ParseException e) {
            statusData.setStatus("时间格式错误");
        }
        return statusData;
    }
}
