package RESTOMES;

public class DataProperty{

    private OntologyClass domain;
    private String range;
    private String property;

    public DataProperty(){
	this.domain = new OntologyClass();
	this.range = "";
	this.property = "";
    }

    public DataProperty(OntologyClass domain, String range, String property){
	this.domain = domain;
	this.range = range;
	this.property = property;
    }

    public OntologyClass getDomain(){
	return this.domain;
    }

    public void SetDomain(OntologyClass domain){
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
