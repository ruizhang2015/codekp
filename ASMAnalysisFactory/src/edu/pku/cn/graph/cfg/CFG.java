/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Liuxizhiyi
 * @time 2008-5-22 ����12:20:29
 * @modifier: Liuxizhiyi
 * @time 2008-5-22 ����12:20:29
 * @reviewer: Liuxizhiyi
 * @time 2008-5-22 ����12:20:29
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.graph.cfg;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.tree.analysis.Frame;

import edu.pku.cn.analysis.common.Subroutine;
import edu.pku.cn.classfile.ClassNode;
import edu.pku.cn.classfile.LineNumberTable;
import edu.pku.cn.classfile.LocalVariableNode;
import edu.pku.cn.classfile.LocalVariableTable;
import edu.pku.cn.classfile.MethodNode;
import edu.pku.cn.graph.BasicGraph;
import edu.pku.cn.graph.visit.DFS;
import edu.pku.cn.graph.visit.GraphVisitor;
import edu.pku.cn.hierarchy.Repository;
import edu.pku.cn.jir.JIRBuilder;
import edu.pku.cn.jir.Stmt;

/**
 * CFG stores owner and subroutines.
 * 
 * @author Liuxizhiyi
 */
public class CFG extends BasicGraph<Edge, BasicBlock> {
	private String owner;
	private MethodNode method; // reference to the methodnode who holds this
	// cfg
	private BasicBlock entry;
	private BasicBlock exit;
	private BasicBlock unhandleException;
	private boolean isDealed = false;
	private Frame[] frames;

	private Subroutine[] subroutines;
	private List<TryCatchBlockNode>[] handlers;

	public LocalVariableTable localVariableTable;
	public LineNumberTable lineNumberTable;

	public ArrayList<BasicBlock> getBlocks() {
		return this.vertexList;
	}

	protected List<Stmt> stmts;

	public List<Stmt> getJIRStmt() {
		if (stmts == null) {
			JIRBuilder builder = new JIRBuilder();
			builder.build(method);
		}
		return stmts;
	}

	public void setJIRStmt(List<Stmt> stmts) {
		this.stmts = stmts;

	}

	public void printStmts() {
		Iterator<Stmt> iter = stmts.iterator();
		int i = 0;
		while (iter.hasNext()) {
			System.out.println(i++ + ": " + iter.next());
		}
		System.err.println("...............................");
	}

	public void printEachBlockStmts() {
		Iterator<BasicBlock> blockIter = this.blockIterator();
		while (blockIter.hasNext()) {
			BasicBlock block = blockIter.next();
			System.out.println("Block" + block.getLabel());
			for (int i = block.startStmt; i <= block.endStmt; i++) {
				System.out.println(this.stmts.get(i));
			}
		}
	}

	public MethodNode getMethod() {
		return method;
	}

	public CFG(MethodNode node) {
		method = node;
		if ((method.access & (Opcodes.ACC_ABSTRACT | Opcodes.ACC_NATIVE)) != 0) {
			frames = new Frame[0];
		} else
			frames = new Frame[method.instructions.size()];
	}

	public CFG(String owner, MethodNode node) {
		this.owner = owner;
		this.method = node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.pku.cn.graph.BasicGraph#allocateEdge(edu.pku.cn.graph.BasicVertex,
	 *      edu.pku.cn.graph.BasicVertex)
	 */
	@Override
	protected Edge allocateEdge(BasicBlock source, BasicBlock target) {
		return new Edge(source, target);
	}

	public String getOwner() {
		return this.owner;
	}

	public void setSubroutines(Subroutine[] subroutines) {
		this.subroutines = subroutines;
	}

	public Subroutine[] getSubroutines() {
		return this.subroutines;
	}

	public Edge createEdge(BasicBlock source, BasicBlock target, int type) {
		Iterator<Edge> iter = Edge.newIterator(source, false);
		boolean exist = false;
		while (iter.hasNext()) {
			Edge edge = iter.next();
			if (edge.getTarget().equals(target) && edge.getType() == type) {
				exist = true;
				break;
			}
		}
		if (!exist) {
			Edge edge = super.createEdge(source, target);
			edge.setType(type);
			return edge;
		}
		return null;
	}

