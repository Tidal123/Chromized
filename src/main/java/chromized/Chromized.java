package chromized;

import chromized.event.InvokeEvent;
import chromized.event.Priority;

public class Chromized {
    public static final Chromized INSTANCE = new Chromized();

    @InvokeEvent
    public void preinit() {

    }

    @InvokeEvent(priority = Priority.HIGH)
    public void init() {

    }
}
