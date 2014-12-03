/**
 * 
 */
package com.pj3.pos_manager.database;

import java.util.List;

import com.pj3.pos_manager.res_obj.Bill;
import com.pj3.pos_manager.res_obj.Employee;
import com.pj3.pos_manager.res_obj.Food;
import com.pj3.pos_manager.res_obj.FoodStatistic;
import com.pj3.pos_manager.res_obj.Order;
import com.pj3.pos_manager.res_obj.Position;

/**
 * @author L�C�ng
 *
 */
public interface SqliteAPIs {
	//Users actions
	/**
	 * Create user
	 * @user user will be created
	 * @return id of user is created if success or -1 if fail
	 */
	public int createUser(Employee user);
	
	/**
	 * get user by userId
	 * @param userId
	 * @return user has id: userId
	 */
	public Employee getUser(int userId);
	
	/**
	 * Get all user on server
	 * @return all employee 
	 */
	public List<Employee> getAllUsers();
	
	/**
	 * get Employee by positon
	 * @param postionId
	 * @return list employee 
	 */
	public List<Employee> getUserByPosition(int postionId);
	
	/**
	 * update user on server
	 * @param user
	 * @return true if success of false if fail
	 */
	public boolean updateUser(Employee user);
	
	/**
	 * Check user login 
	 * @param username
	 * @param password
	 * @return true if success or false if fail
	 */
	public boolean checkUser(String username, String password);
	
	/**
	 * Delete user by userId
	 * @param userId
	 * @return true if success or false if fail
	 */
	public boolean deleteUser(int userId);
	
	
	//MENU ACTION
	/**
	 * Create new food in menu
	 * @param menu
	 * @return id of new food if success or -1 if fail
	 */
	public int createFood(Food menu);
	
	/**
	 * Update menu 
	 * @param menu
	 * @return true if success or false if fail
	 */
	public  boolean updateMenu(Food menu);
	
	/**
	 * Change status of food
	 * @param foodId
	 * @param status true: enable or false: disable
	 * @return true if success or false if fail
	 */
	public boolean changeStatusFood(int foodId, boolean status);
	
	/**
	 * Get food by food Id
	 * @param foodId
	 * @return return Food
	 */
	public Food getFood(int foodId);
	
	/**
	 * Get list foods by status
	 * @param type true when food was displayed or false when food hide
	 * @return
	 */
	public List<Food> getFoodsByStatus(boolean type);
	
	/**
	 * Get all food in menu
	 * @return list food
	 */
	public List<Food> getAllFood();
	
	//BILL ACTION 
	/**
	 * Create bill
	 * @param bill
	 * @return id of new bill
	 */
	public int createBill(Bill bill);
	
	/**
	 * get bill info
	 * @param billId
	 * @return bill
	 */
	public Bill getBill(int billId);
	
	/**
	 * get all  bill on server
	 * @return list all bill
	 */
	public List<Bill> getAllBill();
	
	/**
	 * delete bill on server
	 * @param billId
	 * @return true if success or false if fail
	 */
	public boolean deleteBill(int billId);
	
	//BILL ACTION TEMPORARY
	/**
	 * Create bill temporary
	 * @param bill
	 * @return id of new bill
	 */
	public int createBillTemp(Order order);
	
	/**
	 * Update bill temporary
	 * @param bill
	 * @return true if success or false if fail
	 */
	public boolean updateBillTemp(Order order);
	
	/**
	 * Clear file temporary
	 * @return true if success or false if fail
	 */
	public boolean clearFileTemp();
	
	/**
	 * Delete bill temporary
	 * @param billId
	 * @return true if success or false if fail
	 */
	public boolean deleteBillTemp(int billId);
	
	/**
	 * Get bill temporary
	 * @param billId
	 * @return bill temporary 
	 */
	public Order getBillTemp(int billId);
	/**
	 * Get all current orders
	 * @return List<Order>
	 * */
	public List<Order> getOrderList();
	
	//Food statistic
	/**
	 * new cooking status
	 * @param foodStatistic
	 * @return 
	 */
	public int createFoodStatistic(FoodStatistic foodStatistic);
	
	/**
	 * Get food statistic by food id
	 * @param f_m_id food id
	 * @return list foodstatistic
	 */
	public List<FoodStatistic> getStatisticByFoodId(int f_m_id);
	
	/**
	 * get all cooking order
	 * @return list food statistic
	 */
	public List<FoodStatistic> getCookingOrder(int billId);
	
	
	/**
	 * Update food statistic on server
	 * @param fStatistic
	 * @return true if success or false if fail
	 */
	public boolean updateFoodStatus( FoodStatistic fStatistic);
	
	/**
	 * Create position
	 * @param position
	 * @return
	 */
	public int createPosition(Position position);
	
	/**
	 * Get all position
	 * @return
	 */
	public List<Position> getPositions();
}
