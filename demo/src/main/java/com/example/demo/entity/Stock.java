package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Stock {

	@Id
	@GeneratedValue
	private Long id;

	private Date recordDatetime;
	private int numGainers;
	private int numLosers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRecordDatetime() {
		return recordDatetime;
	}

	public void setRecordDatetime(Date recordDatetime) {
		this.recordDatetime = recordDatetime;
	}

	public int getNumGainers() {
		return numGainers;
	}

	public void setNumGainers(int numGainers) {
		this.numGainers = numGainers;
	}

	public int getNumLosers() {
		return numLosers;
	}

	public void setNumLosers(int numLosers) {
		this.numLosers = numLosers;
	}

}
