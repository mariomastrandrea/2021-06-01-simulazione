package it.polito.tdp.genes.simulation;

import it.polito.tdp.genes.model.Genes;

public class GeneStudyEvent implements Comparable<GeneStudyEvent>
{
	private int monthNum;
	private Engineer engineer;
	private Genes geneToStudy;
	
	
	public GeneStudyEvent(int monthNum, Engineer engineer, Genes geneToStudy)
	{
		this.monthNum = monthNum;
		this.engineer = engineer;
		this.geneToStudy = geneToStudy;
	}

	public int getMonthNum()
	{
		return this.monthNum;
	}

	public Engineer getEngineer()
	{
		return this.engineer;
	}

	public Genes getGeneToStudy()
	{
		return this.geneToStudy;
	}

	@Override
	public int compareTo(GeneStudyEvent otherEvent)	//order cronologically
	{
		return Integer.compare(this.monthNum, otherEvent.monthNum);
	}
}
