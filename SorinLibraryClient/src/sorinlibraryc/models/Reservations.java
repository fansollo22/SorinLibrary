/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author Flagster
 */
public class Reservations implements Serializable {

    private Integer id;
    private Users userId;
    private Date date;

    public Reservations() {
    }
    
    public Reservations(JSONObject obj){
        this.id = obj.getInt("id");
        this.userId = new Users(new JSONObject(obj.get("userId").toString()));
        String d = obj.getString("date");
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date dt;
        try{
           dt = formatter.parse(d); 
        }
        catch (Exception ex)
        {
            dt = new Date();
        }
        LocalDate ds = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.date = java.sql.Date.valueOf(ds);
    }

    public Reservations(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }
    
    public Date getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservations)) {
            return false;
        }
        Reservations other = (Reservations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Reservations[ id=" + id + " ]";
    }
}
