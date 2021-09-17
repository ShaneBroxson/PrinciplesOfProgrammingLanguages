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
        if (operands.size() == 1) {
            if (String.valueOf(lVal).contains("-")){
                return new DynamicError("Error: Contains Negative Number");
            }
            return lVal;
        }
        //int lValParts = Integer.parseInt(String.valueOf(lVal));

        ArrayList<Integer> intConversion = new ArrayList<Integer>();
        for (int i = 0; i < operands.size(); i++) {
            NumVal rVal = (NumVal) operands.get(i).accept(this);
            int lValPart = Integer.parseInt(String.valueOf(rVal));
            //System.out.println(lValPart); //show values inputted
            intConversion.add(lValPart);

            //System.out.println(String.valueOf(rVal));
            if (String.valueOf(rVal).contains("-")) {
                //System.out.println("error: contains negative number");
                return new DynamicError("Error: Contains Negative Number");
            }
//            if(intConversion.get(i)<0){
//                return new DynamicError(ts.visit(e));
//            }


        }
        //Arrays.sort(intConversion);
        Collections.sort(intConversion);
//        for (int y = 0; y < intConversion.size(); y++) {
//            System.out.println(intConversion.get(y)); //shows values sorted
//        }


        return new NumVal(intConversion.get(0));
    }

    @Override
    public Value visit(MostExp e) {
        List<Exp> operands = e.all();
        NumVal lVal = (NumVal) operands.get(0).accept(this);
        String error = "Error";
//        if(operands.size()!=0){
//            return new DynamicError("Error");
//        }
        return new DynamicError("Error");
    }

}
