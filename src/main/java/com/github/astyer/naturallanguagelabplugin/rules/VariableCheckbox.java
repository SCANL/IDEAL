package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Variable;

public interface VariableCheckbox {
    CheckboxResult visitVariable(Variable variable);
}
