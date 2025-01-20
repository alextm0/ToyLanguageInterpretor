package model.adt;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import exceptions.ADTException;
import exceptions.EmptyStackException;
import model.statements.IStmt;

public class MyStack<T> implements IMyStack<T>{

    private final Stack<T> stack;

    public MyStack()
    {
        this.stack = new Stack<>();
    }

    @Override
    public T pop() throws EmptyStackException{
        synchronized (stack) {
            if (stack.isEmpty())
                throw new EmptyStackException("Stack is empty");
            return this.stack.pop();
        }
    }

    @Override
    public void push(T v)
    {
        synchronized (stack) {
            stack.push(v);
        }
    }

    @Override
    public boolean isEmpty()
    {
        return this.stack.isEmpty();
    }

    @Override
    public T peek() {
        if (stack.isEmpty()) {
            throw new ADTException("Stack is empty!");
        }
        return stack.peek();
    }

    @Override
    public Stack<T> getStack() {
        return stack;
    }

    @Override
    public List<IStmt> toList() {
        List<IStmt> list = new ArrayList<>();

        for(T element: this.stack)
            list.add((IStmt) element);
        return list;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder("{");
        for(T element: this.stack)
            str.append(element.toString()).append("|");
        if(!this.stack.isEmpty())
            str.deleteCharAt(str.length()-1);
        str.append("}");
        return str.toString();
    }
}
