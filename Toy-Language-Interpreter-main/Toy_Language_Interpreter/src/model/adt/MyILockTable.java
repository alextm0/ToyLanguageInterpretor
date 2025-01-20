package model.adt;

import exceptions.ADTException;

import java.util.HashMap;
import java.util.Set;

public interface MyILockTable {
  boolean containsKey(int key);
  int getFreeAddress();
  HashMap<Integer, Integer> getContent();
  int get(int position) throws ADTException;
  Set<Integer> keySet();

  void setContent(HashMap<Integer, Integer> newLockTbl);
  void put(int key, int value) throws ADTException;
}
