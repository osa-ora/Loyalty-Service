package osa.ora;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import osa.ora.beans.LoyaltyAccount;
import osa.ora.beans.LoyaltyTransaction;
import osa.ora.db.LoyaltyDB;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
/**
 * Loyalty Service
 * Related to manage the Loyalty Transactions
 * @author ooransa
 *
 */
@Path("/loyalty/v1")
public class LoyaltyService {
	@Inject
	LoyaltyDB loyaltyDB;
	/**
	 * Useless function after adding smallrye-health dependency
	 * Now you can use: /health/live and /health/ready
	 * It can be extended to do actual readiness tests
	 * @return String
	 */
	@GET
    @Path("/healthcheck")
    @Produces(MediaType.APPLICATION_JSON)
	@Deprecated
    public String check() {
		 return "{\"status\":\"UP\"}";
	}
	/**
	 * Get Loyalty account balance
	 * @param account
	 * @return LoyaltyAccount
	 */
    @GET
    @Path("/balance/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public LoyaltyAccount balance(@PathParam(value = "ID") Integer account) {
    	System.out.println("Get Balance: "+account);
    	LoyaltyAccount loyalty=loyaltyDB.getLoyalty(account);
    	if(loyalty==null){
    		loyalty=new LoyaltyAccount(-1,-1,-1,false);
    	}else{
    		System.out.println("Account data:"+loyalty.getBalance());
    	}
        return loyalty;
    }
    /**
     * Get Loyalty account transaction list
     * @param account
     * @return LoyaltyTransaction[]
     */
    @GET
    @Path("/transactions/{ID}")
    @Produces(MediaType.APPLICATION_JSON)
    public LoyaltyTransaction[] getTransactions(@PathParam(value = "ID") Integer account) {
    	System.out.println("Get Balance: "+account);
    	LoyaltyAccount loyalty=loyaltyDB.getLoyalty(account);
    	if(loyalty==null){
    		loyalty=new LoyaltyAccount(-1,-1,-1,false);
    	}else{
    		System.out.println("Account data:"+loyalty.getBalance());
    		LoyaltyTransaction[] transactions=loyaltyDB.getLoyaltyTransactions(loyalty.getId());
    		return transactions;
    	}
        return null;
    }
    /**
     * Add Loyalty Transaction to a loyalty account
     * OR Redeem loyalty points (points will be minus)
     * @param loyaltyTransaction
     * @return LoyaltyTransaction
     */
    @POST
    @Path("/transactions/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoyaltyTransaction addLoyaltyTransaction(LoyaltyTransaction loyaltyTransaction){
    	System.out.println("Get Balance: "+loyaltyTransaction.getAccount_id());
    	LoyaltyAccount loyalty=loyaltyDB.getLoyalty(loyaltyTransaction.getAccount_id());
    	if(loyalty==null){
    		return null;
    	}else{
    		if(loyaltyDB.updateLoyaltyBalance(loyalty.getId(),loyalty.getBalance()+loyaltyTransaction.getPoints())){
    			LoyaltyTransaction transaction=loyaltyDB.addLoyaltyTransaction(loyaltyTransaction.getAccount_id(),loyaltyTransaction.getPoints(),loyaltyTransaction.getName(), loyaltyTransaction.getDate());
    			return transaction;
    		}else{
    			return null;
    		}
    		
    	}
    }
    /**
     * Create new loyalty account
     * @param loyaltyAccount
     * @return LoyaltyAccount
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public LoyaltyAccount addLoyaltyAccount(LoyaltyAccount loyaltyAccount){
    	LoyaltyAccount loyalty=loyaltyDB.createLoyalty(loyaltyAccount.getBalance(),loyaltyAccount.getTier(),loyaltyAccount.isEnabled());
        return loyalty;    	
    }
 }