package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import it.polito.tdp.genes.db.GenesDao;

public class Model 
{
	private final GenesDao dao;
	private Graph<Genes, DefaultWeightedEdge> graph;
	private final Map<String, Genes> genesIdMap;
	
	
	public Model()
	{
		this.dao = new GenesDao();
		this.genesIdMap = new HashMap<>();
	}
	
	public void createGraph()
	{
		this.graph = GraphTypeBuilder.<Genes, DefaultWeightedEdge>undirected()
									.allowingMultipleEdges(false)
									.allowingSelfLoops(false)
									.weighted(true)
									.edgeClass(DefaultWeightedEdge.class)
									.buildGraph();
		
		//add vertices
		Collection<Genes> essentialGenes = this.dao.getAllEssentialGenes(this.genesIdMap);
		Graphs.addAllVertices(this.graph, essentialGenes);
		
		//add edges
		Collection<GenesPair> allGenesPairs = this.dao.getAllGenesPairs(this.genesIdMap);
		
		for(var pair : allGenesPairs)
		{
			Genes gene1 = pair.getGene1();
			Genes gene2 = pair.getGene2();
			
			if(gene1.equals(gene2)) continue; //loops not allowed
			
			double weight = pair.getCorrelation();
			
			if(!this.graph.containsEdge(gene1, gene2))
				Graphs.addEdge(this.graph, gene1, gene2, weight);
		}
	}

	public int getNumVertices() { return this.graph.vertexSet().size(); }
	public int getNumEdges() { return this.graph.edgeSet().size(); }
	public boolean isGraphCreated() { return this.graph != null; }
	
	public List<Genes> getGenes()
	{
		if(this.graph == null) return null;
		
		List<Genes> orderedGenes = new ArrayList<>(this.graph.vertexSet());
		orderedGenes.sort((g1, g2) -> g1.getGeneId().compareTo(g2.getGeneId()));
		//ordered according to alphabetical order of GeneIDs
		
		return orderedGenes;
	}

	public Map<Genes, Double> getGenesWeightsAdjacentTo(Genes selectedGene)
	{
		if(selectedGene == null || !this.graph.containsVertex(selectedGene))
			return null; //error occurred
		
		Map<Genes, Double> adjacentGenesWeights = new HashMap<>();
		
		for(var edge : this.graph.edgesOf(selectedGene))
		{
			double weight = this.graph.getEdgeWeight(edge);
			Genes adjacentGene = Graphs.getOppositeVertex(this.graph, edge, selectedGene);
			
			adjacentGenesWeights.put(adjacentGene, weight);
		}
		
		return adjacentGenesWeights;
	}
	
}
