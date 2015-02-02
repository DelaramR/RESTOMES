package MoviePickRESTfulService;

public class Genre{
  private String genreId;
  private String name;
  
  public Genre(){
    this.genreId = "";
    this.name = "";
  }
  
  public Genre(String genreId, String name){
    this.genreId = genreId;
    this.name = name;
  }
  
  public String getGenreId(){
    return this.genreId;
  }
  
  public void setGenreId(String id){
    this.genreId = id;
  }
  
  public String getName(){
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }
}
