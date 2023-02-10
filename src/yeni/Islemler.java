package yeni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Islemler {

    private float[] puanlar = new float[75];
    public Set<Program> programlar = new LinkedHashSet<Program>();
    private Connection con = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private static int kullaniciId;
    public Islemler() {
        String url = "jdbc:mysql://" + Database.host + ":" + Database.port + "/" + Database.db_ismi;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver bulunamadi.");

        }
        try {
            con = DriverManager.getConnection(url, Database.kullanici_adi, Database.parola);
            System.out.println("Baglanti Basarili");
        } catch (SQLException ex) {
            System.out.println("Baglanti basarisiz!");
        }
        puanOku();
        programlariAta();
    }
    public int izlenmesuresiBul(int kullaniciid, int programid) {
         int toplam = 0;
         String sorgu1 = "Select * from kullaniciprogram where kullaniciid = ? and programid = ?";
           try {
            preparedStatement = con.prepareStatement(sorgu1);

            preparedStatement.setInt(1, kullaniciId);
            preparedStatement.setInt(2, programid);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
               int izlenme = rs.getInt("izlenmesuresi");
               toplam += izlenme;
            }
           }
           catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
           return toplam;
    }
    public int kullaniciProgramUzerineYazma(String programadi, int izlenmesuresi, int puan) {
        int programid = 0;
        int bolumid = 0;
        String sorgu1 = "Select * from program where adi = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu1);
            preparedStatement.setString(1, programadi);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
               programid = rs.getInt("programid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
         String sorgu = "Insert Into kullaniciprogram (kullaniciid, programid,izlenmetarihi,izlenmesuresi, bolumid, puan) VALUES(?,?,?,?,?,?)";
         LocalDate myObj = LocalDate.now(); 
         String date = myObj.toString();
         int toplamizleme = izlenmesuresiBul(kullaniciId, programid) + izlenmesuresi;
         if(programid>50) {
           bolumid = toplamizleme / 40;     
           if(bolumid>10) {
               bolumid = 10;
           }
         }
         else {
             bolumid = 1;
         }
        try {
            preparedStatement = con.prepareStatement(sorgu);
            preparedStatement.setInt(1, kullaniciId);
            preparedStatement.setInt(2, programid);
            preparedStatement.setString(3, date);
            preparedStatement.setInt(4, izlenmesuresi);
            preparedStatement.setInt(5, bolumid);
            preparedStatement.setInt(6, puan);
            preparedStatement.executeUpdate();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        if(bolumid > 1 && toplamizleme>=400) {
            return 2;
        }
        else if(bolumid == 1 && toplamizleme >= 120){
            return 2;
        }
        else {
            return 1;
        }
          
    }
    public ArrayList<Program> kullaniciProgramAtamasi() {
         
          ArrayList<Program> izlenenprogramlar = new ArrayList<Program>();
          izlenenprogramlar.addAll(programlar);
          String sorgu1 = "Select * from kullaniciprogram where kullaniciid = ?";
           try {
            preparedStatement = con.prepareStatement(sorgu1);

            preparedStatement.setInt(1, kullaniciId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
               int programid =  rs.getInt("programid")-1;
               izlenenprogramlar.get(programid).setIzlenmeSuresi(izlenmesuresiBul(kullaniciId, programid+1));
               if(izlenenprogramlar.get(programid).getTip().equals("Film")) {
                  if(izlenenprogramlar.get(programid).getIzlenmeSuresi()>=120) {
                      izlenenprogramlar.get(programid).setIzlenmeDurumu(true);
                  }
                
              }   else {
                        if(izlenenprogramlar.get(programid).getIzlenmeSuresi()>=400) {
                      izlenenprogramlar.get(programid).setIzlenmeDurumu(true);
                  }
              }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
           return izlenenprogramlar;
    }
    public void puanOku() {
        String line;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File("puan.txt")));
            int i = 0;
           
                while ((line = br.readLine()) != null) {
                    puanlar[i] = Float.parseFloat(line);
                    i++;
                }
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        catch (IOException ex) {
                Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

       
    }
    public boolean kullaniciEkle(String ad,String email, String sifre, String dogumtarihi) {
          String sorgu = "Insert Into kullanici (ad,email,sifre,dogumtarihi) VALUES(?,?,?,?)";
        
        try {
            preparedStatement = con.prepareStatement(sorgu);
            
            
            preparedStatement.setString(1, ad);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, sifre);
            preparedStatement.setString(4, dogumtarihi);
            
            preparedStatement.executeUpdate();
            
            return true;
            
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
      
    }
    public int girisyap(String email, String sifre) {
        String sorgu1 = "Select * from kullanici where email = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu1);

            preparedStatement.setString(1, email);

            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return 0;
            }
            else {
                kullaniciId = rs.getInt("kullaniciid");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        String sorgu2 = "Select * from kullanici where email = ? and sifre = ?";
        try {
            preparedStatement = con.prepareStatement(sorgu2);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, sifre);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return 2;
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public TreeSet<String> turAl() {
        String sorgu = "Select * from tur";
        TreeSet<String> tur = new TreeSet<String>();
        try {
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sorgu);

            while (rs.next()) {
                String ad = rs.getString("turadi");
                tur.add(ad);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return tur;
    }

    public ArrayList<String> turbul(int programturid) {
        String sorgu1 = "Select * from programtur where programturid = ?";
        int[] turler = new int[3];
        try {
            preparedStatement = con.prepareStatement(sorgu1);

            preparedStatement.setInt(1, programturid);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                turler[0] = rs.getInt("tur1");
                turler[1] = rs.getInt("tur2");
                turler[2] = rs.getInt("tur3");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        ArrayList<String> tur = new ArrayList<String>();
        for (int i = 0; i < 3; i++) {
            sorgu1 = "Select * from tur where turid = ?";
            if (turler[i] == 0) {
                return tur;
            }
            try {
                preparedStatement = con.prepareStatement(sorgu1);

                preparedStatement.setInt(1, turler[i]);

                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    String turadi = rs.getString("turadi");
                    tur.add(turadi);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return tur;

    }

    public void programlariAta() {
        String sorgu = "Select * from program";
        ArrayList<String> tur = new ArrayList<String>();
        try {
            statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sorgu);
            
            while (rs.next()) {
                int id = rs.getInt("programid");
                String ad = rs.getString("adi");
                String tip = rs.getString("tipi");
                int bolumsayisi = rs.getInt("bolumsayisi");
                int programturid = rs.getInt("programturid");
                tur = turbul(programturid);
                int uzunluk = rs.getInt("uzunluk");
                float puan = puanlar[id-1];
                programlar.add(new Program(id, ad, tip, tur, bolumsayisi, uzunluk, puan));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Islemler.class.getName()).log(Level.SEVERE, null, ex);

        }
        

    }
}