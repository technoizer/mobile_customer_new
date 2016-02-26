package id.ac.its.alpro.customer.component;

import java.io.Serializable;

/**
 * Created by ALPRO on 22/02/2016.
 */
public class Auth implements Serializable{
    Integer id;
    String username, password, token, status;

    public Auth(){

    }

    public Auth(Integer id, String username, String password, String token, String status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.token = token;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString(){
        return getUsername() + " " + getToken() + " " + getStatus();
    }
}
