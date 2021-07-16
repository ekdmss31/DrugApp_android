package com.example.androidlogin;

public class SideDrug {
        //리스트에 띄울 목록
    private String side_ingredient; // 성분명
    private String side_drugName; //품목명
    private String side_company; // 업소명
    private String side_image;//이미지 주소
    private String side_className; //분류명
    private String side_detail; //상세정보
    private String age; //나이
    private String ageDetail; //나이상세

    public String getSide_detail() {
        return side_detail;
    }

    public void setSide_detail(String side_detail) {
        this.side_detail = side_detail;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAgeDetail() {
        return ageDetail;
    }

    public void setAgeDetail(String ageDetail) {
        this.ageDetail = ageDetail;
    }

    public String getSide_ingredient() {
        return side_ingredient;
    }

    public void setSide_ingredient(String side_ingredient) {
        this.side_ingredient = side_ingredient;
    }

    public String getSide_drugName() {
        return side_drugName;
    }

    public void setSide_drugName(String side_drugName) {
        this.side_drugName = side_drugName;
    }

    public String getSide_company() {
        return side_company;
    }

    public void setSide_company(String side_company) {
        this.side_company = side_company;
    }

    public String getSide_image() {
        return side_image;
    }

    public void setSide_image(String side_image) {
        this.side_image = side_image;
    }

    public String getSide_className() {
        return side_className;
    }

    public void setSide_className(String side_className) {
        this.side_className = side_className;
    }
}