	public Edge getEdge(BasicBlock source, BasicBlock target) {
		Edge edge = null;
		Iterator<Edge> edgeIter = super.edgeIterator();
		while (edgeIter.hasNext()) {
			Edge temp = edgeIter.next();
			if (temp.getSource().equals(source) && temp.getTarget().equals(target))
				edge = temp;
		}
		return edge;
	}

	public BasicBlock createBasicBlock(int start, int end) {
		BasicBlock block = super.createVertex();
		addVertex(block);
		block.setStartInc(start);
		block.setEndInc(end);
		block.frames = this.frames;
		block.insnList = method.instructions;
		return block;
	}

	public BasicBlock breakBlock(BasicBlock block, int jumpTo) {
		int size = jumpTo - block.getStartInc();
		if (size <= 0 || jumpTo >= block.getEndInc())
			return block;
		BasicBlock newBlock = createBasicBlock(jumpTo + 1, block.getEndInc());
		// newBlock.frameList=block.frameList.subList(size,block.frameList.size());
		// newBlock.insnList=block.insnList.subList(size,block.insnList.size());
		// newBlock.setEndInc(block.getEndInc());
		// addVertex(newBlock);
		//    	
		// block.frameList=block.frameList.subList(0,size);
		// block.insnList=block.insnList.subList(0,size);
		block.setEndInc(jumpTo);

		Iterator<Edge> iter = block.outEdgeIterator();
		while (iter.hasNext()) {
			Edge edge = iter.next();
			createEdge(newBlock, edge.getTarget(), edge.getType());
			removeEdge(edge);
		}
		createEdge(block, newBlock, EdgeType.FALL_THROUGH_EDGE);
		return newBlock;
	}

	public BasicBlock changeBlock(int type, BasicBlock block) {
		switch (type) {
		case BlockType.IF_BLOCK:
			return new IfBlock(block);
		case BlockType.SWITCH_BLOCK:
			return new SwitchBlock(block);
		case BlockType.WHILE_BLOCK:
			return new WhileBlock(block);
		case BlockType.DOWHILE_BLOCK:
			return new DoWhileBlock(block);
		case BlockType.RETURN_BLOCK:
			return new ReturnBlock(block);
		case BlockType.EXCEPTION_BLOCK:
		case BlockType.UNHANDLE_EXCEPTION_BLOCK:
			BasicBlock newblock = new ExceptionBlock(block);
			newblock.setType(type);
			return newblock;
		default:
			return block;
		}
	}

