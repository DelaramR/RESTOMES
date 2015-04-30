package RESTOMES;
import java.util.ArrayList;

public class DataProperty{

    private ArrayList<OntologyClass> domain;
    private ArrayList<String> range;
    private String property;

    public DataProperty(){
	this.domain = new ArrayList<OntologyClass>();
	this.range = new ArrayList<String>();
	this.property = "";
    }

    public DataProperty(ArrayList<OntologyClass> domain, ArrayList<String> range, String property){
	this.domain = domain;
	this.range = range;
	this.property = property;
    }

    public ArrayList<OntologyClass> getDomain(){
	return this.domain;
    }

    public void SetDomain(ArrayList<OntologyClass> domain){
	this.domain = domain;
    }

    public ArrayList<String> getRange(){
	return this.range;
    }

    public void setRange(ArrayList<String> range){
	this.range = range;
    }

    public String getProperty(){
	return this.property;
    }

    public void setProperty(String property){
	this.property = property;
    }
}
