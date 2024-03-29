package org.ys.core.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.ys.core.model.CoreParameter;
import org.ys.core.model.CoreParameterExample;

@Repository
public interface CoreParameterMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    long countByExample(CoreParameterExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int deleteByExample(CoreParameterExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long coreParamId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int insert(CoreParameter record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int insertSelective(CoreParameter record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    List<CoreParameter> selectByExample(CoreParameterExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    CoreParameter selectByPrimaryKey(Long coreParamId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CoreParameter record, @Param("example") CoreParameterExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CoreParameter record, @Param("example") CoreParameterExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CoreParameter record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_parameter
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CoreParameter record);
}