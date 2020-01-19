package com.besttime.workhorse;

public enum Hours {
    h6_00(6), h7_00(7), h8_00(8), h9_00(9), h10_00(10), h11_00(11), h12_00(12), h13_00(13), h14_00(14),
    h15_00(15), h16_00(16), h16_30(16.30), h17_00(17), h17_30(17.30),
    h18_00(18), h18_30(18.30), h19_00(19), h19_30(19.30), h20_00(20), h20_30(20.30),
    h21_00(21), h21_30(21.30), h22_00(22);


    private double hourVal;

    Hours(double hourVal) {
        this.hourVal = hourVal;
    }

    public double getHourValue(){
        return hourVal;
    }
}
