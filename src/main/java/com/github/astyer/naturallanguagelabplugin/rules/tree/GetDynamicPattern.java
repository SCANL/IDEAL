package com.github.astyer.naturallanguagelabplugin.rules.tree;

import com.github.astyer.naturallanguagelabplugin.IR.Identifier;
import com.kipust.regex.Pattern;

public interface GetDynamicPattern {
    Pattern getDynamicPattern(Identifier identifier);
}
