package org.ys.core.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.ys.core.model.CoreRole;
import org.ys.core.model.CoreRoleExample;

@Repository
public interface CoreRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    long countByExample(CoreRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int deleteByExample(CoreRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long coreRoleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int insert(CoreRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int insertSelective(CoreRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    List<CoreRole> selectByExample(CoreRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    CoreRole selectByPrimaryKey(Long coreRoleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") CoreRole record, @Param("example") CoreRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") CoreRole record, @Param("example") CoreRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(CoreRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table core_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(CoreRole record);
    
    List<CoreRole> listCoreRolesByUserId(Long coreUserId);
}