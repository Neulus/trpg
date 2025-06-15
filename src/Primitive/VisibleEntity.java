package Primitive;

import Entity.Entity;

public class VisibleEntity {
    private final Position position;
    private final Entity entity;
    private final double visibility;
    private final String angle;

    public VisibleEntity(Entity entity, Position position, double visibility, String angle) {
        this.entity = entity;
        this.position = position;
        this.visibility = visibility;
        this.angle = angle;
    }

    public String render() {
        String name = entity.getName();
        String renderedDistance;
        if (visibility < 0.3)
            renderedDistance = name + "처럼 보이는 실루엣이 저 멀리 희미하게 보입니다.";
        else if (visibility < 0.6)
            renderedDistance = name + "이(가) 희미하게 보입니다.";
        else
            renderedDistance = name + "이(가) 있습니다.";

        return "[" + angle + "에 " + renderedDistance + "]";
    }

    public Position getPosition() { return position; }
    public Entity getEntity() { return entity; }
    public double getVisibility() { return visibility; }
    public String getAngle() { return angle; }
}
