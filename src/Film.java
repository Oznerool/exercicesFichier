import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Scanner;

public class Film {
    private String titre;
    private String genre;
    private String duree;
    private String sysnopsis;
    private int index;
    private Arrays movies;

    public Film(String titre, String genre, String duree, String sysnopsis/*,int index*/) {
//        this.index = index ? 1 : index;
        this.titre = titre;
        this.genre = genre;
        this.duree = duree;
        this.sysnopsis = sysnopsis;
    }

//    public IndexList(Scanner scan) {
//
//        while (scan.hasNextLine()) {
////                System.out.println(scan.hasNextInt());
//            String line = scan.nextLine();
//            String[] movielist = line.split(",");
//            Film movie = new Film(movielist[0], movielist[1], movielist[2], movielist[3]);
//            System.out.println(movie.toString());
//        }
//    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getSysnopsis() {
        return sysnopsis;
    }

    public void setSysnopsis(String sysnopsis) {
        this.sysnopsis = sysnopsis;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Arrays getMovies() {
        return movies;
    }

    public void setMovies(Arrays movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "Film{" +
                "titre='" + titre + '\'' +
                ", genre='" + genre + '\'' +
                ", duree='" + duree + '\'' +
                ", sysnopsis='" + sysnopsis + '\'' +
                '}';
    }

    public JSONObject toJson() {
        JSONObject film = new JSONObject();
        film.put("titre", this.titre);
        film.put("genre", this.genre);
        film.put("duree", this.duree);
        film.put("sysnopsis", this.sysnopsis);
        return film;

    }
}
