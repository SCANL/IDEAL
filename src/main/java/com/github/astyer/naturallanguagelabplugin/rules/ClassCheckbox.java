package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Class;

public interface ClassCheckbox {
    CheckboxResult visitClass(Class c);
}
