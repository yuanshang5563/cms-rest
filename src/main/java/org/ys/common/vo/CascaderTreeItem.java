package org.ys.common.vo;

import java.util.List;

public class CascaderTreeItem {
    private String id;
    private String parentId;
    private String name;
    private List<CascaderTreeItem> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CascaderTreeItem> getChildren() {
        return children;
    }

    public void setChildren(List<CascaderTreeItem> children) {
        this.children = children;
    }
}
