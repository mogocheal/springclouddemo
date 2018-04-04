package com.thunder.controller;


import com.thunder.model.StatusData;
import com.thunder.repository.primaryRepository.Upar_adtd_chn_tabRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
public class primaryCotroller {
    @Autowired
    private Upar_adtd_chn_tabRepository upar_adtd_chn_tabRepository;

    @GetMapping("/get2thunderByPreHour")
    public StatusData findAllByD_AndD_datetimeInOrderByD_datetimeAsc(@RequestParam(required = false, defaultValue = "1") Integer preHour) {
        StatusData statusData = new StatusData();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        String eTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(calendar.getTime());
        if ("".equals(preHour) || preHour.equals(1)) {
            calendar.add(Calendar.HOUR, -1);
            String sTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(calendar.getTime());
            statusData.setData(upar_adtd_chn_tabRepository.findAllByDateTimeBetweenOrderByDateTimeAsc(sTime, eTime));
        } else {
            calendar.add(Calendar.HOUR, -preHour);
            String sTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(calendar.getTime());
            statusData.setData(upar_adtd_chn_tabRepository.findAllByDateTimeBetweenOrderByDateTimeAsc(sTime, eTime));
        }
        statusData.setStatus("查询成功");
        return statusData;
    }

    @GetMapping("/get2thunder")
    public StatusData get2Thunder(String sTime, String eTime,
                                  String province, @RequestParam(required = false) String city,
                                  @RequestParam(required = false) String county,
                                  String minPower, String maxPower) {
        StatusData statusData = new StatusData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        statusData.setStatus("success");
        if ("all".equals(province)) {
            statusData.setData(upar_adtd_chn_tabRepository.findAllByDateTimeBetweenAndV73016BetweenOrderByDateTimeAsc(sTime, eTime, minPower, maxPower));
        } else {
            if ("all".equals(city)) {
                statusData.setData(upar_adtd_chn_tabRepository.findAllByDateTimeBetweenAndProvinceAndV73016BetweenOrderByDateTimeAsc(sTime, eTime, province, minPower, maxPower));
            } else {
                if ("all".equals(county)) {
                    statusData.setData(upar_adtd_chn_tabRepository.findAllByDateTimeBetweenAndProvinceAndCityAndV73016BetweenOrderByDateTimeAsc(sTime, eTime, province, city, minPower, maxPower));
                } else {
                    statusData.setData(upar_adtd_chn_tabRepository.findAllByDateTimeBetweenAndProvinceAndCityAndCountyAndV73016BetweenOrderByDateTimeAsc(sTime, eTime, province, city, county, minPower, maxPower));
                }
            }
        }
        return statusData;
    }

 /*  @GetMapping("/test")
    public Upar_adtd_chn_tab find(String id){
        return  upar_adtd_chn_tabRepository.findAllById(id);
   }*/
}
