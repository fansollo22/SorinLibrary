/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorinlibraryc.models;

import java.io.Serializable;
import org.json.JSONObject;
import sorinlibraryc.models.Books;
import sorinlibraryc.models.Users;

/**
 *
 * @author Flagster
 */

public class Reviews implements Serializable {

    private Integer id;
    private String review;
    private Users userid;
    private int rating;

    public Reviews() {
    }

    public Reviews(JSONObject obj)
    {
        this.id = obj.getInt("id");
        this.rating = obj.getInt("rating");
        this.review = obj.getString("review");
        this.userid = new Users(new JSONObject(obj.get("userid").toString()));
    }
    
    public Reviews(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
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
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sorinlibrarys.entities.Reviews[ id=" + id + " ]";
    }
    
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
}
