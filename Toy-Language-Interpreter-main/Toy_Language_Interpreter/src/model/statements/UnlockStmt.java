package model.statements;

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

public class UnlockStmt implements IStmt {
  private final String variable;
  private static final Lock lock = new ReentrantLock();

  public UnlockStmt(String var) {
    this.variable = var;
    //lock = new ReentrantLock();
  }

  @Override
  public PrgState execute(PrgState state) throws StatementException {
    lock.lock();

    IMyDictionary<String, IValue> symTable = state.getSymTable();
    MyILockTable lockTable = state.getLockTable();

    if (symTable.contains(variable)) {

      if (symTable.getValue(variable).getType().equals(new IntIType())) {
        IntIValue fi = (IntIValue) symTable.getValue(variable);
        int freeAddress = fi.getVal();

        if (lockTable.containsKey(freeAddress)) {

          if (lockTable.get(freeAddress) == state.getId())
            lockTable.put(freeAddress, -1);

        } else {
          throw new StatementException(String.format("Unlock: Address %d not in the lock table", freeAddress));
        }
      } else {
        throw new StatementException(String.format("Unlock: Variable %s is not of int type", variable));
      }
    } else {
      throw new StatementException(String.format("Unlock: Variable %s is not defined", variable));
    }

    lock.unlock();
    return null;
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
    if (typeEnv.getValue(variable).equals(new IntIType()))
      return typeEnv;
    else
      throw new StatementException(String.format("Lock: Var %s is not of type int!", variable));
  }

  @Override
  public IStmt deepCopy() {
    return new UnlockStmt(variable);
  }

  @Override
  public String toString() {
    return String.format("Unlock(%s)", variable);
  }
}
