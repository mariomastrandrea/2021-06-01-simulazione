package it.polito.tdp.genes.simulation;

import java.util.Map;

import it.polito.tdp.genes.model.Genes;

public interface SimulationResult
{
	Map<Genes, Integer> getNumEngineersStudyingGenes();
}
