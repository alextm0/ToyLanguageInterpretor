package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import javafx.util.Pair;
import jdk.jshell.EvalException;
import model.adt.IMyDictionary;
import model.adt.IMyHeap;
import model.adt.MyISemaphore;
import model.expressions.IExp;
import model.states.PrgState;
import model.types.IType;
import model.types.IntIType;
import model.values.IValue;
import model.values.IntIValue;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStmt implements IStmt {
  private final String var;
  private final IExp expression;
  private static final Lock lock = new ReentrantLock();

  public CreateSemaphoreStmt(String var, IExp expression) {
    this.var = var;
    this.expression = expression;
  }

  @Override
  public PrgState execute(PrgState state) throws StatementException {
    lock.lock();

    IMyDictionary<String, IValue> symbolTable = state.getSymTable();
    IMyHeap heap = state.getHeap();
    MyISemaphore semaphoreTable = state.getSemaphoreTable();

    IntIValue nr = (IntIValue) (expression.eval(symbolTable, heap));
    int number = nr.getVal();
    int freeAddress = semaphoreTable.getFreeAddress();

    semaphoreTable.put(freeAddress, new Pair<>(number, new ArrayList<>()));

    if (symbolTable.contains(var) && symbolTable.getValue(var).getType().equals(new IntIType()))
      symbolTable.insert(var, new IntIValue(freeAddress));
    else
      throw new StatementException("Variable '" + var + "' does not exist");
    lock.unlock();
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new CreateSemaphoreStmt(var, expression.deepCopy());
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
    if(typeEnv.getValue(var).equals(new IntIType())) {
      if(expression.typecheck(typeEnv).equals(new IntIType())) {
        return typeEnv;
      } else {
        throw new StatementException("Expression is not of int type");
      }
    } else {
      throw new StatementException(String.format("Create sem %s is not of type int", var));
    }
  }

  @Override
  public String toString() {
    return String.format("createSem(%s, %s)", var, expression);
  }
}
