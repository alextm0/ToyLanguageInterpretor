package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import javafx.util.Pair;
import model.adt.IMyDictionary;
import model.adt.MyISemaphore;
import model.states.PrgState;
import model.types.IType;
import model.types.IntIType;
import model.values.IValue;
import model.values.IntIValue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AcquirePermitStmt implements IStmt {
  private final String variable;
  private static final Lock lock = new ReentrantLock();

  public AcquirePermitStmt(String variable) {
    this.variable = variable;
  }

  @Override
  public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
    lock.lock();
    IMyDictionary<String, IValue> symTable = prgState.getSymTable();
    MyISemaphore semaphoreTable = prgState.getSemaphoreTable();

    if(symTable.contains(variable)) {
      if(symTable.getValue(variable).getType().equals(new IntIType())) {
        IntIValue fi = (IntIValue) symTable.getValue(variable);
        int address = fi.getVal();

        if(semaphoreTable.getSemaphoreTable().containsKey(address)) {
          Pair<Integer, List<Integer>> foundSemaphore = semaphoreTable.get(address);

          int NL = foundSemaphore.getValue().size();
          int N1 = foundSemaphore.getKey();

          if(N1 > NL) {
            if (!foundSemaphore.getValue().contains(prgState.getId())) {
              foundSemaphore.getValue().add(prgState.getId());
              semaphoreTable.put(address, new Pair<>(N1, foundSemaphore.getValue()));
            }
          } else {
            prgState.getExeStack().push(this);
          }
        } else {
          throw new StatementException("Acquire: Index not a key in the semaphore table");
        }
      } else {
        throw new StatementException("Acquire: Index must be of int type");
      }
    } else {
      throw new StatementException("Acquire: Index not in symbol table");
    }

    lock.unlock();
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new AcquirePermitStmt(variable);
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
    if(typeEnv.getValue(variable).equals(new IntIType())) {
      return typeEnv;
    } else {
      throw new StatementException(String.format("Acquire: %s is not int", variable));
    }
  }

  @Override
  public String toString() {
    return String.format("acquire(%s)", variable);
  }
}
