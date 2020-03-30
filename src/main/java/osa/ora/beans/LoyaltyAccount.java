package osa.ora.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "loyalty_account")
public class LoyaltyAccount {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "balance")
	private int balance;
	@Column(name = "tier")
	private int tier;
	@Column(name = "enabled")
	private boolean enabled;
	public LoyaltyAccount(){
	}
	public LoyaltyAccount(int id, int balance,int tier, boolean enabled){
		this.id=id;
		this.balance=balance;
		this.tier=tier;
		this.enabled=enabled;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
