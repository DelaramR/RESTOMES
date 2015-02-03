package MoviePickRESTfulService;

import java.util.ArrayList;

public class ShowTime{
  private Movie movie;
  private Theater theater;
  private ArrayList<String> date;
  private ArrayList<String> time;
  
  public ShowTime(){
    this.movie = new Movie();
    this.theater = new Theater();
    this.date = new ArrayList<String>();
    this.time = new ArrayList<String>();
  }
  
  public ShowTime(Movie movie, Theater theater, ArrayList<String> date, ArrayList<String> time){
    this.movie = new Movie(movie.getMovieTitle(), movie.getRate());
    this.theater = new Theater(theater.getTheaterName(), theater.getTheaterAddress());
    this.date = date;
    this.time = time;
  }
  
  public Movie getMovie(){
    return this.movie;
  }
  
  public void setMovie(Movie movie){
    this.movie = new Movie(movie.getMovieId(), movie.getMovieTitle(), movie.getRate());
  }
  
  public Theater getTheater(){
    return this.theater;
  }
  
  public void setTheater(Theater theater){
    this.theater = new Theater(theater.getTheaterId(), theater.getTheaterName(), theater.getTheaterAddress());
  }
  
  public ArrayList<String> getDate(){
    return this.date;
  }
  
  public void setDate(ArrayList<String> date){
    this.date = date;
  }
  
  public void addDate(String date){
    this.date.add(date);
  }
  
  public ArrayList<String> getTime(){
    return this.time;
  }
  
  public void setTime(ArrayList<String> time){
    this.time = time;
  }
  
  public void addTime(String time){
    this.time.add(time);
  }
}
