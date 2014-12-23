/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-2-13 ����11:38:22
 * @modifier: Administrator
 * @time 2009-2-13 ����11:38:22
 * @reviewer: Administrator
 * @time 2009-2-13 ����11:38:22
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

public final class LockMap {

	private int lastUpdateTimestamp = -1;

	private Map<Object, Integer> map;

	public Map<Integer, Object> intToObject;

	public LockMap() {
		this.map = new HashMap<Object, Integer>();
		this.intToObject = new HashMap<Integer, Object>();
	}

	public LockMap(LockMap obj) {
		this();
		this.map.putAll(obj.map);
		this.lastUpdateTimestamp = obj.lastUpdateTimestamp;
		this.intToObject.putAll(obj.intToObject);
	}

	public int getLockCount(Object node) {
		Integer keyInt = findKey(node);
		if (keyInt == null) {
			keyInt = Integer.valueOf(0);
			map.put(node, keyInt);
		}
		return keyInt.intValue();
	}

	public int getLockSum() {
		int counter = 0;
		Set keys = map.keySet();
		for (Object key : keys) {
			counter += map.get(key);
		}
		return counter;
	}

	private Integer findKey(Object node) {
		Integer keyInt = null;
		FieldInsnNode fInsn = null;
		LocalVariableNode lvNode = null;
		Set keys = map.keySet();
		if (node instanceof Integer) {
			keyInt = map.get(node);
		} else if (node instanceof FieldInsnNode) {
			fInsn = (FieldInsnNode) node;
		} else if (node instanceof LocalVariableNode) {
			lvNode = (LocalVariableNode) node;
		}
		if (fInsn != null) {
			boolean flag = false;
			for (Object key : keys) {
				if (key instanceof FieldInsnNode) {
					FieldInsnNode temp = (FieldInsnNode) key;
					if (temp.desc.equals(fInsn.desc) && temp.name.equals(fInsn.name) && temp.owner.equals(fInsn.owner)) {
						keyInt = map.get(key);
						flag = true;
						break;
					}
				}
			}
			if (!flag) {
				for (Entry<Integer, Object> entry : intToObject.entrySet()) {
					if (entry.getValue() instanceof FieldInsnNode) {
						FieldInsnNode temp = (FieldInsnNode) (entry.getValue());
						if (temp.desc.equals(fInsn.desc) && temp.name.equals(fInsn.name)
								&& temp.owner.equals(fInsn.owner)) {
							keyInt = map.get(entry.getKey());
							flag = true;
							break;
						}
					}
				}
			}
		} else if (lvNode != null) {
			boolean flag = false;
			for (Object key : keys) {
				if (key instanceof LocalVariableNode) {
					LocalVariableNode temp = (LocalVariableNode) key;
					if (temp.desc.equals(lvNode.desc) && temp.end.equals(lvNode.end) && temp.index == lvNode.index
							&& temp.name.equals(lvNode.name) && temp.start == lvNode.start) {
						keyInt = map.get(key);
						flag = true;
						break;
					}
				}
			}
			if (!flag) {
				for (Entry<Integer, Object> entry : intToObject.entrySet()) {
					if (entry.getValue() instanceof LocalVariableNode) {
						LocalVariableNode temp = (LocalVariableNode) (entry.getValue());
						if (temp.desc.equals(lvNode.desc) && temp.end.equals(lvNode.end) && temp.index == lvNode.index
								&& temp.name.equals(lvNode.name) && temp.start == lvNode.start) {
							keyInt = map.get(entry.getKey());
							flag = true;
							break;
						}
					}
				}
			}
		}
		return keyInt;
	}

	public void setLockCount(Object node, int lockCount) {
		map.put(node, lockCount);
	}

	public int size() {
		return map.size();
	}

	public void put(Object target, int count) {
		map.put(target, count);
	}

	public void remove(Object target) {
		map.remove(target);
	}

	// To-do more things
	public boolean merge(LockMap pred) {
		boolean changes = false;
		Set<Object> keys = pred.map.keySet();
		for (Object key : keys) {
			if (!this.containsKey(key) || this.getLockCount(key) < pred.getLockCount(key)) {
				this.map.put(key, pred.getLockCount(key));
				if (key instanceof Integer) {
					this.intToObject.put((Integer) key, pred.intToObject.get(key));
				}
				changes = true;
			}
		}
		return changes;
	}

	// To-do more things
	public boolean merge(final LockMap map, final boolean[] access) {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		LockMap other = (LockMap) obj;
		if (this.size() != other.size())
			return false;
		Set<Object> keys = map.keySet();
		for (Object key : keys) {
			if (other.containsKey(key)) {
				if (!map.get(key).equals(other.getLockCount(key)))
					return false;
			}
		}
		if (this.lastUpdateTimestamp != other.lastUpdateTimestamp)
			return false;
		return true;
	}

	public boolean containsKey(Object obj) {
		Integer keyInt = findKey(obj);
		return keyInt != null;
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		Set keys = map.keySet();
		for (Object key : keys) {
			if (buf.length() > 0) {
				buf.append(',');
			}
			if (map.get(key) > 0) {
				buf.append(key + " has " + map.get(key) + " lock(s)");
			}
		}
		buf.append(" lastUpdateTimestamp = " + this.lastUpdateTimestamp);
		return buf.toString();
	}

	public void setLastUpdateTimestamp(int ts) {
		this.lastUpdateTimestamp = ts;
	}

	public int getLastUpdateTimestamp() {
		return this.lastUpdateTimestamp;
	}
}

// end
