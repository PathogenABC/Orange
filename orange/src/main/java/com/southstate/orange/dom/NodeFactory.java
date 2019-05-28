package com.southstate.orange.dom;

import android.util.Log;

import com.eclipsesource.v8.V8Object;
import com.southstate.orange.Orange;
import com.southstate.orange.context.PageContext;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public class NodeFactory {

    private static final String KEY_UNIQUE_REF = "___unique_ref_";

    private static final ThreadLocal<HashMap<String, Node<?>>> sThreadLocalNodeMap = new ThreadLocal<>();   // TODO: 2019/5/9 需要优化
    private static final ThreadLocal<Integer> sNextNodeUniqueRefIndex = new ThreadLocal<>();

    private static String generateNodeRef() {
        Integer index = sNextNodeUniqueRefIndex.get();
        index = index == null ? 0 : index + 1;
        sNextNodeUniqueRefIndex.set(index);
        return "js-node-" + (index);
    }

    private static HashMap<String, Node<?>> getLocalNodeMap() {
        HashMap<String, Node<?>> map = sThreadLocalNodeMap.get();
        if (map == null) {
            map = new HashMap<String, Node<?>>() {
                @Override
                public Node<?> put(String key, Node<?> value) {
                    Node<?> old = super.put(key, value);
                    Log.d("NodeFactory", "all node count = " + size());
                    return old;
                }
            };
            sThreadLocalNodeMap.set(map);
        }
        return map;
    }

    public static V8Object createNode(PageContext pc, String tag) {
        NodeCreator nodeCreator = Orange.getInstance().getNodeCreator(tag);
        if (nodeCreator == null) {
            throw new IllegalArgumentException("未知的标签[" + tag + "]");
        }
        String uniqueRef = generateNodeRef();

        V8Object jsNode = new V8Object(pc.getV8());
        jsNode.add(KEY_UNIQUE_REF, uniqueRef);

        Node<?> node = nodeCreator.createNode(pc, jsNode);
        node.mUniqueRef = uniqueRef;

        getLocalNodeMap().put(uniqueRef, node);
        return jsNode;
    }

    public static Node<?> getNode(V8Object jsNode) {
        String ref = jsNode.getString(KEY_UNIQUE_REF);
        Node<?> node = getLocalNodeMap().get(ref);
        return Objects.requireNonNull(node);
    }

    static void freeNode(Node<?> node) {
        getLocalNodeMap().remove(node.mUniqueRef);
    }

    public static void release() {
        int count = 0;
        HashMap<String, Node<?>> localNodeMap = getLocalNodeMap();
        for (Node<?> nodeWr : localNodeMap.values()) {
            if (nodeWr != null) {
                count++;
                nodeWr.mJsNode.close();
            }
        }
        localNodeMap.clear();

        Log.d("NodeFactory", "release node: " + count);
    }

    public interface NodeCreator {
        Node<?> createNode(PageContext pc, V8Object jsNode);
    }
}
