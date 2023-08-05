package ru.crashdami.emortality.managers;

import ru.crashdami.emortality.objects.Macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager {

    public static final List<Macro> macros = new ArrayList<>();


    public static Macro getMacroById(Integer id) {
        for (Macro macro : macros)
            if (macro.getId() == id)
                return macro;
        return null;
    }
}
