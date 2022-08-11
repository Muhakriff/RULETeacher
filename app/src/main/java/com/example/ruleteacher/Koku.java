package com.example.ruleteacher;

public class Koku {
    String docID, elemen, kodPerkara, perkara;
    float skor;

    public Koku() {
    }

    public Koku(String docID, String elemen, String kodPerkara, String perkara, float skor) {
        this.docID = docID;
        this.elemen = elemen;
        this.kodPerkara = kodPerkara;
        this.perkara = perkara;
        this.skor = skor;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getElemen() {
        return elemen;
    }

    public void setElemen(String elemen) {
        this.elemen = elemen;
    }

    public String getKodPerkara() {
        return kodPerkara;
    }

    public void setKodPerkara(String kodPerkara) {
        this.kodPerkara = kodPerkara;
    }

    public String getPerkara() {
        return perkara;
    }

    public void setPerkara(String perkara) {
        this.perkara = perkara;
    }

    public float getSkor() {
        return skor;
    }

    public void setSkor(float skor) {
        this.skor = skor;
    }
}
