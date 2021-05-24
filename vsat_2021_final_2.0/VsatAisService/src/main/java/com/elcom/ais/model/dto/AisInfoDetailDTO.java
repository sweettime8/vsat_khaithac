package com.elcom.ais.model.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author anhdv
 */
public class AisInfoDetailDTO implements Serializable {
    
    private Long mmsi;
    private Long imo;
    private String vesselName;
    private Long length;
    private Long width;
    private String imagePath;
    private Integer yearOfBuild;
    private Integer draugth;
    private String engineType;
    private Integer grossTonnage;
    private Integer deadWeight;
    private Float speedAvg;
    private Float speedMax;
    private String displacement;
    private String crew;
    private String callSign;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String countryName;
    private String vesselTypeName;
    private String operationUnit;
    private String otherInfo;
    private String owner;
    private String unit;
    private String status;
    private String ipLstStr;
    private String phoneLstStr;
    private String imageLst;
    private List<MmsiIpDTO> ipLst;
    private List<MmsiPhoneDTO> phoneLst;
    private List<AisDTO> aisLst;

    public static void main(String[] args) {
        
        int[] arr1 = {8,0,5,2,1,9};
        int[] arr11 = bubbleSort(arr1);
        
        int[] arr2 = {8,0,5,2,1,9};
        int[] arr22 = bubbleSort2(arr2);
        
        System.out.println("arr11:" + Arrays.toString(arr11));
        System.out.println("arr22:" + Arrays.toString(arr22));
    }
    
