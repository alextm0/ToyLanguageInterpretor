package model.adt;

import exceptions.ADTException;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class MySemaphore implements MyISemaphore {
  private HashMap<Integer, Pair<Integer, List<Integer>>> semaphoreTable;
  private int freeAddress;

  public MySemaphore() {
    this.semaphoreTable = new HashMap<>();
    this.freeAddress = 0;
  }

  @Override
  public synchronized void put(int key, Pair<Integer, List<Integer>> value) {
    semaphoreTable.put(key, value);
  }

  @Override
  public synchronized Pair<Integer, List<Integer>> get(int key) throws ADTException {
    if(semaphoreTable.containsKey(key))
      return semaphoreTable.get(key);
    else throw new ADTException(String.format("Semaphore table does not contain key %d", key));
  }

  @Override
  public synchronized int getFreeAddress() {
    freeAddress++;
    return freeAddress;
  }

  @Override
  public synchronized HashMap<Integer, Pair<Integer, List<Integer>>> getSemaphoreTable() {
    return semaphoreTable;
  }

  @Override
  public String toString() {
    return semaphoreTable.toString();
  }
}
