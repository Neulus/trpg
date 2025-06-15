package Dialog;

import Entity.Hero.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DialogNode {
    private final String id;
    private final String npcText;
    private final List<DialogOption> options;
    private final boolean endsConversation;
    
    public DialogNode(String id, String npcText) {
        this(id, npcText, false);
    }
    
    public DialogNode(String id, String npcText, boolean endsConversation) {
        this.id = id;
        this.npcText = npcText;
        this.options = new ArrayList<>();
        this.endsConversation = endsConversation;
    }

    public void addOption(String text, String nextNodeId) {
        options.add(new DialogOption(text, nextNodeId));
    }
    
    public void addOption(String text, String nextNodeId, Consumer<Hero> action) {
        options.add(new DialogOption(text, nextNodeId, action));
    }
    
    public String getId() { return id; }
    public String getNpcText() { return npcText; }
    public List<DialogOption> getOptions() { return options; }
    public boolean endsConversation() { return endsConversation; }
}