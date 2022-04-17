package com.yaoting.utf.domain.func;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FuncFactory {
    private Map<String, Func<?>> funcs = new HashMap<>();

    @PostConstruct
    public void init() {
        {
            SleepFunc sleepFunc = new SleepFunc(5000L);
            funcs.put(sleepFunc.name(), sleepFunc);
        }
        {
            StartFunc startFunc = new StartFunc();
            funcs.put(startFunc.name(), startFunc);
        }
        {
            EndFunc endFunc = new EndFunc();
            funcs.put(endFunc.name(), endFunc);
        }
    }

    public Optional<Func<?>> findFunc(String name) {
        return Optional.ofNullable(funcs.get(name));
    }

}
