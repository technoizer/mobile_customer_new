package id.ac.its.alpro.customer.component;

import java.io.Serializable;

/**
 * Created by Luffi on 27/02/2016.
 */
public class Mandiri implements Serializable {
    String phone, password;
    int id;

    public Mandiri() {
    }

    public Mandiri(String phone, String password, int id) {
        this.phone = phone;
        this.password = password;
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getPhone() + " " + getPassword();
    }
}
