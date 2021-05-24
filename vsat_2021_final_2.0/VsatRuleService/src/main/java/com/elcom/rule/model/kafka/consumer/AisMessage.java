package com.elcom.rule.model.kafka.consumer;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author anhdv
 */
public class AisMessage implements Serializable {
    
    private String objId;
    private String imo;
    private Integer countryId;
    private Integer typeId;
    private Integer dimA;
    private Integer dimB;
    private Integer dimC;
    private Integer dimD;
    private Float draugth;
    private Float rot;
    private Float sog;
    private Float cog;
    private Float longitude;
    private Float latitude;
    private Integer navstatus;
    private Integer trueHanding;
    private String callSign;
    private String name;
    private String eta;
    private String destination;
    private Long eventTime;
    private Long procTime;
    private String mmsiMaster;
    private Integer isMaster;
    private Long sourceId;
    private Long destId;
    private Integer sourcePort;
    private Integer destPort;
    private String sourceIp;
    private String destIp;
    private Integer direction;
    private Boolean isUfo;
    private Long dataSource;
    private String dataSourceName;
    private List<Integer> areaIds;
    private List<Integer> groupIds;
    private Long count;
    private Integer hasMedia;
    private String partName;
    private String timeKey;
    private String aisKey;

    public AisMessage() {
    }

    public AisMessage(String objId, String imo, Integer countryId, Integer typeId, Integer dimA, Integer dimB, Integer dimC, Integer dimD
            , Float draugth, Float rot, Float sog, Float cog, Float longitude, Float latitude, Integer navstatus, Integer trueHanding
            , String callSign, String name, String eta, String destination, Long eventTime, Long procTime, String mmsiMaster
            , Integer isMaster, Long sourceId, Long destId, Integer sourcePort, Integer destPort, String sourceIp, String destIp
            , Integer direction, Boolean isUfo, Long dataSource, String dataSourceName, List<Integer> areaIds, List<Integer> groupIds
            , Long count, Integer hasMedia, String partName, String timeKey, String aisKey) {
        this.objId = objId;
        this.imo = imo;
        this.countryId = countryId;
        this.typeId = typeId;
        this.dimA = dimA;
        this.dimB = dimB;
        this.dimC = dimC;
        this.dimD = dimD;
        this.draugth = draugth;
        this.rot = rot;
        this.sog = sog;
        this.cog = cog;
        this.longitude = longitude;
        this.latitude = latitude;
        this.navstatus = navstatus;
        this.trueHanding = trueHanding;
        this.callSign = callSign;
        this.name = name;
        this.eta = eta;
        this.destination = destination;
        this.eventTime = eventTime;
        this.procTime = procTime;
        this.mmsiMaster = mmsiMaster;
        this.isMaster = isMaster;
        this.sourceId = sourceId;
        this.destId = destId;
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.sourceIp = sourceIp;
        this.destIp = destIp;
        this.direction = direction;
        this.isUfo = isUfo;
        this.dataSource = dataSource;
        this.dataSourceName = dataSourceName;
        this.areaIds = areaIds;
        this.groupIds = groupIds;
        this.count = count;
        this.hasMedia = hasMedia;
        this.partName = partName;
        this.timeKey = timeKey;
        this.aisKey = aisKey;
    }

    /**
     * @return the objId
     */
    public String getObjId() {
        return objId;
    }

    /**
     * @param objId the objId to set
     */
    public void setObjId(String objId) {
        this.objId = objId;
    }

    /**
     * @return the countryId
     */
    public Integer getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the typeId
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the dimA
     */
    public Integer getDimA() {
        return dimA;
    }

    /**
     * @param dimA the dimA to set
     */
    public void setDimA(Integer dimA) {
        this.dimA = dimA;
    }

    /**
     * @return the dimB
     */
    public Integer getDimB() {
        return dimB;
    }

    /**
     * @param dimB the dimB to set
     */
    public void setDimB(Integer dimB) {
        this.dimB = dimB;
    }

    /**
     * @return the dimC
     */
    public Integer getDimC() {
        return dimC;
    }

    /**
     * @param dimC the dimC to set
     */
    public void setDimC(Integer dimC) {
        this.dimC = dimC;
    }

    /**
     * @return the dimD
     */
    public Integer getDimD() {
        return dimD;
    }

    /**
     * @param dimD the dimD to set
     */
    public void setDimD(Integer dimD) {
        this.dimD = dimD;
    }

    /**
     * @return the draugth
     */
    public Float getDraugth() {
        return draugth;
    }

    /**
     * @param draugth the draugth to set
     */
    public void setDraugth(Float draugth) {
        this.draugth = draugth;
    }

    /**
     * @return the rot
     */
    public Float getRot() {
        return rot;
    }

    /**
     * @param rot the rot to set
     */
    public void setRot(Float rot) {
        this.rot = rot;
    }

    /**
     * @return the sog
     */
    public Float getSog() {
        return sog;
    }

    /**
     * @param sog the sog to set
     */
    public void setSog(Float sog) {
        this.sog = sog;
    }

