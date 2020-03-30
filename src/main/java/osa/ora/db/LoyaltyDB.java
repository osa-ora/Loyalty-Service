package osa.ora.db;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import osa.ora.beans.LoyaltyAccount;
import osa.ora.beans.LoyaltyTransaction;

@ApplicationScoped
public class LoyaltyDB {
	@Inject
	EntityManager em;
	/**
	 * Method to create new Loyalty Account
	 * @param balance
	 * @param tier
	 * @param enabled
	 * @return LoyaltyAccount
	 */
	@Transactional
	public LoyaltyAccount createLoyalty(int balance,int tier, boolean enabled) {
		LoyaltyAccount loyalty=new LoyaltyAccount(0,balance,tier,enabled);
		em.persist(loyalty);
		return loyalty;
	}
	/**
	 * Method to get Loyalty account by ID
	 * @param id
	 * @return LoyaltyAccount
	 */
	@Transactional
	public LoyaltyAccount getLoyalty(int id) {
		LoyaltyAccount loyalty=em.find(LoyaltyAccount.class,id);
		return loyalty;
	}
	/**
	 * Method to delete a Loyalty account (when account is closed)
	 * @param id
	 * @return boolean
	 */
	@Transactional
	public boolean deleteLoyalty(int id) {
		LoyaltyAccount loyalty=em.find(LoyaltyAccount.class,id);
		if(loyalty!=null){
			em.remove(loyalty);
			return true;
		}
		return false;
	}
	/**
	 * Method to get list of loyalty account transactions
	 * @param account_id
	 * @return LoyaltyTransaction[]
	 */
	@SuppressWarnings("unchecked")
	public LoyaltyTransaction[] getLoyaltyTransactions(int account_id) {
		List<LoyaltyTransaction> loyaltyTransactionList = null;
        Query query = em.createQuery("SELECT a FROM LoyaltyTransaction a WHERE a.account_id = :account_id");
        query.setParameter("account_id", account_id);
        query.setMaxResults(10);
        try {
        	loyaltyTransactionList = (List<LoyaltyTransaction>) query.getResultList();
        	if(loyaltyTransactionList.isEmpty()) return null;
            return loyaltyTransactionList.toArray(new LoyaltyTransaction[0]);
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load loyalty transaction!");
            ex.printStackTrace();
            return null;
        }
	}
	/**
	 * Method to add loyalty transaction
	 * @param account_id
	 * @param points
	 * @param name
	 * @param date
	 * @return LoyaltyTransaction
	 */
	@Transactional
	public LoyaltyTransaction addLoyaltyTransaction(int account_id, int points,String name, String date) {
		LoyaltyTransaction loyaltyTransaction=new LoyaltyTransaction(0,account_id,points,name,date);
		em.persist(loyaltyTransaction);
		return loyaltyTransaction;
	}
	/**
	 * Method to update the loyalty balance
	 * @param id
	 * @param newBalance
	 * @return boolean
	 */
	@Transactional
	public boolean updateLoyaltyBalance(int id, int newBalance) {
		LoyaltyAccount loyalty=em.find(LoyaltyAccount.class,id);
		loyalty.setBalance(newBalance);
		em.persist(loyalty);
		return true;
	}
}
