package RESTOMES;
import java.util.ArrayList;

public class DataProperty{

    private ArrayList<OntologyClass> domain;
    private String range;
    private String property;

    public DataProperty(){
	this.domain = new ArrayList<OntologyClass>();
	this.range = "";
	this.property = "";
    }

    public DataProperty(ArrayList<OntologyClass> domain, String range, String property){
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

    public String getRange(){
	return this.range;
    }

    public void setRange(String range){
	this.range = range;
    }

    public String getProperty(){
	return this.property;
    }

    public void setProperty(String property){
	this.property = property;
    }
}
