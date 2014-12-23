/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-26 ����02:18:59
 * @modifier: Administrator
 * @time 2009-3-26 ����02:18:59
 * @reviewer: Administrator
 * @time 2009-3-26 ����02:18:59
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ReachingDefinitionFact {

	public Map<Integer, BitSet> lvDefMap = new HashMap<Integer, BitSet>();
	int timestamp = -1;

	public ReachingDefinitionFact() {

	}

	public ReachingDefinitionFact(ReachingDefinitionFact fact) {
		this();
		Set<Entry<Integer, BitSet>> entries = fact.lvDefMap.entrySet();
		for (Entry<Integer, BitSet> entry : entries) {
			BitSet value = entry.getValue();
			BitSet copy = new BitSet();
			copy.or(value);
			this.lvDefMap.put(entry.getKey(), copy);
		}
		// this.lvDefMap.putAll(fact.lvDefMap);
		this.timestamp = fact.timestamp;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ReachingDefinitionFact))
			return false;
		ReachingDefinitionFact rdFact = (ReachingDefinitionFact) obj;
		if (!this.lvDefMap.equals(rdFact.lvDefMap))
			return false;
		if (timestamp != rdFact.timestamp)
			return false;
		return true;
	}

	public int getLastTimestamp() {
		return this.timestamp;
	}

	public int hashCode() {
		return this.lvDefMap.hashCode() * 10 + this.timestamp;
	}

	public void orOp(ReachingDefinitionFact fact) {
		Set<Entry<Integer, BitSet>> entries = fact.lvDefMap.entrySet();
		for (Entry<Integer, BitSet> entry : entries) {
			if (this.lvDefMap.get(entry.getKey()) == null) { // this map does
				// not contain the
				// element
				this.lvDefMap.put(entry.getKey(), entry.getValue());
			} else {
				BitSet thisBitSet = this.lvDefMap.get(entry.getKey());
				BitSet otherBitSet = fact.lvDefMap.get(entry.getKey());
				thisBitSet.or(otherBitSet);
			}
		}
	}

	public void put(int index, BitSet bitSet) {
		lvDefMap.put(index, bitSet);
	}

	public void setLastTimestamp(int ts) {
		this.timestamp = ts;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Set<Entry<Integer, BitSet>> entrySet = this.lvDefMap.entrySet();
		for (Entry<Integer, BitSet> entry : entrySet) {
			buf.append(entry.getKey() + entry.getValue().toString() + ",  ");
		}
		buf.append(this.timestamp + "\n");
		return buf.toString();
	}
}

// end