    public static int[] bubbleSort(int[] numArray) {
        int n = numArray.length;
        int temp = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (numArray[j - 1] > numArray[j]) {
                    temp = numArray[j - 1];
                    numArray[j - 1] = numArray[j];
                    numArray[j] = temp;
                }

            }
        }
        return numArray;
    }
    
    public static int[] bubbleSort2(int[] numArray) {
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < numArray.length - 1; i++) {
                if (numArray[i] > numArray[i + 1]) {
                    int temp = numArray[i];
                    numArray[i] = numArray[i + 1];
                    numArray[i + 1] = temp;
                    swapped = true;
                }
            }
        }
        return numArray;
    }
    
    public AisInfoDetailDTO() {
    }

    /**
     * @return the mmsi
     */
    public Long getMmsi() {
        return mmsi;
    }

    /**
     * @param mmsi the mmsi to set
     */
    public void setMmsi(Long mmsi) {
        this.mmsi = mmsi;
    }

    /**
     * @return the imo
     */
    public Long getImo() {
        return imo;
    }

    /**
     * @param imo the imo to set
     */
    public void setImo(Long imo) {
        this.imo = imo;
    }

    /**
     * @return the vesselName
     */
    public String getVesselName() {
        return vesselName;
    }

    /**
     * @param vesselName the vesselName to set
     */
    public void setVesselName(String vesselName) {
        this.vesselName = vesselName;
    }

    /**
     * @return the length
     */
    public Long getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(Long length) {
        this.length = length;
    }

    /**
     * @return the width
     */
    public Long getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Long width) {
        this.width = width;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the yearOfBuild
     */
    public Integer getYearOfBuild() {
        return yearOfBuild;
    }

    /**
     * @param yearOfBuild the yearOfBuild to set
     */
    public void setYearOfBuild(Integer yearOfBuild) {
        this.yearOfBuild = yearOfBuild;
    }

    /**
     * @return the draugth
     */
    public Integer getDraugth() {
        return draugth;
    }

    /**
     * @param draugth the draugth to set
     */
    public void setDraugth(Integer draugth) {
        this.draugth = draugth;
    }

    /**
     * @return the engineType
     */
    public String getEngineType() {
        return engineType;
    }

    /**
     * @param engineType the engineType to set
     */
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    /**
     * @return the grossTonnage
     */
    public Integer getGrossTonnage() {
        return grossTonnage;
    }

    /**
     * @param grossTonnage the grossTonnage to set
     */
    public void setGrossTonnage(Integer grossTonnage) {
        this.grossTonnage = grossTonnage;
    }

    /**
     * @return the deadWeight
     */
    public Integer getDeadWeight() {
        return deadWeight;
    }

    /**
     * @param deadWeight the deadWeight to set
     */
    public void setDeadWeight(Integer deadWeight) {
        this.deadWeight = deadWeight;
    }

    /**
     * @return the speedAvg
     */
    public Float getSpeedAvg() {
        return speedAvg;
    }

    /**
     * @param speedAvg the speedAvg to set
     */
    public void setSpeedAvg(Float speedAvg) {
        this.speedAvg = speedAvg;
    }

    /**
     * @return the speedMax
     */
    public Float getSpeedMax() {
        return speedMax;
    }

    /**
     * @param speedMax the speedMax to set
     */
    public void setSpeedMax(Float speedMax) {
        this.speedMax = speedMax;
    }

    /**
     * @return the displacement
     */
    public String getDisplacement() {
        return displacement;
    }

    /**
     * @param displacement the displacement to set
     */
    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    /**
     * @return the crew
     */
    public String getCrew() {
        return crew;
    }

    /**
     * @param crew the crew to set
     */
    public void setCrew(String crew) {
        this.crew = crew;
    }

    /**
     * @return the callSign
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * @param callSign the callSign to set
     */
    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    /**
     * @return the createdTime
     */
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime the createdTime to set
     */
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return the updatedTime
     */
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    /**
     * @param updatedTime the updatedTime to set
     */
    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * @return the countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName the countryName to set
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return the vesselTypeName
     */
    public String getVesselTypeName() {
        return vesselTypeName;
    }

    /**
     * @param vesselTypeName the vesselTypeName to set
     */
    public void setVesselTypeName(String vesselTypeName) {
        this.vesselTypeName = vesselTypeName;
    }

    /**
     * @return the operationUnit
     */
    public String getOperationUnit() {
        return operationUnit;
    }

    /**
     * @param operationUnit the operationUnit to set
     */
    public void setOperationUnit(String operationUnit) {
        this.operationUnit = operationUnit;
    }

    /**
     * @return the otherInfo
     */
    public String getOtherInfo() {
        return otherInfo;
    }

    /**
     * @param otherInfo the otherInfo to set
     */
    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the imageLst
     */
    public String getImageLst() {
        return imageLst;
    }

    /**
     * @param imageLst the imageLst to set
     */
    public void setImageLst(String imageLst) {
        this.imageLst = imageLst;
    }

    /**
     * @return the aisLst
     */
    public List<AisDTO> getAisLst() {
        return aisLst;
    }

    /**
     * @param aisLst the aisLst to set
     */
    public void setAisLst(List<AisDTO> aisLst) {
        this.aisLst = aisLst;
    }

    /**
     * @return the ipLstStr
     */
    public String getIpLstStr() {
        return ipLstStr;
    }

    /**
     * @param ipLstStr the ipLstStr to set
     */
    public void setIpLstStr(String ipLstStr) {
        this.ipLstStr = ipLstStr;
    }

    /**
     * @return the phoneLstStr
     */
    public String getPhoneLstStr() {
        return phoneLstStr;
    }

    /**
     * @param phoneLstStr the phoneLstStr to set
     */
    public void setPhoneLstStr(String phoneLstStr) {
        this.phoneLstStr = phoneLstStr;
    }

    /**
     * @return the ipLst
     */
    public List<MmsiIpDTO> getIpLst() {
        return ipLst;
    }

    /**
     * @param ipLst the ipLst to set
     */
    public void setIpLst(List<MmsiIpDTO> ipLst) {
        this.ipLst = ipLst;
    }

    /**
     * @return the phoneLst
     */
    public List<MmsiPhoneDTO> getPhoneLst() {
        return phoneLst;
    }

    /**
     * @param phoneLst the phoneLst to set
     */
    public void setPhoneLst(List<MmsiPhoneDTO> phoneLst) {
        this.phoneLst = phoneLst;
    }
}
