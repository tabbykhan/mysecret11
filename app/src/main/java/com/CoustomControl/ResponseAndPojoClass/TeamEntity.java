package com.CoustomControl.ResponseAndPojoClass;

import java.io.Serializable;

public class TeamEntity implements Serializable {
    String start , length , user_id , frm_date,to_date,position;

    public TeamEntity(String start, String length, String user_id, String frm_date, String to_date, String position) {
        this.start = start;
        this.length = length;
        this.user_id = user_id;
        this.frm_date = frm_date;
        this.to_date = to_date;
        this.position = position;
    }
}