    /**
     * @return the cog
     */
    public Float getCog() {
        return cog;
    }

    /**
     * @param cog the cog to set
     */
    public void setCog(Float cog) {
        this.cog = cog;
    }

    /**
     * @return the longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the navstatus
     */
    public Integer getNavstatus() {
        return navstatus;
    }

    /**
     * @param navstatus the navstatus to set
     */
    public void setNavstatus(Integer navstatus) {
        this.navstatus = navstatus;
    }

    /**
     * @return the trueHanding
     */
    public Integer getTrueHanding() {
        return trueHanding;
    }

    /**
     * @param trueHanding the trueHanding to set
     */
    public void setTrueHanding(Integer trueHanding) {
        this.trueHanding = trueHanding;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the eta
     */
    public String getEta() {
        return eta;
    }

    /**
     * @param eta the eta to set
     */
    public void setEta(String eta) {
        this.eta = eta;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the eventTime
     */
    public Long getEventTime() {
        return eventTime;
    }

    /**
     * @param eventTime the eventTime to set
     */
    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * @return the procTime
     */
    public Long getProcTime() {
        return procTime;
    }

    /**
     * @param procTime the procTime to set
     */
    public void setProcTime(Long procTime) {
        this.procTime = procTime;
    }

    /**
     * @return the isMaster
     */
    public Integer getIsMaster() {
        return isMaster;
    }

    /**
     * @param isMaster the isMaster to set
     */
    public void setIsMaster(Integer isMaster) {
        this.isMaster = isMaster;
    }

    /**
     * @return the sourceId
     */
    public Long getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId the sourceId to set
     */
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return the destId
     */
    public Long getDestId() {
        return destId;
    }

    /**
     * @param destId the destId to set
     */
    public void setDestId(Long destId) {
        this.destId = destId;
    }

    /**
     * @return the sourcePort
     */
    public Integer getSourcePort() {
        return sourcePort;
    }

    /**
     * @param sourcePort the sourcePort to set
     */
    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    /**
     * @return the destPort
     */
    public Integer getDestPort() {
        return destPort;
    }

    /**
     * @param destPort the destPort to set
     */
    public void setDestPort(Integer destPort) {
        this.destPort = destPort;
    }

    /**
     * @return the sourceIp
     */
    public String getSourceIp() {
        return sourceIp;
    }

    /**
     * @param sourceIp the sourceIp to set
     */
    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    /**
     * @return the destIp
     */
    public String getDestIp() {
        return destIp;
    }

    /**
     * @param destIp the destIp to set
     */
    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    /**
     * @return the direction
     */
    public Integer getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    /**
     * @return the isUfo
     */
    public Boolean getIsUfo() {
        return isUfo;
    }

    /**
     * @param isUfo the isUfo to set
     */
    public void setIsUfo(Boolean isUfo) {
        this.isUfo = isUfo;
    }

    /**
     * @return the dataSource
     */
    public Long getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(Long dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * @return the dataSourceName
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * @param dataSourceName the dataSourceName to set
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * @return the areaIds
     */
    public List<Integer> getAreaIds() {
        return areaIds;
    }

    /**
     * @param areaIds the areaIds to set
     */
    public void setAreaIds(List<Integer> areaIds) {
        this.areaIds = areaIds;
    }

    /**
     * @return the groupIds
     */
    public List<Integer> getGroupIds() {
        return groupIds;
    }

    /**
     * @param groupIds the groupIds to set
     */
    public void setGroupIds(List<Integer> groupIds) {
        this.groupIds = groupIds;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * @return the hasMedia
     */
    public Integer getHasMedia() {
        return hasMedia;
    }

    /**
     * @param hasMedia the hasMedia to set
     */
    public void setHasMedia(Integer hasMedia) {
        this.hasMedia = hasMedia;
    }

    /**
     * @return the partName
     */
    public String getPartName() {
        return partName;
    }

    /**
     * @param partName the partName to set
     */
    public void setPartName(String partName) {
        this.partName = partName;
    }

    /**
     * @return the timeKey
     */
    public String getTimeKey() {
        return timeKey;
    }

    /**
     * @param timeKey the timeKey to set
     */
    public void setTimeKey(String timeKey) {
        this.timeKey = timeKey;
    }

    /**
     * @return the aisKey
     */
    public String getAisKey() {
        return aisKey;
    }

    /**
     * @param aisKey the aisKey to set
     */
    public void setAisKey(String aisKey) {
        this.aisKey = aisKey;
    }

    /**
     * @return the imo
     */
    public String getImo() {
        return imo;
    }

    /**
     * @param imo the imo to set
     */
    public void setImo(String imo) {
        this.imo = imo;
    }

    /**
     * @return the mmsiMaster
     */
    public String getMmsiMaster() {
        return mmsiMaster;
    }

    /**
     * @param mmsiMaster the mmsiMaster to set
     */
    public void setMmsiMaster(String mmsiMaster) {
        this.mmsiMaster = mmsiMaster;
    }
}
