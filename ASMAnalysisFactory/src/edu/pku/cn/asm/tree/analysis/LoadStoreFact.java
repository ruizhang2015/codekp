/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2009-3-19 ����11:16:50
 * @modifier: Administrator
 * @time 2009-3-19 ����11:16:50
 * @reviewer: Administrator
 * @time 2009-3-19 ����11:16:50
 * (C) Copyright PKU Software Lab. 2009
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.asm.tree.analysis;

import java.util.BitSet;

public class LoadStoreFact implements Cloneable {

	public BitSet bitSet;
	int timestamp;

	public LoadStoreFact(int size) {
		bitSet = new BitSet(size);
		timestamp = -1;
	}

	public LoadStoreFact() {
		bitSet = new BitSet();
		timestamp = -1;
	}

	public void setLastUpdateTimestamp(int ts) {
		this.timestamp = ts;
	}

	public int getLastUpdateTimestamp() {
		return this.timestamp;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof LoadStoreFact))
			return false;
		LoadStoreFact lvFact = (LoadStoreFact) obj;
		if (!this.bitSet.equals(lvFact.bitSet))
			return false;
		if (timestamp != lvFact.timestamp)
			return false;
		return true;
	}

	public int hashCode() {
		return bitSet.hashCode() * 10 + timestamp;
	}

	public String toString() {
		return bitSet.toString() + "  timestamp = " + this.timestamp;
	}

}

// end
