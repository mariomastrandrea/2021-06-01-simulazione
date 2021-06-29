package it.polito.tdp.genes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Model;
import it.polito.tdp.genes.simulation.SimulationResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController 
{
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private ComboBox<Genes> cmbGeni;

    @FXML
    private Button btnGeniAdiacenti;

    @FXML
    private TextField txtIng;

    @FXML
    private Button btnSimula;

    @FXML
    private TextArea txtResult;
    
    private Model model;
    

    @FXML
    void doCreaGrafo(ActionEvent event) 
    {
    	this.model.createGraph();
    	
    	//print
    	int numVertices = this.model.getNumVertices();
    	int numEdges = this.model.getNumEdges();
    	
    	String output = this.printGraphInfo(numVertices, numEdges);
    	this.txtResult.setText(output);
    	
    	//update UI
    	List<Genes> genes = this.model.getGenes();
    	this.cmbGeni.getItems().clear();
    	this.cmbGeni.getItems().addAll(genes);
    }

    private String printGraphInfo(int numVertices, int numEdges)
	{
		return String.format("Grafo creato\n#Vertici: %d\n#Archi: %d", numVertices, numEdges);
	}

	@FXML
    void doGeniAdiacenti(ActionEvent event) 
    {
		if(!this.model.isGraphCreated())
		{
			this.txtResult.setText("Errore: creare prima il grafo!");
			return;
		}
		
		Genes selectedGene = this.cmbGeni.getValue();
		
		if(selectedGene == null)
		{
			this.txtResult.setText("Errore: selezionare un gene dal menù a tendina");
			return;
		}
		
		Map<Genes, Double> adjacentGenesWeights = 
						this.model.getGenesWeightsAdjacentTo(selectedGene);
		
		List<Genes> orderedAdjacentGenes = new ArrayList<>(adjacentGenesWeights.keySet());
		
		orderedAdjacentGenes.sort((g1, g2) -> 
			Double.compare(adjacentGenesWeights.get(g2), adjacentGenesWeights.get(g1)));
		
		String output = this.printGenesWeight(selectedGene, 
							orderedAdjacentGenes, adjacentGenesWeights);
		
		this.txtResult.setText(output);
    }

    private String printGenesWeight(Genes selectedGene, 
    		List<Genes> orderedAdjacentGenes, Map<Genes, Double> adjacentGenesWeights)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("Geni adiacenti a ").append(selectedGene.toString()).append(":\n");
		
		if(orderedAdjacentGenes.isEmpty())
		{
			sb.append("(nessuno)");
			return sb.toString();
		}
		
		for(Genes g : orderedAdjacentGenes)
		{
			sb.append("\n - ").append(g.toString()).append(" --> ").append(adjacentGenesWeights.get(g));
		}
		
		return sb.toString();
	}

	@FXML
    void doSimula(ActionEvent event) 
    {
		if(!this.model.isGraphCreated())
		{
			this.txtResult.setText("Errore: creare prima il grafo!");
			return;
		}
		
		Genes selectedGene = this.cmbGeni.getValue();
		
		if(selectedGene == null)
		{
			this.txtResult.setText("Errore: selezionare un gene dal menù a tendina");
			return;
		}
		
		String numEngineersInput = this.txtIng.getText();
		
		if(numEngineersInput == null || numEngineersInput.isBlank())
		{
			this.txtResult.setText("Errore: inserire un numero di Ingegneri (n) nell'apposita casella di testo");
			return;
		}
		
		numEngineersInput = numEngineersInput.trim();
		
		int numEngineers;
		try
		{
			numEngineers = Integer.parseInt(numEngineersInput);
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.setText("Errore: inserire un numero intero valido di Ingegneri");
			return;
		}
		
		if(numEngineers < 1)
		{
			this.txtResult.setText("Errore: inserire un numero intero di Ingegneri almeno pari a 1");
			return;
		}
		
		SimulationResult result = this.model.runSimulationWith(numEngineers, selectedGene);
		
		Map<Genes, Integer> numEngineersStudyingGenes = result.getNumEngineersStudyingGenes();
		
		String output = this.printEngineersStudyingGenes(numEngineers, selectedGene, 
															numEngineersStudyingGenes);
		this.txtResult.setText(output);
    }

    private String printEngineersStudyingGenes(int numEngineers, Genes selectedGene, 
    		Map<Genes, Integer> numEngineersStudyingGenes)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Geni in corso di studio dagli ingengneri (#").append(numEngineers).append("):\n");
		
		for(var pair : numEngineersStudyingGenes.entrySet())
		{
			Genes gene = pair.getKey();
			int num = pair.getValue();
			
			sb.append("\n - ").append(gene.toString()).append("  ->  #").append(num);
		}
		
		sb.append("\nGene di partenza: ").append(selectedGene.toString());
		
		return sb.toString();
	}

	@FXML
    void initialize() 
    {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGeni != null : "fx:id=\"cmbGeni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnGeniAdiacenti != null : "fx:id=\"btnGeniAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtIng != null : "fx:id=\"txtIng\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model)
    {
    	this.model = model;
    }
}
