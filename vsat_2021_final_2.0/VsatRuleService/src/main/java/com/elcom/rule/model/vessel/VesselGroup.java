package com.elcom.rule.model.vessel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Size;

@Entity
@Table(name = "vsat02.vessel_group")
public class VesselGroup implements Serializable {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Size(max = 500)
    @Column(name = "note")
    private String note;
    
    @Column(name = "group_type")
    private Integer groupType;
    
    @Column(name = "is_active")
    private Integer isActive;
    
    @Size(max = 100)
    @Column(name = "created_by")
    private String createdBy;
    
    @Size(max = 1000)
    @Column(name = "assign_to")
    private String assignTo;
    
    @Size(max = 200)
    @Column(name = "updated_by")
    private String updatedBy;
    
    @Column(name = "created_time")
    private Timestamp createdTime;
    
    @Column(name = "updated_time")
    private Timestamp updatedTime;
    
    @PrePersist
    void preInsert() {
       if (this.getIsActive()==null)
           this.setIsActive(1);
       if (this.getCreatedTime()==null)
           this.setCreatedTime(new Timestamp(System.currentTimeMillis()));
       if (this.getGroupType()==null)
           this.setGroupType(1);
    }
    
    @PreUpdate
    void preUpdate() {
       if (this.getUpdatedTime()==null)
           this.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
       if (this.getGroupType()==null)
           this.setGroupType(1);
    }
    
    public VesselGroup() {
    }

    public VesselGroup(Long id, String name, String note, Integer isActive, String createdBy, String assignTo, String updatedBy) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.assignTo = assignTo;
        this.updatedBy = updatedBy;
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
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the isActive
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the assignTo
     */
    public String getAssignTo() {
        return assignTo;
    }

    /**
     * @param assignTo the assignTo to set
     */
    public void setAssignTo(String assignTo) {
        this.assignTo = assignTo;
    }

    /**
     * @return the updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the groupType
     */
    public Integer getGroupType() {
        return groupType;
    }

    /**
     * @param groupType the groupType to set
     */
    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }
}
