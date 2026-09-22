package interfaces;

import java.util.List;

public interface Navigable {

    List<String> findShortestPath(String from, String to);
}