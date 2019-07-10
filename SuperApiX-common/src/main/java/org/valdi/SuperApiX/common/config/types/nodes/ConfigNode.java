package org.valdi.SuperApiX.common.config.types.nodes;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigNode implements IConfigNode {
    private ConfigurationNode root;

    public ConfigNode(ConfigurationNode node) {
        this.root = node;
    }

    public ConfigNode getParent() {
        ConfigurationNode parent = getNativeNode().getParent();
        if(parent == null) {
            return null;
        }

        return new ConfigNode(parent);
    }

    @Override
    public ConfigurationNode getNativeNode() {
        if (this.root == null) {
            throw new RuntimeException("Config is not loaded.");
        }

        return this.root;
    }

    @Override
    public ConfigNode getNode(String path) {
        if(path == null || path.equals("")) {
            return this;
        }

        ConfigurationNode node = this.getNativeNode(path.split("\\."));
        return new ConfigNode(node);
    }

    @Override
    public Object get(String path) {
        return getFixedNode(path).getValue();
    }

    @Override
    public Object get(String path, Object def) {
        return getFixedNode(path).getValue(def);
    }

    @Override
    public <T> T get(String path, TypeToken<T> type) {
        try {
            return getFixedNode(path).getValue(type);
        } catch (ObjectMappingException e) {
            return null;
        }
    }

    @Override
    public <T> T get(String path, TypeToken<T> type, T def) {
        try {
            return getFixedNode(path).getValue(type, def);
        } catch (ObjectMappingException e) {
            return def;
        }
    }

    @Override
    public String getString(String path) {
        return getFixedNode(path).getString();
    }

    @Override
    public String getString(String path, String def) {
        return getFixedNode(path).getString(def);
    }

    @Override
    public int getInt(String path) {
        return getFixedNode(path).getInt();
    }

    @Override
    public int getInt(String path, int def) {
        return getFixedNode(path).getInt(def);
    }

    @Override
    public long getLong(String path) {
        return getFixedNode(path).getLong();
    }

    @Override
    public long getLong(String path, long def) {
        return getFixedNode(path).getLong(def);
    }

    @Override
    public float getFloat(String path) {
        return getFixedNode(path).getFloat();
    }

    @Override
    public float getFloat(String path, float def) {
        return getFixedNode(path).getFloat(def);
    }

    @Override
    public double getDouble(String path) {
        return getFixedNode(path).getDouble();
    }

    @Override
    public double getDouble(String path, double def) {
        return getFixedNode(path).getDouble(def);
    }

    @Override
    public boolean getBoolean(String path) {
        return getFixedNode(path).getBoolean();
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return getFixedNode(path).getBoolean(def);
    }

    @Override
    public List<?> getList(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

        return (List<?>) node.getValue();
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

        return (List<?>) node.getValue(def);
    }

    @Override
    public <T> List<T> getList(String path, TypeToken<T> type) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return null;
        }

        try {
            return node.getList(type);
        } catch (ObjectMappingException e) {
            return null;
        }
    }

    @Override
    public <T> List<T> getList(String path, TypeToken<T> type, List<T> def) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return def;
        }

        try {
            return node.getList(type, def);
        } catch (ObjectMappingException e) {
            return def;
        }
    }

    @Override
    public List<String> getStringList(String path) {
        return this.getList(path, TypeToken.of(String.class));
    }

    @Override
    public List<String> getStringList(String path, List<String> def) {
        return this.getList(path, TypeToken.of(String.class), def);
    }

    @Override
    public List<Integer> getIntList(String path) {
        return this.getList(path, TypeToken.of(Integer.class));
    }

    @Override
    public List<Integer> getIntList(String path, List<Integer> def) {
        return this.getList(path, TypeToken.of(Integer.class), def);
    }

    @Override
    public List<Long> getLongList(String path) {
        return this.getList(path, TypeToken.of(Long.class));
    }

    @Override
    public List<Long> getLongList(String path, List<Long> def) {
        return this.getList(path, TypeToken.of(Long.class), def);
    }

    @Override
    public List<Float> getFloatList(String path) {
        return this.getList(path, TypeToken.of(Float.class));
    }

    @Override
    public List<Float> getFloatList(String path, List<Float> def) {
        return this.getList(path, TypeToken.of(Float.class), def);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return this.getList(path, TypeToken.of(Double.class));
    }

    @Override
    public List<Double> getDoubleList(String path, List<Double> def) {
        return this.getList(path, TypeToken.of(Double.class), def);
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        return this.getList(path, TypeToken.of(Boolean.class));
    }

    @Override
    public List<Boolean> getBooleanList(String path, List<Boolean> def) {
        return this.getList(path, TypeToken.of(Boolean.class), def);
    }

    @Override
    public void set(String path, Object value) {
        getFixedNode(path).setValue(value);
    }

    @Override
    public <T> void set(String path, TypeToken<T> type, T value) {
        try {
            getFixedNode(path).setValue(type, (T)value);
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getKey() {
        Object key = getNativeNode().getKey();
        if(key == null) {
            return "";
        }
        return key.toString();
    }

    @Override
    public boolean contains(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return false;
        }

        return node.getValue() != null || node.hasListChildren() || node.hasMapChildren();
    }

    @Override
    public boolean isSection(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return false;
        }

        return node.hasMapChildren();
    }

    @Override
    public List<String> getKeys(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return new ArrayList<>();
        }

        return node.getChildrenMap().keySet().stream().map(Object::toString).collect(Collectors.toList());
    }

    @Override
    public Map<String, ? extends ConfigNode> getValues(String path) {
        ConfigurationNode node = getFixedNode(path);
        if (node.isVirtual()) {
            return new HashMap<>();
        }

        HashMap<String, ConfigNode> nodes = new HashMap<>();
        node.getChildrenMap().forEach((key, value) -> nodes.put(key.toString(), new ConfigNode(value)));
        return nodes;
    }

    @Override
    public void removeChild(String path, String child) {
        getFixedNode(path).removeChild(child);
    }

    @Override
    public void clear(String path) {
        for(String node : this.getKeys(path)) {
            removeChild(path, node);
        }
    }

    @Override
    public ConfigNode mergeValuesFrom(IConfigNode other) {
        getNativeNode().mergeValuesFrom(other.getNativeNode());
        return this;
    }
}
