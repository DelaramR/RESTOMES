package MoviePickRESTfulService;

public class Rate{
  private String rateId;
  private String rateName;
  private double rateValue;
  
  public Rate(){
    this.rateId = "";
    this.rateName = "";
    this.rateValue = 0.0;
  }
  
  public Rate(String id, String name, double value){
    this.rateId = id;
    this.rateName = name;
    this.rateValue = value;
  }
  
  public String getRateId(){
    return rateId;
  }
  
  public void setRateId(String id){
    this.rateId = id;
  }
  
  public String getRateName(){
    return this.rateName;
  }
  
  public void setRateName(String name){
    this.rateName = name;
  }
  
  public double getRateValue(){
    return this.rateValue;
  }
  
  public void setRateValue(double value){
    this.rateValue = value;
  }
}
