package yeni;

import java.util.ArrayList;

public class Program implements Comparable<Program>{
      private int id; 
      private String ad;
      private ArrayList<String> tur = new ArrayList<String>();
      private String tip;
      private int bolumsayisi;
      private int uzunluk;
      private float puan;
      private boolean izlenmeDurumu;
      private int izlenmeSuresi;

    public boolean isIzlenmeDurumu() {
        return izlenmeDurumu;
    }

    public void setIzlenmeDurumu(boolean izlenmeDurumu) {
        this.izlenmeDurumu = izlenmeDurumu;
    }

    public int getIzlenmeSuresi() {
        return izlenmeSuresi;
    }

    public void setIzlenmeSuresi(int izlenmeSuresi) {
        this.izlenmeSuresi = izlenmeSuresi;
    }
    public Program(int id, String ad, String tip,ArrayList<String> tur, int bolumsayisi, int uzunluk, float puan) {
        this.id = id;
        this.ad = ad;
        this.tip = tip;
        this.tur.addAll(tur);
        this.bolumsayisi = bolumsayisi;
        this.uzunluk = uzunluk;
        this.puan = puan;
        this.izlenmeDurumu = false;
        this.izlenmeSuresi = 0;
    }

    @Override
    public String toString() {
        return "Program{" + "id=" + id + ", ad=" + ad + ", tur=" + tur + ", tip=" + tip + ", bolumsayisi=" + bolumsayisi + ", uzunluk=" + uzunluk + ", puan=" + puan + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public ArrayList<String> getTur() {
        return tur;
    }

    public void setTur(ArrayList<String> tur) {
        this.tur = tur;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getBolumsayisi() {
        return bolumsayisi;
    }

    public void setBolumsayisi(int bolumsayisi) {
        this.bolumsayisi = bolumsayisi;
    }

    public int getUzunluk() {
        return uzunluk;
    }

    public void setUzunluk(int uzunluk) {
        this.uzunluk = uzunluk;
    }

    public float getPuan() {
        return puan;
    }

    public void setPuan(int puan) {
        this.puan = puan;
    }

    @Override
    public int compareTo(Program o) {
         if(this.puan>o.getPuan()) {
             return -1;
         }
         else if (this.puan<o.getPuan()) {
             return 1;
         }
         return 0;
    }
      
}
