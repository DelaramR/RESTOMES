package RESTOMES;

import java.util.Map;
import java.util.HashMap;

public class Ontology{

    private Strin name;
    private Map<Integer, OntologyClass> ontologyClasses;
    private Map<Integer, ObjectProperty> objectProperties;
    private Map<Integer, DataProperty> dataProperties;

    public Ontology(){
	this.name = "";
	this.ontologyClasses = new HashMap<Integer, OntologyClass>();
	this.objectProperties = new HashMap<Integer, ObjectProperty>();
	this.dataProperties = new HashMap<Integer, DataProperty>();
    }

    public Ontology(String name, Map<Integer, OntologyClass> classes, Map<Integer, ObjectProperty> oProperties, Map<Integer, DataProperty> dProperties){
	this.name = name;
	this.ontologyClasses = classes;
	this.objectProperties = oProperties;
	this.dataProperties = dProperties;
    }

    public String getName(){
	return this.name;
    }

    public void setName(String name){
	this.name = name;
    }

    public Map<Integer, OntologyClass> getOntologyClasses(){
	return this.ontologyClasses;
    }

    public void setOntologyClasses(Map<Integer, OntologyClass> classes){
	this.ontologyClasses = classes;
    }

    public Map<Integer, ObjectProperty> getObjectProperties(){
	return this.objectProperties;
    }

    public void setObjectProperties(Map<Integer, ObjectProperty> oProperties){
	this.objectProperties = oProperties;
    }

    public Map<Integer, DataProperty> getDataProperties(){
	return this.dataProperties;
    }

    public void setDataProperties(Map<Integer, DaraProperty> dProperties){
	this.dataProperties = dProperties;
    }
}
