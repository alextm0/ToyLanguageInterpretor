package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.IMyDictionary;
import model.adt.MyILockTable;
import model.states.PrgState;
import model.types.IType;
import model.types.IntIType;
import model.values.IValue;
import model.values.IntIValue;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateLockStmt implements IStmt {
  private final String variable;
  private static final Lock lock = new ReentrantLock();

  public CreateLockStmt(String variable)
  {
    this.variable = variable;
    //lock = new ReentrantLock();
  }

  @Override
  public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
    lock.lock();

    MyILockTable lockTable = prgState.getLockTable();
    IMyDictionary<String, IValue> symbolTable = prgState.getSymTable();

    int freeAddress = lockTable.getFreeAddress();
    lockTable.put(freeAddress, -1); // Ensure correct address is used here
    symbolTable.insert(variable, new IntIValue(freeAddress)); // Assign the lock address to the variable


    lock.unlock();
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new CreateLockStmt(variable);
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
    if (typeEnv.getValue(variable).equals(new IntIType()))
      return typeEnv;
    else throw new StatementException(String.format("CreateLock: Variable %s should be of type int", variable));
  }

  @Override
  public String toString() {
    return String.format("createLock(%s)", variable);
  }
}
