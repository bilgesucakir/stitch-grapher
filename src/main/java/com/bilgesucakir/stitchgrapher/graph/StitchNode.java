package com.bilgesucakir.stitchgrapher.graph;

import com.bilgesucakir.stitchgrapher.stitch.Stitch;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a node in the stitch graph, which corresponds to a single stitch in the crocheting pattern.
 * Each node has a unique ID, a reference to the stitch it represents, and connections to previous and next nodes,
 * as well as parent and child nodes for branching patterns.
 */
@Getter
public class StitchNode {

    private final UUID id;
    private final Stitch stitch;
    private StitchNode previous;
    private StitchNode next;
    private final List<StitchNode> parents;
    private final List<StitchNode> children;

    public StitchNode(Stitch stitch) {
        this.id = UUID.randomUUID();
        this.stitch = stitch;
        this.parents = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public void connectNext(StitchNode next) {
        this.next = next;
        next.previous = this;
    }

    public void addChild(StitchNode child) {
        this.children.add(child);
        child.parents.add(this);
    }

    @Override
    public String toString() {
        return stitch.getType().name()
                + "-"
                + id.toString().substring(0, 5);
    }
}