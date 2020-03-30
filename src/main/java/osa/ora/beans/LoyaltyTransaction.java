package osa.ora.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "loyalty_transaction")
public class LoyaltyTransaction {
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "account_id")
	private int account_id;
	@Column(name = "points")
	private int points;
	@Column(name = "name")
	@Size(max = 30)
	private String name;
	@Column(name = "date")
	private String date;
	public LoyaltyTransaction(){
	}
	public LoyaltyTransaction(int id,int account_id, int points,String name, String date){
		this.id=id;
		this.points=points;
		this.name=name;
		this.account_id=account_id;
		this.date=date;	
	}
	
	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
