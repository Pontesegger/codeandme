package com.codeandme.debugger.textinterpreter.debugger.dispatcher;

import com.codeandme.debugger.textinterpreter.debugger.events.IDebugEvent;

public interface IEventProcessor {

    void handleEvent(IDebugEvent event);
}
