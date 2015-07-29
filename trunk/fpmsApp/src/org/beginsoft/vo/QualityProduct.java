package org.beginsoft.vo;

import java.io.Serializable;

/**
 * Created by maren on 2015/5/27.
 */
public class QualityProduct implements Serializable{

    private String id;
    private String allNumber;
    private String workShop;
    private String flowLine;
    private String zstatu;
    private String proceState;
    private String sofaName;
    private String sofaModel;
    private String goodsName;
    private String employeeNumber;
    private String procePersonName;
    private String threeProceNum;
    private String twoProceName;
    private String proceQuantity;
    private String customerMark;
    private String customerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAllNumber() {
        return allNumber;
    }

    public void setAllNumber(String allNumber) {
        this.allNumber = allNumber;
    }

    public String getWorkShop() {
        return workShop;
    }

    public void setWorkShop(String workShop) {
        this.workShop = workShop;
    }

    public String getFlowLine() {
        return flowLine;
    }

    public void setFlowLine(String flowLine) {
        this.flowLine = flowLine;
    }

    public String getZstatu() {
        return zstatu;
    }

    public void setZstatu(String zstatu) {
        this.zstatu = zstatu;
    }

    public String getProceState() {
        return proceState;
    }

    public void setProceState(String proceState) {
        this.proceState = proceState;
    }

    public String getSofaName() {
        return sofaName;
    }

    public void setSofaName(String sofaName) {
        this.sofaName = sofaName;
    }

    public String getSofaModel() {
        return sofaModel;
    }

    public void setSofaModel(String sofaModel) {
        this.sofaModel = sofaModel;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getProcePersonName() {
        return procePersonName;
    }

    public void setProcePersonName(String procePersonName) {
        this.procePersonName = procePersonName;
    }

    public String getThreeProceNum() {
        return threeProceNum;
    }

    public void setThreeProceNum(String threeProceNum) {
        this.threeProceNum = threeProceNum;
    }

    public String getTwoProceName() {
        return twoProceName;
    }

    public void setTwoProceName(String twoProceName) {
        this.twoProceName = twoProceName;
    }

    public String getProceQuantity() {
        return proceQuantity;
    }

    public void setProceQuantity(String proceQuantity) {
        this.proceQuantity = proceQuantity;
    }

    public String getCustomerMark() {
        return customerMark;
    }

    public void setCustomerMark(String customerMark) {
        this.customerMark = customerMark;
    }

    @Override
    public String toString() {
        return "QualityProduct{" +
                "allNumber='" + allNumber + '\'' +
                ", workShop='" + workShop + '\'' +
                ", flowLine='" + flowLine + '\'' +
                ", zstatu='" + zstatu + '\'' +
                ", proceState='" + proceState + '\'' +
                ", sofaName='" + sofaName + '\'' +
                ", sofaModel='" + sofaModel + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", procePersonName='" + procePersonName + '\'' +
                ", threeProceNum='" + threeProceNum + '\'' +
                ", twoProceName='" + twoProceName + '\'' +
                ", proceQuantity='" + proceQuantity + '\'' +
                ", customerMark='" + customerMark + '\'' +
                '}';
    }
}
