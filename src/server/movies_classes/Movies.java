package server.movies_classes;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.*;


/**
 *    Movie - Movie collection class
 *    @param  {HashSet<Movie>} moviesList
 *    @param  {java.util.Date} initializationDate
 * */

@Root(name="movies")
public class Movies {

    @ElementList(name="moviesList")
    private HashSet<Movie> moviesList = new HashSet<>();
    @Attribute
    private java.util.Date initializationDate;



    public Movies(){
        this.initializationDate = new Date();
    }
    public int moviesCount(){
        return moviesList.size();
    }

    public HashSet<Movie> getMovies(){
        return moviesList;
    }
    public List<Movie> getSortedMovies() {
        List<Movie> sortedList = new ArrayList<>(moviesList);
        sortedList.sort(new Comparator<Movie>() {
            public int compare(Movie o1, Movie o2) {
                return (Integer.compare(o1.getId() - o2.getId(), 0));
            }
        });
        return sortedList;
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

}
