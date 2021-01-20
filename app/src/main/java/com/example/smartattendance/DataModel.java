package com.example.smartattendance;

public class DataModel {

    int img;
    String cname, stotal, ttotal, subtotal;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

//    public String getStotal() {
//        return stotal;
//    }
//
//    public void setStotal(String stotal) {
//        this.stotal = stotal;
//    }
//
//    public String getTtotal() {
//        return ttotal;
//    }
//
//    public void setTtotal(String ttotal) {
//        this.ttotal = ttotal;
//    }
//
//    public String getSubtotal() {
//        return subtotal;
//    }
//
//    public void setSubtotal(String subtotal) {
//        this.subtotal = subtotal;
//    }

    public DataModel(int img, String cname) {
        this.img = img;
        this.cname = cname;
//        this.stotal = stotal;
//        this.ttotal = ttotal;
//        this.subtotal = subtotal;

//        , String stotal, String ttotal, String subtotal
    }
}
