package model.adt;

import exceptions.ADTException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public interface MyISemaphore {
  void put(int key, Pair<Integer, List<Integer>> value);
  Pair<Integer, List<Integer>> get(int key) throws ADTException;
  int getFreeAddress();
  HashMap<Integer, Pair<Integer, List<Integer>>> getSemaphoreTable();
}
