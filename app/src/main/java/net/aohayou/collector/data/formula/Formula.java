package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Preconditions;

import net.aohayou.collector.data.CollectorProtos;

public class Formula {

    private static FormulaConverter converter = new FormulaConverter();

    private final String formulaString;
    private final int elementCount;

    private DiscontinuousRange elements;

    public Formula(@NonNull String formulaString, int elementCount) {
        this.formulaString = Preconditions.checkNotNull(formulaString);
        this.elementCount = elementCount;
    }

    public static Formula fromProto(CollectorProtos.Collection.Formula protoFormula) {
        int elementCount = protoFormula.getElementCount();
        if (elementCount != 0) {
            String formulaString = protoFormula.getFormulaString();
            return new Formula(formulaString, elementCount);
        } else {
            return emptyFormula();
        }
    }

    public static Formula emptyFormula() {
        return new EmptyFormula();
    }

    public int getElementCount() {
        return elementCount;
    }

    public boolean hasElement(int elementNumber) {
        if (elements == null) {
            convertFormula(); // Lazy conversion
        }
        return elements.contains(elementNumber);
    }

    private void convertFormula() {
        try {
            elements = converter.convert(formulaString);
        } catch (InvalidFormulaException e) {
            Log.e("Formula", "Error converting formula: ", e);
            elements = new DiscontinuousRange();
        }
    }

    public String getFormulaString() {
        return formulaString;
    }


    /**
     * A class representing a formula with no elements.
     */
    private static class EmptyFormula extends Formula {
        private EmptyFormula() {
            super("", 0);
        }

        @Override
        public boolean hasElement(int elementNumber) {
            return false;
        }
    }
}