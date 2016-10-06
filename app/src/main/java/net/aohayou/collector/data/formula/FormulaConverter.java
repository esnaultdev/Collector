package net.aohayou.collector.data.formula;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class FormulaConverter {

    public List<Range> convert(@NonNull String formulaString) throws InvalidFormulaException {
        Preconditions.checkArgument(!TextUtils.isEmpty(formulaString));


        return new ArrayList<>();
    }
}
