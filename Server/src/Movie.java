package MoviePickRESTfulService;

public class Movie{
  private String movieId;
  private String movieTitle;
  private Rate rate;
  
  public Movie(){
    this.movieId = "";
    this.movieTitle = "";
    this.rate = new Rate();
  }
  
  public Movie(String movieId, String movieTitle, Rate rate){
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.rate = new Rate(rate.getRateId(), rate.getRateName(), rate.getRateValue());
  }
  
  public String getMovieId(){
    return this.movieId;
  }
  
  public void setMovieId(String id){
    this.movieId = id;
  }
  
  public String getMovieTitle(){
    return movieTitle;
  }
  
  public void setMovieTitle(String title){
    this.movieTitle = title;
  }
  
  public Rate getRate(){
    return rate;
  }
  
  public void setRate(Rate rate){
    this.rate = new Rate(rate.getRateId(), rate.getRateName(), rate.getRateValue());
  }
}
