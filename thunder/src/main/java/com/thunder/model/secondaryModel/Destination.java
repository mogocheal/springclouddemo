package com.thunder.model.secondaryModel;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class Destination implements Serializable {
    private Date datetime;
    private String minisecond;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getMinisecond() {
        return minisecond;
    }

    public void setMinisecond(String minisecond) {
        this.minisecond = minisecond;
    }
}
