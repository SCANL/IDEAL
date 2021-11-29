package com.github.astyer.naturallanguagelabplugin.rules;

import com.github.astyer.naturallanguagelabplugin.IR.Method;

public interface MethodCheckbox {
    CheckboxResult visitMethod(Method method);
}
