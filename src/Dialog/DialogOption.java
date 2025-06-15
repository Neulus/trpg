package Dialog;

import Entity.Hero.Hero;

import java.util.function.Consumer;

public class DialogOption {
    private final String text;
    private final String nextNodeId;
    private final Consumer<Hero> action;
    
    public DialogOption(String text, String nextNodeId) {
        this(text, nextNodeId, null);
    }
    
    public DialogOption(String text, String nextNodeId, Consumer<Hero> action) {
        this.text = text;
        this.nextNodeId = nextNodeId;
        this.action = action;
    }
    
    public String getText() { return text; }
    public String getNextNodeId() { return nextNodeId; }
    public Consumer<Hero> getAction() { return action; }
}