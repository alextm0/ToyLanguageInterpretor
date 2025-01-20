package model.statements;

import exceptions.ADTException;
import exceptions.StatementException;
import model.adt.IMyDictionary;
import model.adt.IMyStack;
import model.expressions.IExp;
import model.expressions.RelationalExpression;
import model.expressions.VariableExpression;
import model.states.PrgState;
import model.types.IType;
import model.types.IntIType;

import java.io.IOException;

public class ForStatement implements IStmt {
  // for(v=exp1; v<exp2; v=exp3) stmt
  String variable;
  IExp exp1, exp2, exp3;
  IStmt statement;

  public ForStatement(String variable, IExp exp1, IExp exp2, IExp exp3, IStmt statement) {
    this.variable = variable;
    this.exp1 = exp1;
    this.exp2 = exp2;
    this.exp3 = exp3;
    this.statement = statement;
  }

  @Override
  public PrgState execute(PrgState prgState) throws StatementException, ADTException, IOException {
    // int v; v=exp1; (while(v<exp2) stmt; v=exp3)

    IMyStack<IStmt> exeStack = prgState.getExeStack();

    IStmt newStmt = new CompStmt(
            new VariablesDeclarationStmt(variable, new IntIType()),
            new CompStmt(
                    new AssignStmt(variable, exp1),
                    new WhileStatement(
                            exp2,
                            new CompStmt(
                                    statement,
                                    new AssignStmt(variable, exp3)
                            )
                    )
            )
    );

    exeStack.push(newStmt);
    return null;
  }

  @Override
  public IStmt deepCopy() {
    return null;
  }

  @Override
  public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
    // !FIXME Add this

    return typeEnv;
  }

  @Override
  public String toString() {
    //return String.format("While(%s) {%s}", expression, statement);

    // format: for(variable=exp1; variable < exp2; variable = exp3) { statement }

    return String.format("For(%s = %s, %s, %s) { %s }", variable, exp1, exp2, exp3, statement);
  }
}
