package it.polito.tdp.genes.simulation;

public class Engineer
{
	private int Id;

	public Engineer(int id)
	{
		Id = id;
	}

	public int getId()
	{
		return this.Id;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + Id;
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
		Engineer other = (Engineer) obj;
		if (Id != other.Id)
			return false;
		return true;
	}
}
