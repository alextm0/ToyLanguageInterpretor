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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStmt implements IStmt {
  private final String variable;
  private static final Lock lock = new ReentrantLock();

  public LockStmt(String variable) {
    this.variable = variable;
    //lock = new ReentrantLock();
  }

  @Override
  public PrgState execute(PrgState state) throws StatementException {
    lock.lock();

    IMyDictionary <String, IValue> symTable = state.getSymTable();
    MyILockTable lockTable = state.getLockTable();

    if (symTable.contains(variable)) {

      if (symTable.getValue(variable).getType().equals(new IntIType())) {
        IntIValue fi = (IntIValue) symTable.getValue(variable);
        int address = fi.getVal();

        if (lockTable.containsKey(address)) {
          if (lockTable.get(address) == -1) {

            lockTable.put(address, state.getId());
            state.setLockTable(lockTable);

          } else {
            state.getExeStack().push(this);
          }
        } else {
          throw new StatementException(String.format("Lock: Address %s is not in the lock table", address));
        }
      } else {
        throw new StatementException(String.format("Lock: Variable %s should be of type int", variable));
      }
    } else {
      throw new StatementException(String.format("Lock: Variable %s not defined", variable));
    }
    lock.unlock();
    return null;
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException, ADTException {
    if (typeEnv.getValue(variable).equals(new IntIType())) {
      return typeEnv;
    } else {
      throw new StatementException(String.format("Lock: Variable %s should be of type int", variable));
    }
  }

  @Override
  public IStmt deepCopy() {
    return new LockStmt(variable);
  }

  @Override
  public String toString() {
    return String.format("lock (%s)", variable);
  }
}
