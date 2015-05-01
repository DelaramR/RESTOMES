package RESTOMES;
import java.util.ArrayList;

public class ObjectProperty{

    private ArrayList<OntologyClass> domain;
    private ArrayList<OntologyClass> range;
    private ArrayList<ObjectProperty> inverseOf;
    private String property;

    public ObjectProperty(){
	this.domain = new ArrayList<OntologyClass>();
	this.range = new ArrayList<OntologyClass>();
	this.property = "";
	this.inverseOf = new ArrayList<ObjectProperty>();
    }

    public ObjectProperty(ArrayList<OntologyClass> domain, ArrayList<OntologyClass> range, String property){
	this.domain = domain;
	this.range = range;
	this.property = property;
	this.inverseOf = new ArrayList<ObjectProperty>();
    }

    public ArrayList<OntologyClass> getDomain(){
	return this.domain;
    }

    public void setDomain(ArrayList<OntologyClass> domain){
	this.domain = domain;
    }

    public ArrayList<OntologyClass> getRange(){
	return this.range;
    }

    public void setRange(ArrayList<OntologyClass> range){
	this.range = range;
    }

    public String getProperty(){
	return this.property;
    }

    public void setProperty(String property){
	this.property = property;
    }
    
    public ArrayList<ObjectProperty> getInverseOf(){
    	return this.inverseOf;
    }
    
    public void setInverseOf(ArrayList<ObjectProperty> inverseOf){
    	this.inverseOf = inverseOf;
    }
}
