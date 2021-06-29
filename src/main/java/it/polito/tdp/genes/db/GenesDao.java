package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.GenesPair;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Collection<Genes> getAllEssentialGenes(Map<String, Genes> genesIdMap)
	{
		final String sqlQuery = String.format("%s %s %s",
							"SELECT DISTINCT GeneID, Essential, Chromosome",
							"FROM genes",
							"WHERE Essential = \"Essential\"");
		
		Collection<Genes> essentialGenes = new HashSet<>();
		
		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next())
			{
				String geneId = queryResult.getString("GeneID");
				
				if(!genesIdMap.containsKey(geneId))
				{
					Genes newGene = new Genes(geneId, 
											queryResult.getString("Essential"), 
											queryResult.getInt("Chromosome"));
					
					genesIdMap.put(geneId, newGene);
				}
				
				essentialGenes.add(genesIdMap.get(geneId));
			}
			
			queryResult.close();
			statement.close();
			connection.close();
			return essentialGenes;
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Dao error in getAllEssentialGenes()", sqle);
		}
	}

	public Collection<GenesPair> getAllGenesPairs(Map<String, Genes> genesIdMap)
	{
		final String sqlQuery = String.format("%s %s %s %s %s %s %s %s",
				"SELECT GeneID1 AS id1, GeneID2 AS id2, ABS(Expression_Corr) AS correlation",
				"FROM interactions",
				"WHERE GeneID1 IN (SELECT GeneID",
									"FROM genes",
									"WHERE Essential = \"Essential\")",
					"AND GeneID2 IN (SELECT GeneID",
									"FROM genes",
									"WHERE Essential = \"Essential\")");

		Collection<GenesPair> genesPairs = new HashSet<>();

		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			ResultSet queryResult = statement.executeQuery();

			while(queryResult.next())
			{
				String geneId1 = queryResult.getString("id1");
				String geneId2 = queryResult.getString("id2");
				double correlation = queryResult.getDouble("correlation");
				
				if(!genesIdMap.containsKey(geneId1) ||
					!genesIdMap.containsKey(geneId2))
					throw new RuntimeException("Error: geneId not found in id map");
				
				Genes gene1 = genesIdMap.get(geneId1);
				Genes gene2 = genesIdMap.get(geneId2);
				
				if(gene1.getChromosome() == gene2.getChromosome())
					correlation *= 2;
				
				GenesPair newPair = new GenesPair(gene1, gene2, correlation);
				genesPairs.add(newPair);
			}

			queryResult.close();
			statement.close();
			connection.close();
			return genesPairs;
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Dao error in getAllGenesPairs()", sqle);
		}
	}
	
}
