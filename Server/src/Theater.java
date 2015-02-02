package MoviePickRESTfulService;

public class Theater{
  private String theaterId;
  private String theaterName;
  private String theaterAddress;
  
  public Theater(){
    this.theaterId = "";
    this.theaterName = "";
    this.theaterAddress = "";
  }
  
  public Theater(String id, String name, String address){
    this.theaterId = id;
    this.theaterName = name;
    this.theaterAddress = address;
  }
  
  public String getTheaterId(){
    return this.theaterId;
  }
  
  public void setTheaterId(String id){
    this.theaterId = id;
  }
  
  public String getTheaterName(){
    return this.theaterName;
  }
  
  public void setTheaterName(String name){
    this.theaterName = name;
  }
  
  public String getTheaterAddress(){
    return this.theaterAddress;
  }
  
  public void setTheaterAddress(String address){
    this.theaterAddress = theater;
  }
}
