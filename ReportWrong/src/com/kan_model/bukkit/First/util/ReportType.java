package com.kan_model.bukkit.First.util;

/**
 * Created by kgdwhsk on 2016/10/6.
 */
public enum ReportType {
/*    public static final int  THEFT = 1;
    public static final int DESTROY = 2;
    public static final int SBUG = 3;*/
    THEFT(1)/*{
    @Override
        public int getReportType() {
            return super.getReportType();
        }
    }*/,
    DESTROY(2),
    SBUG(3);
    private int type;
    private ReportType(int type){
        this.type = type;
    }

    public int getReportType(){
        return this.type;
    }

    @Override
    public String toString() {
        return String.valueOf(getReportType());
    }
}
