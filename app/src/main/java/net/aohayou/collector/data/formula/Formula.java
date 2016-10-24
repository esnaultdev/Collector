package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Preconditions;

import net.aohayou.collector.data.CollectorProtos;

import java.util.Date;

public class Formula {

    private final String formulaString;
    private final int elementCount;
    private final long creationDate;

    private DiscontinuousRange elements;

    public Formula(@NonNull String formulaString, int elementCount, long creationDate) {
        this.formulaString = Preconditions.checkNotNull(formulaString);
        this.elementCount = elementCount;
        this.creationDate = creationDate;
    }

    public Formula(@NonNull String formulaString, long creationDate) {
        this.formulaString = Preconditions.checkNotNull(formulaString);
        this.creationDate = creationDate;
        convertFormula();
        this.elementCount = elements.size();
    }

    public static Formula fromProto(CollectorProtos.Collection.Formula protoFormula) {
        int elementCount = protoFormula.getElementCount();
        if (elementCount != 0) {
            String formulaString = protoFormula.getFormulaString();
            long creationDate = protoFormula.getCreationDate();
            return new Formula(formulaString, elementCount, creationDate);
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

    public int getLastElement() {
        if (elements == null) {
            convertFormula(); // Lazy conversion
        }
        return elements.getLastElement();
    }

    private void convertFormula() {
        try {
            elements = FormulaConverter.convert(formulaString);
        } catch (InvalidFormulaException e) {
            Log.e("Formula", "Error converting formula: ", e);
            elements = new DiscontinuousRange();
        }
    }

    public String getFormulaString() {
        return formulaString;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public CollectorProtos.Collection.Formula toProto() {
        return CollectorProtos.Collection.Formula.newBuilder()
                .setFormulaString(formulaString)
                .setCreationDate(creationDate)
                .setElementCount(getElementCount())
                .build();
    }

    /**
     * A class representing a formula with no elements.
     */
    private static class EmptyFormula extends Formula {
        private EmptyFormula() {
            super("", 0, new Date().getTime());
        }

        @Override
        public boolean hasElement(int elementNumber) {
            return false;
        }

        @Override
        public int getLastElement() {
            return 0;
        }
    }
}