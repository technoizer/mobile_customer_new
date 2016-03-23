package id.ac.its.alpro.customer.component;

/**
 * Created by ALPRO on 23/03/2016.
 */
public class Provider {
    String namapenyediajasa, foto, nohp, namaperusahaan, jamservis, hargaperkiraan, rating, penyediajasa_id;

    public Provider(String namapenyediajasa, String foto, String nohp, String namaperusahaan, String jamservis, String hargaperkiraan, String rating, String penyediajasa_id) {
        this.namapenyediajasa = namapenyediajasa;
        this.foto = foto;
        this.nohp = nohp;
        this.namaperusahaan = namaperusahaan;
        this.jamservis = jamservis;
        this.hargaperkiraan = hargaperkiraan;
        this.rating = rating;
        this.penyediajasa_id = penyediajasa_id;
    }

    public String getNamapenyediajasa() {
        return namapenyediajasa;
    }

    public void setNamapenyediajasa(String namapenyediajasa) {
        this.namapenyediajasa = namapenyediajasa;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getNamaperusahaan() {
        return namaperusahaan;
    }

    public void setNamaperusahaan(String namaperusahaan) {
        this.namaperusahaan = namaperusahaan;
    }

    public String getJamservis() {
        return jamservis;
    }

    public void setJamservis(String jamservis) {
        this.jamservis = jamservis;
    }

    public String getHargaperkiraan() {
        return hargaperkiraan;
    }

    public void setHargaperkiraan(String hargaperkiraan) {
        this.hargaperkiraan = hargaperkiraan;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPenyediajasa_id() {
        return penyediajasa_id;
    }

    public void setPenyediajasa_id(String penyediajasa_id) {
        this.penyediajasa_id = penyediajasa_id;
    }

    public String toString (){
        return getNamapenyediajasa();
    }
}
