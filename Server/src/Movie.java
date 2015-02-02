package MoviePickRESTfulService;

import java.util.ArrayList;

public class Movie{
  private String movieId;
  private String movieTitle;
  private Rate rate;
  private ArrayList<Genre> genres;
  
  public Movie(){
    this.movieId = "";
    this.movieTitle = "";
    this.rate = new Rate();
    this.genres = new ArrayList<Genre>();
  }
  
  public Movie(String movieId, String movieTitle, Rate rate, ArrayList<Genre> genres){
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.rate = new Rate(rate.getRateId(), rate.getRate());
    this.genres = new ArrayList<Genre>();
    for(Genre genre : genres)
      this.genres.add(genre);
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
    this.rate = new Rate(rate.getRateId, this.getRate());
  }
  
  public ArrayList<Genre> getGenres(){
    return this.genres;
  }
  
  public void setGenres(ArrayList<Genre> genres){
    this.genres = new ArrayList<Genre>();
    for(Genre genre : genres)
      this.genres.add(genre);
  }
  
  public void addGenre(Genre genre){
    this.genres.add(genre);
  }
}
