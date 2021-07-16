package com.example.androidlogin;


public class FormDrug {
    //리스트에 띄울 목록
    private String drugName; //품목명
    private String company; // 업소명
    private String image;//이미지 주소
    private String className; //분류명
    private String etcOtcName; // 전문일반구분

    //검색할때 사용, 리스트에 띄우지 않음
    private String shape; //모양
    private String color; //색상
    private String type; //제형
    private String markfront; // 식별 표시 앞
    private String markback; // 식별 표시 뒤

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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



    //////////////모양 검색할때 사용//////////////

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMarkfront() {
        return markfront;
    }

    public void setMarkfront(String markfront) {
        this.markfront = markfront;
    }

    public String getMarkback() {
        return markback;
    }

    public void setMarkback(String markback) {
        this.markback = markback;
    }

}