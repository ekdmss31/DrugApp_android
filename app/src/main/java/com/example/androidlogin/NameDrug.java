package com.example.androidlogin;

import android.graphics.Bitmap;

//getter와 setter를 정의함. 이것을 이용해서 값을 저장하고 불러올것임
public class NameDrug {

    private Bitmap image;//image는 Bitmap값을 이용해야한다.
    private String drugName;
    private String company;

    private String className;
    private String etcOtcName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEtcOtcName() {
        return etcOtcName;
    }

    public void setEtcOtcName(String etcOtcName) {
        this.etcOtcName = etcOtcName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}