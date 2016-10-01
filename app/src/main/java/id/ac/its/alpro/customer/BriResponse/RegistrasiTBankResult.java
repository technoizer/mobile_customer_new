package id.ac.its.alpro.customer.BriResponse;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Luffi on 01/10/2016.
 */

public class RegistrasiTBankResult implements Serializable {

    @SerializedName("ResponCode")
    String responseCode ;

    @SerializedName("ResponseDescription")
    String responseDescription ;

    @SerializedName("KodeMerchant")
    String kodeMerchant ;

    @SerializedName("Password")
    String password ;

    @SerializedName("PinNasabah")
    String pinNasabah ;

    @SerializedName("Saldo")
    String saldo ;

    @SerializedName("Token")
    String token ;

    @SerializedName("Nama")
    String nama;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getKodeMerchant() {
        return kodeMerchant;
    }

    public void setKodeMerchant(String kodeMerchant) {
        this.kodeMerchant = kodeMerchant;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPinNasabah() {
        return pinNasabah;
    }

    public void setPinNasabah(String pinNasabah) {
        this.pinNasabah = pinNasabah;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "RegistrasiTBankResult{" +
                "nama='" + nama + '\'' +
                ", token='" + token + '\'' +
                ", saldo='" + saldo + '\'' +
                ", pinNasabah='" + pinNasabah + '\'' +
                ", password='" + password + '\'' +
                ", kodeMerchant='" + kodeMerchant + '\'' +
                ", responseDescription='" + responseDescription + '\'' +
                ", responseCode='" + responseCode + '\'' +
                '}';
    }
}
