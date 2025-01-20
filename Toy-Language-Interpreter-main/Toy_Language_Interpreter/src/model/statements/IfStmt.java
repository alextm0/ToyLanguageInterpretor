package model.statements;
import exceptions.StatementException;
import model.adt.IMyDictionary;
import model.expressions.IExp;
import model.types.BoolIType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;
import model.states.PrgState;


public class IfStmt implements IStmt{
    private final IExp expression;
    private final IStmt thenStatement;
    private final IStmt elseStatement;

    public IfStmt(IExp e , IStmt t , IStmt el)
    {
        this.expression = e;
        this.thenStatement = t;
        this.elseStatement = el;
    }

    @Override
    public String toString() {
        return "if(" + expression + "){" + thenStatement + "}else{" + elseStatement + "}\n";
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException
    {
        IValue value = expression.eval(state.getSymTable(), state.getHeap());
        if(!value.getType().equals(new BoolIType()))
            throw new StatementException("Expression is not boolean");
        if(((BoolValue)value).getVal())
            state.getExeStack().push(thenStatement);
        else
            state.getExeStack().push(elseStatement);
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new IfStmt(this.expression.deepCopy() , this.thenStatement.deepCopy() , this.elseStatement.deepCopy());
    }

    @Override
    public IMyDictionary<String, IType> typeCheck(IMyDictionary<String, IType> typeEnv) throws StatementException {
        IType typeExp = expression.typecheck(typeEnv);
        if(typeExp.equals(new BoolIType()))
        {
            thenStatement.typeCheck(typeEnv.deepCopy());
            elseStatement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else
            throw new StatementException("IF STATEMENT EXCEPTION: The condition of if has not the type bool");
    }

}
