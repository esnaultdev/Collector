package net.aohayou.collector.data.formula;

import net.aohayou.collector.data.CollectorProtos;

public class Formula {

    private String formulaString;
    private int elementCount;

    public static Formula fromProto(CollectorProtos.Collection.Formula protoFormula) {
        Formula formula = new Formula();
        formula.formulaString = protoFormula.getFormulaString();
        formula.elementCount = protoFormula.getElementCount();
        return formula;
    }

    public static Formula emptyFormula() {
        Formula formula = new Formula();
        formula.formulaString = "";
        formula.elementCount = 0;
        return formula;
    }

    public int getElementCount() {
        return elementCount;
    }

    public boolean hasElement(int elementNumber) {
        //TODO parse formula
        return false;
    }

    public String getFormulaString() {
        return formulaString;
    }
}