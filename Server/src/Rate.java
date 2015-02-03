package MoviePickRESTfulService;

public class Rate{
  private String rateName;
  private double rateValue;
  
  public Rate(){
    this.rateName = "";
    this.rateValue = 0.0;
  }
  
  public Rate(String name, double value){
    this.rateName = name;
    this.rateValue = value;
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
