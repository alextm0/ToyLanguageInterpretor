package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.IMyDictionary;
import model.adt.IMyStack;
import model.expressions.ArithmeticalExpression;
import model.expressions.IExp;
import model.expressions.RelationalExpression;
import model.states.PrgState;
import model.types.IType;

import java.io.IOException;

public class SwitchStmt implements IStmt {
  IExp exp, exp1, exp2;
  IStmt stmt1, stmt2, stmt3;

  public SwitchStmt(IExp exp, IExp exp1, IExp exp2, IStmt stmt1, IStmt stmt2, IStmt stmt3) {
    this.exp = exp;
    this.exp1 = exp1;
    this.exp2 = exp2;
    this.stmt1 = stmt1;
    this.stmt2 = stmt2;
    this.stmt3 = stmt3;
  }

  @Override
  public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
    IMyStack<IStmt> exeStack = prgState.getExeStack();
    IStmt newStmt = new IfStmt(
            new RelationalExpression(exp, "==", exp1),
            stmt1,
            new IfStmt(
                    new RelationalExpression(exp, "=", exp2),
                    stmt2,
                    stmt3
            )
    );

    exeStack.push(newStmt);
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return new SwitchStmt(exp.deepCopy(), exp1.deepCopy(), exp2.deepCopy(), stmt1.deepCopy(), stmt2.deepCopy(), stmt3.deepCopy());
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
    IType typeExp = exp.typecheck(typeEnv);
    IType typeExp1 = exp1.typecheck(typeEnv);
    IType typeExp2 = exp2.typecheck(typeEnv);

    if (!typeExp.equals(typeExp1) || !typeExp.equals(typeExp2)) {
      throw new StatementException("Switch expression and case expressions must have the same type.");
    }

    stmt1.typeCheck(typeEnv);
    stmt2.typeCheck(typeEnv);
    stmt3.typeCheck(typeEnv);

    return typeEnv;
  }

  @Override
  public String toString() {
    return String.format("switch(%s) (case %s : %s) (case %s : %s) (default : %s)", exp, exp1, stmt1, exp2, stmt2, stmt3);
  }
}
