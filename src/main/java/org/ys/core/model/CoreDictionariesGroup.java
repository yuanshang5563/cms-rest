package org.ys.core.model;

import java.io.Serializable;

public class CoreDictionariesGroup implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7047094431868338404L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column core_dictionaries_group.core_dict_group_id
     *
     * @mbg.generated
     */
    private Long coreDictGroupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column core_dictionaries_group.dict_group_name
     *
     * @mbg.generated
     */
    private String dictGroupName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column core_dictionaries_group.dict_group_code
     *
     * @mbg.generated
     */
    private String dictGroupCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column core_dictionaries_group.dict_group_desc
     *
     * @mbg.generated
     */
    private String dictGroupDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column core_dictionaries_group.parent_core_dict_group_id
     *
     * @mbg.generated
     */
    private Long parentCoreDictGroupId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column core_dictionaries_group.core_dict_group_id
     *
     * @return the value of core_dictionaries_group.core_dict_group_id
     *
     * @mbg.generated
     */
    public Long getCoreDictGroupId() {
        return coreDictGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column core_dictionaries_group.core_dict_group_id
     *
     * @param coreDictGroupId the value for core_dictionaries_group.core_dict_group_id
     *
     * @mbg.generated
     */
    public void setCoreDictGroupId(Long coreDictGroupId) {
        this.coreDictGroupId = coreDictGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column core_dictionaries_group.dict_group_name
     *
     * @return the value of core_dictionaries_group.dict_group_name
     *
     * @mbg.generated
     */
    public String getDictGroupName() {
        return dictGroupName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column core_dictionaries_group.dict_group_name
     *
     * @param dictGroupName the value for core_dictionaries_group.dict_group_name
     *
     * @mbg.generated
     */
    public void setDictGroupName(String dictGroupName) {
        this.dictGroupName = dictGroupName == null ? null : dictGroupName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column core_dictionaries_group.dict_group_code
     *
     * @return the value of core_dictionaries_group.dict_group_code
     *
     * @mbg.generated
     */
    public String getDictGroupCode() {
        return dictGroupCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column core_dictionaries_group.dict_group_code
     *
     * @param dictGroupCode the value for core_dictionaries_group.dict_group_code
     *
     * @mbg.generated
     */
    public void setDictGroupCode(String dictGroupCode) {
        this.dictGroupCode = dictGroupCode == null ? null : dictGroupCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column core_dictionaries_group.dict_group_desc
     *
     * @return the value of core_dictionaries_group.dict_group_desc
     *
     * @mbg.generated
     */
    public String getDictGroupDesc() {
        return dictGroupDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column core_dictionaries_group.dict_group_desc
     *
     * @param dictGroupDesc the value for core_dictionaries_group.dict_group_desc
     *
     * @mbg.generated
     */
    public void setDictGroupDesc(String dictGroupDesc) {
        this.dictGroupDesc = dictGroupDesc == null ? null : dictGroupDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column core_dictionaries_group.parent_core_dict_group_id
     *
     * @return the value of core_dictionaries_group.parent_core_dict_group_id
     *
     * @mbg.generated
     */
    public Long getParentCoreDictGroupId() {
        return parentCoreDictGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column core_dictionaries_group.parent_core_dict_group_id
     *
     * @param parentCoreDictGroupId the value for core_dictionaries_group.parent_core_dict_group_id
     *
     * @mbg.generated
     */
    public void setParentCoreDictGroupId(Long parentCoreDictGroupId) {
        this.parentCoreDictGroupId = parentCoreDictGroupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_dictionaries_group
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CoreDictionariesGroup other = (CoreDictionariesGroup) that;
        return (this.getCoreDictGroupId() == null ? other.getCoreDictGroupId() == null : this.getCoreDictGroupId().equals(other.getCoreDictGroupId()))
            && (this.getDictGroupName() == null ? other.getDictGroupName() == null : this.getDictGroupName().equals(other.getDictGroupName()))
            && (this.getDictGroupCode() == null ? other.getDictGroupCode() == null : this.getDictGroupCode().equals(other.getDictGroupCode()))
            && (this.getDictGroupDesc() == null ? other.getDictGroupDesc() == null : this.getDictGroupDesc().equals(other.getDictGroupDesc()))
            && (this.getParentCoreDictGroupId() == null ? other.getParentCoreDictGroupId() == null : this.getParentCoreDictGroupId().equals(other.getParentCoreDictGroupId()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_dictionaries_group
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCoreDictGroupId() == null) ? 0 : getCoreDictGroupId().hashCode());
        result = prime * result + ((getDictGroupName() == null) ? 0 : getDictGroupName().hashCode());
        result = prime * result + ((getDictGroupCode() == null) ? 0 : getDictGroupCode().hashCode());
        result = prime * result + ((getDictGroupDesc() == null) ? 0 : getDictGroupDesc().hashCode());
        result = prime * result + ((getParentCoreDictGroupId() == null) ? 0 : getParentCoreDictGroupId().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_dictionaries_group
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", coreDictGroupId=").append(coreDictGroupId);
        sb.append(", dictGroupName=").append(dictGroupName);
        sb.append(", dictGroupCode=").append(dictGroupCode);
        sb.append(", dictGroupDesc=").append(dictGroupDesc);
        sb.append(", parentCoreDictGroupId=").append(parentCoreDictGroupId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}