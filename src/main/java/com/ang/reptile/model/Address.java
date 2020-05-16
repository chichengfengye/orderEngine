package com.ang.reptile.model;

public class Address {
    private NameCode province;
    private NameCode city;
    private NameCode county;
    private NameCode town;

    public Address(NameCode province, NameCode city, NameCode county, NameCode town) {
        this.province = province;
        this.city = city;
        this.county = county;
        this.town = town;
    }

    public NameCode getProvince() {
        return province;
    }

    public void setProvince(NameCode province) {
        this.province = province;
    }

    public NameCode getCity() {
        return city;
    }

    public void setCity(NameCode city) {
        this.city = city;
    }

    public NameCode getCounty() {
        return county;
    }

    public void setCounty(NameCode county) {
        this.county = county;
    }

    public NameCode getTown() {
        return town;
    }

    public void setTown(NameCode town) {
        this.town = town;
    }
}
