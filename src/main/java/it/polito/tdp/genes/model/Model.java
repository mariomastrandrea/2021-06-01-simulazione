package it.polito.tdp.genes.model;

import it.polito.tdp.genes.db.GenesDao;

public class Model 
{
	private final GenesDao dao;
	
	public Model()
	{
		this.dao = new GenesDao();
	}
	
	public void createGraph()
	{
		
	}
	
}
