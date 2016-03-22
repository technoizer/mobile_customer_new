package id.ac.its.alpro.customer.component;

import java.io.Serializable;

/**
 * Created by ALPRO on 18/02/2016.
 */
public class Request implements Serializable {

    private String tanggalrequest, namacustomer, nohp, lokasi, tipejasa, catatancustomer, lat, lng, urlAmbil, jamservis, catatanpenyediajasa, tanggalselesai, namapenyediajasa, tanggalbayar, review;
    private Integer broadcast_id,direct_id, hargaperkiraan, hargatotal, transaksi_id, flagtransaksi, rating, tipetransaksi_id;

    public Request(String tanggalrequest, String namacustomer, String nohp, String lokasi, String tipejasa, String catatancustomer, String lat, String lng, String urlAmbil, String jamservis, String catatanpenyediajasa, String tanggalselesai, String namapenyediajasa, String tanggalbayar, String review, Integer broadcast_id, Integer direct_id, Integer hargaperkiraan, Integer hargatotal, Integer transaksi_id, Integer flagtransaksi, Integer rating, Integer tipetransaksi_id) {
        this.tanggalrequest = tanggalrequest;
        this.namacustomer = namacustomer;
        this.nohp = nohp;
        this.lokasi = lokasi;
        this.tipejasa = tipejasa;
        this.catatancustomer = catatancustomer;
        this.lat = lat;
        this.lng = lng;
        this.urlAmbil = urlAmbil;
        this.jamservis = jamservis;
        this.catatanpenyediajasa = catatanpenyediajasa;
        this.tanggalselesai = tanggalselesai;
        this.namapenyediajasa = namapenyediajasa;
        this.tanggalbayar = tanggalbayar;
        this.review = review;
        this.broadcast_id = broadcast_id;
        this.direct_id = direct_id;
        this.hargaperkiraan = hargaperkiraan;
        this.hargatotal = hargatotal;
        this.transaksi_id = transaksi_id;
        this.flagtransaksi = flagtransaksi;
        this.rating = rating;
        this.tipetransaksi_id = tipetransaksi_id;
    }

    public String getTanggalrequest() {
        return tanggalrequest;
    }

    public void setTanggalrequest(String tanggalrequest) {
        this.tanggalrequest = tanggalrequest;
    }

    public String getNamacustomer() {
        return namacustomer;
    }

    public void setNamacustomer(String namacustomer) {
        this.namacustomer = namacustomer;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTipejasa() {
        return tipejasa;
    }

    public void setTipejasa(String tipejasa) {
        this.tipejasa = tipejasa;
    }

    public String getCatatancustomer() {
        return catatancustomer;
    }

    public void setCatatancustomer(String catatancustomer) {
        this.catatancustomer = catatancustomer;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUrlAmbil() {
        return urlAmbil;
    }

    public void setUrlAmbil(String urlAmbil) {
        this.urlAmbil = urlAmbil;
    }

    public String getJamservis() {
        return jamservis;
    }

    public void setJamservis(String jamservis) {
        this.jamservis = jamservis;
    }

    public String getCatatanpenyediajasa() {
        return catatanpenyediajasa;
    }

    public void setCatatanpenyediajasa(String catatanpenyediajasa) {
        this.catatanpenyediajasa = catatanpenyediajasa;
    }

    public String getTanggalselesai() {
        return tanggalselesai;
    }

    public void setTanggalselesai(String tanggalselesai) {
        this.tanggalselesai = tanggalselesai;
    }

    public String getNamapenyediajasa() {
        return namapenyediajasa;
    }

    public void setNamapenyediajasa(String namapenyediajasa) {
        this.namapenyediajasa = namapenyediajasa;
    }

    public String getTanggalbayar() {
        return tanggalbayar;
    }

    public void setTanggalbayar(String tanggalbayar) {
        this.tanggalbayar = tanggalbayar;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Integer getBroadcast_id() {
        return broadcast_id;
    }

    public void setBroadcast_id(Integer broadcast_id) {
        this.broadcast_id = broadcast_id;
    }

    public Integer getDirect_id() {
        return direct_id;
    }

    public void setDirect_id(Integer direct_id) {
        this.direct_id = direct_id;
    }

    public Integer getHargaperkiraan() {
        return hargaperkiraan;
    }

    public void setHargaperkiraan(Integer hargaperkiraan) {
        this.hargaperkiraan = hargaperkiraan;
    }

    public Integer getHargatotal() {
        return hargatotal;
    }

    public void setHargatotal(Integer hargatotal) {
        this.hargatotal = hargatotal;
    }

    public Integer getTransaksi_id() {
        return transaksi_id;
    }

    public void setTransaksi_id(Integer transaksi_id) {
        this.transaksi_id = transaksi_id;
    }

    public Integer getFlagtransaksi() {
        return flagtransaksi;
    }

    public void setFlagtransaksi(Integer flagtransaksi) {
        this.flagtransaksi = flagtransaksi;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTipetransaksi_id() {
        return tipetransaksi_id;
    }

    public void setTipetransaksi_id(Integer tipetransaksi_id) {
        this.tipetransaksi_id = tipetransaksi_id;
    }

    public String toString (){
        return (getBroadcast_id() == null ? getDirect_id(): getBroadcast_id()) + " " + getCatatancustomer() + " "+ getTransaksi_id();
    }
}
