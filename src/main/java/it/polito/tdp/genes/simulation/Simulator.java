package it.polito.tdp.genes.simulation;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.genes.model.Genes;

public class Simulator implements SimulationResult
{
	//constants 
	private static final int TOT_MONTHS = 36;
	private static final double CHANGE_GENE_PROBABILITY = 0.7;
	
	//input
	private Graph<Genes, DefaultWeightedEdge> graph;
	private int numEngineers;
	private Genes startGene;
	
	//events queue
	private PriorityQueue<GeneStudyEvent> eventsQueue;
	
	//world status
	private Map<Engineer, Genes> genesStudiedByEngineers;
	
	//output
	private Map<Genes, Integer> numEngineersStudyingGenes;

	
	public void initialize(int numEngineers, Genes startGene, Graph<Genes, DefaultWeightedEdge> graph)
	{
		if(numEngineers < 1 || graph == null || !this.graph.containsVertex(startGene))
			throw new IllegalArgumentException();
		
		this.graph = graph;
		this.numEngineers = numEngineers;
		this.startGene = startGene;
		
		this.eventsQueue = new PriorityQueue<>();
		//fill queue with events of 1st month
		for(int i=1; i<= numEngineers; i++)
		{
			Engineer newEngineer = new Engineer(i);
			GeneStudyEvent event = new GeneStudyEvent(1, newEngineer, startGene);
		}
		
		this.genesStudiedByEngineers = new HashMap<>();
		
		this.numEngineersStudyingGenes = new HashMap<>();
	}

	public SimulationResult run()
	{
		GeneStudyEvent currentEvent;
		
		while((currentEvent = this.eventsQueue.poll()) != null)
		{
			int month = currentEvent.getMonthNum();
			Engineer engineer = currentEvent.getEngineer();
			Genes studiedGene = currentEvent.getGeneToStudy();
			
			this.genesStudiedByEngineers.put(engineer, studiedGene);
			
			if(month >= TOT_MONTHS) continue;	//stop: 36 months passed
			
			//establish gene to study next month
			Genes nextGeneToStudy = this.getNextGeneToStudyFor(engineer);
			
			GeneStudyEvent nextMonthEvent = new GeneStudyEvent(month+1, engineer, nextGeneToStudy);
			this.eventsQueue.add(nextMonthEvent);
		}
		
		this.computeNumEngineersStudyingGenes();
		
		return this;
	}
	
	private Genes getNextGeneToStudyFor(Engineer engineer)
	{
		Genes lastGene = this.genesStudiedByEngineers.get(engineer);
		
		if(Math.random() < 1 - CHANGE_GENE_PROBABILITY)
		{
			//maintain the same gene
			return lastGene;
		}
		
		//change gene
		Map<Genes, Double> adjacentGenesProbabilities = new HashMap<>();
		double totWeights = 0.0;
		
		for(var edge : this.graph.edgesOf(lastGene))
		{
			double weight = this.graph.getEdgeWeight(edge);
			Genes adjacentGene = Graphs.getOppositeVertex(this.graph, edge, lastGene);
			adjacentGenesProbabilities.put(adjacentGene, weight);
			
			totWeights += weight;
		}
		
		if(totWeights == 0.0 || adjacentGenesProbabilities.isEmpty())
			return lastGene;
		
		for(Genes g : adjacentGenesProbabilities.keySet())
		{
			double w = adjacentGenesProbabilities.get(g);
			adjacentGenesProbabilities.put(g, w/totWeights);
		}
		
		//select gene
		double cumulativeProb = 0.0;
		Genes last = null; //for approximation errors
		double randomNum = Math.random(); //[0, 1)
		
		for(Genes gene : adjacentGenesProbabilities.keySet())
		{
			cumulativeProb += adjacentGenesProbabilities.get(gene);
			
			if(randomNum < cumulativeProb)
				return gene;
			
			last = gene;
		}
		
		return last;	//for approximation errors
	}

	private void computeNumEngineersStudyingGenes()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Genes, Integer> getNumEngineersStudyingGenes()
	{
		return this.numEngineersStudyingGenes;
	}

}
