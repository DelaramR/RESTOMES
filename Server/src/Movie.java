package MoviePickRESTfulService;

import java.util.ArrayList;

public class Movie{
  private String movieId;
  private String movieTitle;
  private String rateId;
  
  public Movie(){
    this.movieId = "";
    this.movieTitle = "";
    this.rateId = "";
  }
  
  public Movie(String movieId, String movieTitle, String rateId){
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.rateId = rateId;
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
  
  public String getRateId(){
    return rateId;
  }
  
  public void setRateId(String id){
    this.rateId = id;
  }
}
