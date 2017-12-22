package com.nms.db.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.nms.db.bean.equipment.shelf.SiteInst;
import com.nms.db.bean.system.Field;
import com.nms.db.bean.system.user.UserInst;



public interface FieldMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(Field record);

    Field selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Field record);

    int updateByPrimaryKey(Field record);
    
    public List<Field> queryByUserIdField(@Param("userInst")UserInst userInst);
    
    public List<Field> queryByCondition(@Param("field")Field field);
    public List<Field> queryByConditions(@Param("field")Field field);
    
    public List<Field> comboboxList();
    
    public int insert(@Param("field")Field field,@Param("type")String type); 
    
    public int update(@Param("field")Field field); 
    
    public int delete(@Param("field")Field field); 
    
    public List<Field> selectByGroupId(@Param("fieldId")Integer fieldId);
    
    public List<Field> selectByParentId(@Param("fieldId")Integer fieldId);
    
    public List<Field> querySiteByCondition(@Param("field")Field field); 
    
    public Field getSiteParent(@Param("siteInst")SiteInst siteInst);
    
    public List<Field> queryByConditionAll(@Param("field")Field field,@Param("type")String type);
    
    public List<Field> queryByNetWorkId(@Param("netWorkId")Integer netWorkId);
    
    public List<Field> checkNameExist(@Param("name")String name);
    
    public int updateField(@Param("field")Field field);
    
    public int insertField(@Param("field")Field field);
    
    public List<Field> queryByidCondition(@Param("fieldids")List<Integer> fieldids);

    /**
	 * 	查询子网的上一
	 * @param id 子网父类ID
	 * @return
	 */
	public List<Field> subnetCombo(@Param("id")String id);
	
	/**
	 *  用户权限  
	 *  	查询 能查看的域或者子网
	 * @param userInst
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<Field> queryByUserId(UserInst userInst);
	
	/**
	 * 通过条件查询
	 * @param fieldCondition
	 *            查询条件
	 * @return List<Field>集合
	 */
	public List<Field> queryNoMsite();
	
	/**
	 * 通过主键删除field
	 * 
	 * @param id
	 *            主键
	 * @param connection
	 *            数据库连接
	 * @return 删除记录数
	 * @throws Exception
	 */
	public int deleteById(int id);
	/**
	 * 删除域时，删除该域下所以子�
	 * @param field  子网
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public int deleteSubNet(int parentId);
	
	public int updateBatch(@Param("field")Field field); 
	
	/**
	 * 查询重复
	 * @param afterName 修改前名�?
	 * @param beforeName 修改后名�?
	 * @param parentId 所属域ID
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public int query_name(@Param("afterName")String afterName,@Param("beforeName")String beforeName,@Param("parentId")Integer parentId);
	
	/**
	 * 通过域名字查找域
	 * 
	 * @param name
	 * @param connection
	 * @return
	 * @throws Exception
	 */
	public List<Field> queryByNameCondition(@Param("name")String name);

	List<Field> select_SBN_north();
	
}