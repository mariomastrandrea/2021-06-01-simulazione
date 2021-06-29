package it.polito.tdp.genes.model;

public class GenesPair
{
	private final Genes gene1;
	private final Genes gene2;
	private final double correlation;
	
	public GenesPair(Genes gene1, Genes gene2, double correlation)
	{
		this.gene1 = gene1;
		this.gene2 = gene2;
		this.correlation = correlation;
	}

	public Genes getGene1()
	{
		return this.gene1;
	}

	public Genes getGene2()
	{
		return this.gene2;
	}

	public double getCorrelation()
	{
		return this.correlation;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gene1 == null) ? 0 : gene1.hashCode());
		result = prime * result + ((gene2 == null) ? 0 : gene2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenesPair other = (GenesPair) obj;
		if (gene1 == null)
		{
			if (other.gene1 != null)
				return false;
		}
		else
			if (!gene1.equals(other.gene1))
				return false;
		if (gene2 == null)
		{
			if (other.gene2 != null)
				return false;
		}
		else
			if (!gene2.equals(other.gene2))
				return false;
		return true;
	}
	
}
