package arithlang;

import static arithlang.AST.*;
import static arithlang.Value.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Evaluator implements Visitor<Value> {
    private NumVal record = new NumVal(0);
    Printer.Formatter ts = new Printer.Formatter();

    Value valueOf(Program p) {
        // Value of a program in this language is the value of the expression
        return (Value) p.accept(this);
    }

    @Override
    public Value visit(AddExp e) {
        List<Exp> operands = e.all();
        double result = 0;
        for (Exp exp : operands) {
            NumVal intermediate = (NumVal) exp.accept(this); // Dynamic type-checking
            result += intermediate.v(); //Semantics of AddExp in terms of the target language.
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(NumExp e) {
        return new NumVal(e.v());
    }

    @Override
    public Value visit(DivExp e) {
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            if (rVal.v() == 0) {
                return new DynamicError(ts.visit(e)); //Dynamic Error allows us to customize our error message.
            }
            result = result / rVal.v();
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(MultExp e) {
        List<Exp> operands = e.all();
        double result = 1;
        for (Exp exp : operands) {
            NumVal intermediate = (NumVal) exp.accept(this); // Dynamic type-checking
            result *= intermediate.v(); //Semantics of MultExp.
        }
        return new NumVal(result);
    }

    @Override
    public Value visit(Program p) {
        return (Value) p.e().accept(this);
    }

    @Override
    public Value visit(SubExp e) {
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);
        double result = lVal.v();
        for (int i = 1; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            result = result - rVal.v();
        }
        return new NumVal(result);
    }

    @Override
    // Homework assignment code.
    public Value visit(LeastExp e) {
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);

        /**
         * If only number was inputted
         */
        if (operands.size() == 1) {
            // Negative check
            if (String.valueOf(lVal).contains("-")){
                return new DynamicError("Error: Contains Negative Number");
            }
            return lVal;
        }
       // Initialize Array
        ArrayList<Integer> intConversion = new ArrayList<Integer>();
        for (int i = 0; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this); //Take each number inputted in list
            int lValPart = Integer.parseInt(String.valueOf(rVal)); // Convert list type to integer
            intConversion.add(lValPart); // Populate Array with integer

            // Negative check
            if (String.valueOf(rVal).contains("-")) {
                return new DynamicError("Error: Contains Negative Number");
            }
        }
        Collections.sort(intConversion); // Sort new array
        return new NumVal(intConversion.get(0));
    }

    /**
     * For dealing with > case
     */
    @Override
    public Value visit(MostExp e) {

        return new DynamicError("Error");
    }

    @Override
    public Value visit(Len e) {
        List<Exp> operands = e.all();
        //System.out.println(operands.size());
        //NumVal length = ((NumVal) operands.size());
        double result = operands.size();
        return new NumVal(result);
    }

    @Override
    public Value visit(Unique e) {
        List<Exp> operands = e.all();
        ArrayList<Integer> intConversion = new ArrayList<Integer>();
        for(int i = 0; i < operands.size(); i++){
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            int lValPart = Integer.parseInt(String.valueOf(rVal));
            if(!intConversion.contains(lValPart)){

                intConversion.add(lValPart);

            }
        }
        String s = "";
        String fin = "";
        for(int y = 0; y < intConversion.size(); y++){
            fin = s.concat(String.valueOf(intConversion.get(y)));
        }

    Value[] elems  = new Value[intConversion.size()];
        for(int i=0; i<intConversion.size(); i++)
            elems[i] = (Value) operands.get(i).accept(this);
        Value result = new Value() {
            @Override
            public String toString() {
                return null;
            }
        };
        //result = elems;
        for(int i=0; i<operands.size(); i++)
            result = elems[i];
       // NumVal stringarr =  (NumVal) fin ;

        //System.out.println(operands.size());
        //NumVal length = ((NumVal) operands.size());
       // double result = operands.size();

        return result;
    }

}