	@Override
	public int hashCode() {
		String type = method.name + method.desc;
		return type.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof CFG))
			return false;
		CFG cfg = (CFG) obj;
		if (!method.name.equals(cfg.method.name))
			return false;
		if (!method.desc.equals(cfg.method.desc))
			return false;
		return true;
	}

	/**
	 * @see edu.pku.cn.graph.BasicGraph#allocateVertex()
	 */
	@Override
	protected BasicBlock allocateVertex() {
		return new BasicBlock();
	}

	public void setRoot(BasicBlock root) {
		entry = root;
	}

	public void setHandlers(List<TryCatchBlockNode>[] handlers) {
		this.handlers = handlers;
	}

	public List<TryCatchBlockNode>[] getHandlers() {
		return this.handlers;
	}

	@Override
	public BasicBlock getRoot() {
		return entry;
	}

	public void setExit(BasicBlock exit) {
		this.exit = exit;
	}

	@Override
	public BasicBlock getExit() {
		return exit;
	}

	public void setLocalVariableTable(LocalVariableTable table) {
		localVariableTable = table;
	}

	public LocalVariableNode getLocalVariableNode(int start, int index) {
		return localVariableTable.getNode(start, index);
	}

	public LocalVariableTable getLocalVariableTable() {
		return localVariableTable;
	}

	public void setLineNumberTable(LineNumberTable table) {
		lineNumberTable = table;
	}

	public boolean hasLocalVariable() {
		return localVariableTable != null;
	}

	public int getLine(int index) {
		return lineNumberTable.getLine(index);
	}

	public LineNumberTable getLineNumberTable() {
		return lineNumberTable;
	}

	public boolean hasLineNumber() {
		return lineNumberTable != null;
	}

	// /**
	// * CFG������ȱ���Visitor
	// */
	public class CFGVisitor extends GraphVisitor<CFG, Edge, BasicBlock> {

		// boolean jump=false;

		public void visitCircleEdge(Edge edge) {
			BasicBlock target = edge.getTarget();
			if (target.getType() == BlockType.IF_BLOCK) {
				target = changeBlock(BlockType.WHILE_BLOCK, target);

			} else if (target.getType() == BlockType.NORMAL_BLOCK) {
				target = changeBlock(BlockType.DOWHILE_BLOCK, target);
				if (edge.getSource().getType() == BlockType.DOWHILE_BLOCK) {
					((DoWhileBlock) edge.getSource()).reverse = true;
					((DoWhileBlock) edge.getSource()).addEntryBlock(target);
				}
				// ((WhileBlock)target).addEntryBlock(edge.getSource());
			}
			// if(target.getType()==BlockType.WHILE_BLOCK){
			if (target instanceof WhileBlock) {
				((WhileBlock) target).addEntryBlock(edge.getSource());
			}
			vertexList.set(target.getLabel(), target);
		}
	}

	protected void inline() {
		if (isDealed == false) {
			DFS<CFG, Edge, BasicBlock> dfs = new DFS<CFG, Edge, BasicBlock>(this);
			dfs.accept(new CFGVisitor());
			isDealed = true;
		}
		setLocalVariableAndLine(method);
	}

	protected void setLineTable(final MethodNode m, HashMap<LabelNode, Integer> labelIndex) {
		LineNumberTable lineTable = new LineNumberTable();
		int lastLine = -1;
		int startLine = 0;

		for (int i = 0; i < m.instructions.size(); i++) {
			AbstractInsnNode insnNode = m.instructions.get(i);
			int insnType = insnNode.getType();
			if (insnType == AbstractInsnNode.LABEL) {
				labelIndex.put((LabelNode) insnNode, i);
			} else if (insnType == AbstractInsnNode.LINE) {
				if (lastLine < 0) {
					lastLine = ((LineNumberNode) insnNode).line;
				} else {
					lineTable.addLine(startLine, i, lastLine);
					startLine = i + 1;
					lastLine = ((LineNumberNode) insnNode).line;
				}
			}
		}
		lineTable.addLine(startLine, m.instructions.size(), lastLine);
		setLineNumberTable(lineTable);
	}

	private LabelNode getPreLabel(LabelNode startNode) {
		if (startNode.getPrevious() instanceof VarInsnNode) {
			AbstractInsnNode preInsn = startNode.getPrevious();
			while (preInsn.getType() != AbstractInsnNode.LABEL) {
				preInsn = preInsn.getPrevious();
			}
			startNode = (LabelNode) preInsn;
		}
		return startNode;
	}

	private BasicBlock findBlock(int insn) {
		Iterator<BasicBlock> iter = vertexIterator();
		while (iter.hasNext()) {
			BasicBlock block = iter.next();
			if (block.getStartInc() <= insn && block.getEndInc() >= insn)
				return block;
		}
		return null;
	}

	protected void setLocalVariableAndLine(final MethodNode m) {
		HashMap<LabelNode, Integer> labelIndex = new HashMap<LabelNode, Integer>();
		setLineTable(m, labelIndex);
		// if (m.localVariables != null) {
		// LocalVariableTable table = new LocalVariableTable();
		// for (int i = 0; i < m.localVariables.size();) {
		// LocalVariableNode node = (LocalVariableNode) m.localVariables.get(i);
		// // LabelNode startNode=getPreLabel(node.start);
		// List<LocalVariableNode> sameVar = null;
		// while (table.size() > 0 &&
		// node.name.equals(table.getLastNode().name)) {
		// if
		// (Repository.getInstance().instanceOf(Type.getType(node.desc).getClassName(),
		// "java.lang.Exception") == false) {
		// if (sameVar == null)
		// sameVar = new ArrayList<LocalVariableNode>();
		// sameVar.add(node);
		// if (++i >= m.localVariables.size())
		// break;
		// node = (LocalVariableNode) m.localVariables.get(i);
		// } else
		// break;
		// }
		// if (sameVar != null) {
		// int size = sameVar.size();
		// LocalVariableNode tempNode = table.getLastNode();// it should have
		// one
		// // region here
		// BasicBlock from = findBlock(tempNode.start.index);
		// BasicBlock to = findBlock(sameVar.get(size - 1).start.index);
		// assert (from != null && to != null);
		// boolean isSameVar = false;
		// BitSet visit = new BitSet();
		// visit.set(from.getLabel());
		// while (from != null && from.getStartInc() <= to.getStartInc()) {
		// Iterator<Edge> iter = from.outEdgeIterator();
		// boolean isChanged = false;
		// while (iter.hasNext()) {
		// from = iter.next().getTarget();
		// if (visit.get(from.getLabel()) == false) {
		// isChanged = true;
		// visit.set(from.getLabel());
		// break;
		// }
		// }
		// if (isChanged == false)
		// break;
		// if (from.equals(to)) {
		// isSameVar = true;
		// break;
		// }
		// }
		// if (isSameVar) {
		// for (int j = 0; j < size; j++) {
		// node = sameVar.get(j);
		// LabelNode startNode = node.start;//getPreLabel(node.start);
		// //tempNode.addRegion(startNode.index, node.end.index, node.index);
		// //tempNode.region.add(new Region());
		// }
		// } else {
		// for (int j = 0; j < size; j++) {
		// node = sameVar.get(j);
		// LabelNode startNode = node.start;//getPreLabel(node.start);
		// if (labelIndex.containsKey(node.end))
		// table.addNode(labelIndex.get(startNode), labelIndex.get(node.end),
		// node.index, node);
		// else
		// table.addNode(labelIndex.get(startNode), m.instructions.size(),
		// node.index, node);
		// }
		// }
		// } else {
		// LabelNode startNode = node.start;//getPreLabel(node.start);
		// if (labelIndex.containsKey(node.end))
		// table.addNode(labelIndex.get(startNode), labelIndex.get(node.end),
		// node.index, node);
		// else
		// table.addNode(labelIndex.get(startNode), m.instructions.size(),
		// node.index, node);
		// i++;
		// }
		// }
		// if (m.localVariables.size() > 0 && m.maxLocals >
		// m.localVariables.size()) {// InnerClass
		// // Inner Class Byte Code aload0;aload1; putfield....
		// LocalVariableNode node = (LocalVariableNode) m.localVariables.get(0);
		// AbstractInsnNode insn = node.start;
		// while (insn != null) {
		// if (insn instanceof FieldInsnNode)
		// break;
		// insn = insn.getNext();
		// }
		// if (insn != null) {
		// // throw new RuntimeException("Inner Class Local");
		// // FieldInsnNode field = (FieldInsnNode) insn;
		// // Type type = Type.getType(field.desc);
		// // LocalVariableNode var =new LocalVariableNode(name, desc,
		// signature, start, end, index)
		// // new LocalVariableNode(0, m.instructions.size(), 1, type
		// // .getClassName(), type);
		// // if (table.size() <= 1)
		// // table.addNode(var);
		// // else
		// // table.insertNode(1, var);
		// }
		// }
		// setLocalVariableTable(table);
		// }
	}

	public Frame[] getFrames() {
		return frames;
	}

	/**
	 * @return Returns the unhandleException.
	 */
	public BasicBlock getUnhandleException() {
		return unhandleException;
	}

	/**
	 * @param unhandleException
	 *            The unhandleException to set.
	 */
	public void setUnhandleException(BasicBlock unhandleException) {
		this.unhandleException = unhandleException;
	}

	public Iterator<BasicBlock> blockIterator() {
		return super.vertexIterator();
	}

	public String toString() {
		Iterator<BasicBlock> iter = blockIterator();
		StringBuffer buf = new StringBuffer();
		while (iter.hasNext()) {
			BasicBlock block = iter.next();
			buf.append(block.toString());
			buf.append("\n");
		}
		return buf.toString();
	}

	public void removeInfsbPaths() {

		Iterator<BasicBlock> ite_block = this.blockIterator();
		while (ite_block.hasNext()) {
			BasicBlock bb = ite_block.next();
			ArrayList<Edge> outExEdges = new ArrayList<Edge>();
			Iterator<Edge> ite_edge = bb.outEdgeIterator();
			while (ite_edge.hasNext()) {
				Edge edge = ite_edge.next();
				if (edge.getType() == EdgeType.HANDLED_EXCEPTION_EDGE) {
					outExEdges.add(edge);
				}
			}

			if (outExEdges.size() == 0)
				continue;

			// For blocks who are considered to be able to throw exceptions (in
			// the try block), check the exceptions
			ArrayList<ClassNode> exceptions = bb.getExceptions();
			// System.out.println(bb+" exceptions : "+exceptions);

			if (exceptions.size() == 0 && bb.hasInvokeStmt()) {// this block
				// throw no
				// exceptions,
				// so remove all
				// HANDLED_EXCEPTION_EDGE
				// edges.
				for (int m = 0; m < outExEdges.size(); m++) {
					this.removeEdge(outExEdges.get(m));
					System.out.println("removing edge that throw no exceptions: " + outExEdges.get(m) + "...");
				}
			} else {
				if (!(exceptions.size() > 0 && bb.hasInvokeStmt()))
					continue;

				// for each target block of the HANDLED_EXCEPTION_EDGE edge,
				// check whether they could handle the exceptions thrown from
				// the block, if not, remove it
				Edge finalEdge = null;
				boolean couldBeHandled = false;
				for (int i = 0; i < outExEdges.size(); i++) {
					Edge exEdge = outExEdges.get(i);
					BasicBlock exBb = exEdge.getTarget();

					ClassNode handleEx = exBb.handleException();
					// System.out.println(exBb+" handle exception:"+handleEx);
					if (handleEx != null) {// this exBc is a catch block,
						// handles exceptions of the
						// classType handleEx
						boolean remove = true;
						for (int j = 0; j < exceptions.size(); j++) {
							ClassNode throwEx = exceptions.get(j);
							if (throwEx.instanceOf(handleEx)) {// at least one
								// type of
								// exception
								// thrown from
								// the current
								// block bb
								// could be
								// handled by
								// the catch
								// block
								// System.out.println(throwEx+" is a subClass of
								// "+handleEx);
								remove = false;
								couldBeHandled = true;
							}
						}

						if (remove) {// this catch block couldn't handle any
							// exceptions thrown from the current
							// block bb,
							// so remove the edge between the current block and
							// the catch block
							this.removeEdge(exEdge);
							System.out.println("removing edge: " + exEdge + "...");
						}

					} else {
						finalEdge = exEdge;// this is a edge whose target is a
						// finally block
					}

				}

				// if exceptions thrown from bb could be caught by at least one
				// of the catch block, then remove the edge from bb to a finally
				// block
				if (finalEdge != null && couldBeHandled) {
					this.removeEdge(finalEdge);
					System.out.println("removing a finally edge: " + finalEdge + "...");
				}
			}
		}
	}

	public List<BasicBlock> getABackwardBlockChain(BasicBlock cutpointBlock) {
		List<BasicBlock> backwardBlocks = new ArrayList<BasicBlock>();
		backwardBlocks.add(cutpointBlock);
		if (cutpointBlock.getFirstIncomingEdge() != null && cutpointBlock.getFirstIncomingEdge().getSource() != null) {
			backwardBlocks.addAll(this.getABackwardBlockChain(cutpointBlock.getFirstIncomingEdge().getSource()));
		}
		return backwardBlocks;
	}

	public List<BasicBlock> getABackwardPathWithNoException(BasicBlock cutpointBlock) {
		List<BasicBlock> backwardBlocks = new ArrayList<BasicBlock>();
		backwardBlocks.add(cutpointBlock);
		Iterator<Edge> inEdges = cutpointBlock.inEdgeIterator();
		while (inEdges.hasNext()) {
			Edge inEdge = inEdges.next();
			if (!EdgeType.Type[inEdge.getType()].equals("UNHANDLED_EXCEPTION_EDGE")
					&& !EdgeType.Type[inEdge.getType()].equals("HANDLED_EXCEPTION_EDGE")) {
				backwardBlocks.addAll(this.getABackwardPathWithNoException(inEdge.getSource()));
				break;
			}
		}
		return backwardBlocks;
	}

	public List<BasicBlock> getAForwardPathWithNoException(BasicBlock cutpointBlock) {
		List<BasicBlock> forwardBlocks = new ArrayList<BasicBlock>();

		// cutpointBlock.printBlockFullInfo();

		forwardBlocks.add(cutpointBlock);
		Iterator<Edge> outEdges = cutpointBlock.outEdgeIterator();

		BasicBlock falseBranch = cutpointBlock.getItsFalseBlock();
		BasicBlock trueBranch = cutpointBlock.getItsTrueBlock();

		if (trueBranch != null || falseBranch != null) {
			// an if block
			if (trueBranch != null && falseBranch == null) {
				// ?? choose the true branch
				if (!forwardBlocks.contains(trueBranch)) {
					this.getAForwardPathWithNoException(trueBranch, forwardBlocks);
				} else {
					this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
				}
			} else if (trueBranch != null && falseBranch != null) {
				// an if block
				if (falseBranch.isReturnBlock()) {
					// if the if-false branch is a return block, we should chose
					// a if-true branch
					if (!forwardBlocks.contains(trueBranch)) {
						this.getAForwardPathWithNoException(trueBranch, forwardBlocks);
					} else {
						this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
					}
				} else {
					if (!forwardBlocks.contains(falseBranch)) {
						this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
					} else {
						this.getAForwardPathWithNoException(trueBranch, forwardBlocks);
					}
				}
			} else if (trueBranch == null && falseBranch != null) {
				if (!forwardBlocks.contains(falseBranch)) {
					this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
				}
			}
		} else {
			// a normal block
			while (outEdges.hasNext()) {
				Edge outEdge = outEdges.next();
				if (!EdgeType.Type[outEdge.getType()].equals("UNHANDLED_EXCEPTION_EDGE")
						&& !EdgeType.Type[outEdge.getType()].equals("HANDLED_EXCEPTION_EDGE")
						&& !EdgeType.Type[outEdge.getType()].equals("IFCMP_EDGE_JUMP")) {
					this.getAForwardPathWithNoException(outEdge.getTarget(), forwardBlocks);
					break;
				}
			}
		}
		return forwardBlocks;
	}

	private void getAForwardPathWithNoException(BasicBlock cutpointBlock, List<BasicBlock> forwardBlocks) {
		forwardBlocks.add(cutpointBlock);
		Iterator<Edge> outEdges = cutpointBlock.outEdgeIterator();

		// cutpointBlock.printBlockFullInfo();

		BasicBlock falseBranch = cutpointBlock.getItsFalseBlock();
		BasicBlock trueBranch = cutpointBlock.getItsTrueBlock();

		
		if (trueBranch != null || falseBranch != null) {
			// an if block
			if (trueBranch != null && falseBranch == null) {
				// choose the true branch
				if (!forwardBlocks.contains(trueBranch)) {
					this.getAForwardPathWithNoException(trueBranch, forwardBlocks);
				} else {
					this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
				}
			} else if (trueBranch != null && falseBranch != null) {
				// an if block
				if (falseBranch.isReturnBlock()) {
					// if the if-false branch is a return block, we should chose
					// a if-true branch
					if (!forwardBlocks.contains(trueBranch)) {
						this.getAForwardPathWithNoException(trueBranch, forwardBlocks);
					} else {
						this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
					}
				} else {
					if (!forwardBlocks.contains(falseBranch)) {
						this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
					} else {
						this.getAForwardPathWithNoException(trueBranch, forwardBlocks);
					}
				}
			} else if (trueBranch == null && falseBranch != null) {
				if (!forwardBlocks.contains(falseBranch)) {
					this.getAForwardPathWithNoException(falseBranch, forwardBlocks);
				}
			}
		} else {
			// a normal block
			while (outEdges.hasNext()) {
				Edge outEdge = outEdges.next();
				if (!EdgeType.Type[outEdge.getType()].equals("UNHANDLED_EXCEPTION_EDGE")
						&& !EdgeType.Type[outEdge.getType()].equals("HANDLED_EXCEPTION_EDGE")
						&& !EdgeType.Type[outEdge.getType()].equals("IFCMP_EDGE_JUMP")) {
					this.getAForwardPathWithNoException(outEdge.getTarget(), forwardBlocks);
					break;
				}
			}
		}
	}

	public List<BasicBlock> getAForwardBlockChain(BasicBlock cutpointBlock) {
		List<BasicBlock> forwardBlocks = new ArrayList<BasicBlock>();
		forwardBlocks.add(cutpointBlock);
		if (cutpointBlock.getFirstOutgoingEdge() != null && cutpointBlock.getFirstOutgoingEdge().getTarget() != null) {
			forwardBlocks.addAll(this.getAForwardBlockChain(cutpointBlock.getFirstOutgoingEdge().getTarget()));
		}
		return forwardBlocks;
	}

	public void printEachBlockEdges() {
		System.out.println(this.toString());
	}

	public void printEachBlockEdgesAndStmts() {
		Iterator<BasicBlock> blockIter = this.blockIterator();
		while (blockIter.hasNext()) {
			BasicBlock block = blockIter.next();
			System.out.println("Block" + block.getLabel());

			StringBuffer buf = new StringBuffer();
			buf.append(block.toString());
			buf.append("\n");
			System.out.println(buf);

			for (int i = block.startStmt; i <= block.endStmt; i++) {
				System.out.println("	" + this.stmts.get(i));
			}
		}
	}

	public List<BasicBlock> getABackwardLongestExceptionalPath(BasicBlock cutpointBlock) {
		List<BasicBlock> backwardBlocks = new ArrayList<BasicBlock>();
		backwardBlocks.add(cutpointBlock);
		Iterator<Edge> inEdges = cutpointBlock.inEdgeIterator();

		int maxPreviousExceptionBlockID = -1;
		Edge normalEdge = null;
		Edge maxExceptionEdge = null;

		while (inEdges.hasNext()) {
			Edge inEdge = inEdges.next();
			if (EdgeType.Type[inEdge.getType()].equals("UNHANDLED_EXCEPTION_EDGE")
					|| EdgeType.Type[inEdge.getType()].equals("HANDLED_EXCEPTION_EDGE")) {
				if (inEdge.getSource().getLabel() > maxPreviousExceptionBlockID) {
					maxExceptionEdge = inEdge;
					maxPreviousExceptionBlockID = inEdge.getSource().getLabel();
				}
			} else {
				normalEdge = inEdge;
			}
		}

		if (maxExceptionEdge != null) {
			backwardBlocks.addAll(this.getABackwardLongestExceptionalPath(maxExceptionEdge.getSource()));
		} else if (normalEdge != null) {
			backwardBlocks.addAll(this.getABackwardLongestExceptionalPath(normalEdge.getSource()));
		}

		return backwardBlocks;
	}

}

// end
