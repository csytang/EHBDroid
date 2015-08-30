
package com.aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.SootMethod;
import soot.Type;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeStmt;
import soot.toolkits.graph.DirectedGraph;
import tags.FlagTag;
import tags.Tagger;
import vasco.Context;
import vasco.InterProceduralAnalysis;

/**
 * The goal of this class is to analysis the local of method m.
 * Model no_field_anal means if a local points to a field, we ignore it.
 * Model field_anal means if a local points to a field, we analyze it.
 * 
 * ע�⣺��m���й��̼������ǰ����m��callGraph�ɴ�
 * */
public abstract class AEFGInterproceduralAnalysis<M,N,A> extends InterProceduralAnalysis<M,N,A> {
	protected List<M> visitedM = new ArrayList<M>();
	public final static int no_field_anal = 1;
	public final static int field_anal = 2;
	
	List<Type> types;
	int field_anal_or_not = field_anal;
	public AEFGInterproceduralAnalysis(M m,List<Type> types){
		super(false, m);
		this.types = types;
	}
	
	public AEFGInterproceduralAnalysis(M m,List<Type> types, int model){
		super(false, m);
		this.types = types;
		this.field_anal_or_not = model;
	}
	
	public Context<M, N, A> doInterprocedure(M method, A input){
		return doInterprocedure(method, 7, input, null);
	}
	
	/**
	 * ִ�й��̼���������ط���m�е�locals������ֵ
	 * @param method �������ķ���method
	 * @param depth depth��ָ��methodΪ��ǰ�ڵ㣬��ȱ���callgraph��ֱ���������Ϊdepth��
	 * @param input method�������Ϣ����ʼֵΪ�գ������ϱ����Ĺ�������Ϊ�գ����£���ֵ��caller method�ṩ��
	 * @param tillUnit ���ϱ�����Ŀ������cur method�����ֵ�������ֵ����pre method��á��������pre methodʱ��ֻ��ִ�е�srcUnit������ִ�С�
	 * Ŀ�ģ� ��Լʱ��.
	 * @return currentContext ���ص�ǰm�е�localsֵ
	 * m��Context�ǿ�ֵ��
	 * ���������1. m���ɴ� 2. m��û��Ŀ�����ͣ���String�� Uri�ȣ�
	 * 
	 * ˼·��
	 * ��������Ϊ3�ַ���:
	 * 1. undo 2. doing 3. done 
	 * doing ��Ӧ analyzingContext
	 * done ��ӦanalyzedContext
	 * */
	public Context<M, N, A> doInterprocedure(M method, int depth, A input, N tillUnit){
//		List<Context<M, N, A>> list2 = contexts.get(m);
		Context<M,N,A> currentContext = initContext(method, topValue());
		
		if(Tagger.getFlagTag((SootMethod)method)==FlagTag.DOING){
			return currentContext;
		}
		else if(Tagger.getFlagTag((SootMethod)method)==FlagTag.DONE){
			return (Context<M, N, A>)Contexts.v().getAnalyzedContexts().get((SootMethod)method);
		}
		if(depth>0){
			A entry;
			//��ʵ����ͳ���������������δ����������fr()��fr(String)
			if(input==null){
				//���method�Ĳ����Ƿ���types����û�л��߲���Ϊ�գ��򷵻�false���������ϲ���
				if(paramContainsType(method, types)){
					Map<M, List<N>> map = programRepresentation().resolveSources(method);
					Set<M> keySet = map.keySet();
					entry = topValue();
					for(M src:keySet){
						List<N> list = map.get(src);
						for(N node:list){
							Context<M, N, A> doInterprocedure = doInterprocedure(src, depth-1, input, node);
							A topValue = doInterprocedure.getValueBefore(node);
							A flowEntry = compute_Entry_OfMethod(doInterprocedure, method, node, topValue);
							entry = meet(entry, flowEntry);
						}
					}
				}
				else {
					entry = topValue();
				}
			}
			else {
				entry = copy(input);
			}
			
			//System.out.println("Entry initialion finished-----------in AEFGInterproceduralAnalysis Method:"+method);
			//2. ��ʼ��context
			currentContext = initContext(method, entry);
			addToAnalyzingContext(method, currentContext);
			
			DirectedGraph<N> controlFlowGraph = programRepresentation().getControlFlowGraph(method);
			//System.out.println("96: "+controlFlowGraph.size());
			Iterator<N> iterator = controlFlowGraph.iterator();
			while(iterator.hasNext()){
				N node = iterator.next();
				if (node!=null) {
					//3. �̳�ǰ����ֵ
					List<N> predecessors = currentContext.getControlFlowGraph().getPredsOf(node);
					if (predecessors.size() != 0) {
						A in = topValue();					
						for (N pred : predecessors) {
							A predOut = currentContext.getValueAfter(pred);
							in = meet(in, predOut);
						}					
						currentContext.setValueBefore(node, in);
					}
					A in = currentContext.getValueBefore(node);
					A out = topValue();
					
					//4. ��gen(node)
					if(!node.equals(tillUnit)){
						//4.1 ������Ǹ�ֵ���
						if(isDefinitionStmt(node)){
							DefinitionStmt ds = (DefinitionStmt)node;
							//��Ҫ�����������������֣�1.constant 2. types
							if(isAnalyzedType(ds,types)){
								if(isIdentityStmt(node)){
									out = copy(in);
									currentContext.setValueAfter(node, out);
									continue;
								}
								else if(isAssignStmt(node)){
									if (isCall(node)) {
										for (M targetMethod : programRepresentation().resolveTargets(currentContext.getMethod(), node)) {
											if(isApplicationMethod(targetMethod)){
												//System.out.println("application!"+targetMethod+" node: "+node);
												A entryValue = compute_Entry_OfMethod(currentContext, targetMethod, node, in);
												Context<M, N, A> resultContext = doInterprocedure(targetMethod, depth-1, entryValue,null);
												A returnedValue = resultContext.getExitValue();
												out = copy(in);
												out = meet(out, returnedValue);
											}
											else if(isTargetMethod(targetMethod, types)){
												A returnedValue = compute_Def_ByMethod(currentContext,node,in);
												//System.out.println("targetMethod!"+targetMethod+" node: "+node);
												out = meet(out, returnedValue);
											}
											else{
												out = copy(in);
											}
										}
									}
									//����fieldʱ��ִ����һ��
									else out = compute_Def_ByConstant(currentContext, node, in);
								}
								else out = copy(in);
							}
							else out = copy(in);
						}
						//4.2 ������ǵ������
						else if(isInvokeStmt(node)){
							M targetMethod = resolveMethod(node);
							if(isTargetMethod(targetMethod, types)){
								A returnedValue = compute_Def_ByMethod(currentContext,node,in);
								out = meet(out, returnedValue);
							}
							else out = copy(in);
						}
						//4.3 ��������������.
						else out = copy(in);
						currentContext.setValueAfter(node, out);
					}
					//��node�ǽ�ֹ���ʱ����ִֹ��
					else{
						break;
					}
				}
			}
			
			//System.out.println(method+"'s analysis has finished");
			//5. ��β����,��exitValue
			A exitValue = topValue();
			for (N tailNode : currentContext.getControlFlowGraph().getTails()) {
				A tailOut = currentContext.getValueAfter(tailNode);
				exitValue = meet(exitValue, tailOut);
			}
			currentContext.setExitValue(exitValue);
			depth--;
			
			if(tillUnit!=null){
				removeFromAnalyzingContext(method, currentContext);
			}
			else {
				//removeFromAnalyzingContext(method, currentContext);
				addToAnalyzedContext(method, currentContext);
			}
		}
		//depth=0ʱ������ִ��
		else {
			return currentContext;
		}
		return currentContext;
	}

	protected abstract boolean paramContainsType(M method, List<Type> types2) ;
	
	@Override
	public void doAnalysis() {
	}

	/**
	 * Creates a new value context and initialises data flow values for its nodes.
	 * 
	 * <p>
	 * The following steps are performed:
	 * <ol>
	 * <li>Construct the context.</li>
	 * <li>Initialise IN/OUT for all nodes and add them to the work-list</li>
	 * <li>Initialise the IN of entry points with a copy of the given entry value.</li>
	 * <li>Add this new context to the given method's mapping.</li>
	 * <li>Add this context to the global work-list.</li>
	 * </ol>
	 * </p>
	 * 
	 * @param method the method whose context to create
	 * @param entryValue the data flow value at the entry of this method
	 */
	protected Context<M,N,A> initContext(M method, A entryValue) {
		// Construct the context
		Context<M,N,A> context = new Context<M,N,A>(method, programRepresentation().getControlFlowGraph(method), false);

		// Initialise IN/OUT for all nodes and add them to the work-list
		for (N unit : context.getControlFlowGraph()) {
			context.setValueBefore(unit, topValue());
			context.setValueAfter(unit, topValue());
			context.getWorkList().add(unit);
		}

		// Now, initialise the IN of entry points with a copy of the given entry value.
		context.setEntryValue(copy(entryValue));
		for (N unit : context.getControlFlowGraph().getHeads()) {
			context.setValueBefore(unit, copy(entryValue));
		}
		context.setExitValue(topValue());

		// Add this new context to the given method's mapping.
//		if (!analyzedContexts.containsKey(method)) {
//			analyzedContexts.put(method, new LinkedList<Context<M,N,A>>());
//		}
		//analyzedContexts.put(method, context);
		worklist.add(context);
		return context;

	}

	public boolean isIdentityStmt(N node) {
		return node instanceof IdentityStmt;
	}
	
	public boolean isInvokeStmt(N node){
		return node instanceof InvokeStmt;
	}
	
	public boolean isAssignStmt(N node) {
		return node instanceof AssignStmt;
	}
	
	public boolean isDefinitionStmt(N node) {
		return node instanceof DefinitionStmt;
	}
	
	public boolean isCall(N node){
		return ((soot.jimple.Stmt) node).containsInvokeExpr();
	}
	
	protected abstract boolean isAnalyzedType(DefinitionStmt ds, List<Type> types) ;
	
	protected abstract M resolveMethod(N node) ;
	
	public abstract boolean isTargetMethod(M m, List<Type> types);
	
	public abstract boolean isApplicationMethod(M m);
	/**
	 * Processes the intra-procedural flow function of a statement that does 
	 * not contain a method call.
	 * 
	 * @param context       the value context at the call-site
	 * @param node      the statement whose flow function to process
	 * @param inValue   the data flow value before the statement
	 * @return          the data flow value after the statement 
	 */
	public abstract A compute_Def_ByConstant(Context<M,N,A> context, N node, A inValue);
	
	/**
	 * Processes the inter-procedural flow function for a method call at
	 * the start of the call, to handle parameters.
	 * 
	 * @param context       the value context at the call-site
	 * @param targetMethod  the target (or one of the targets) of this call site
	 * @param node          the statement containing the method call
	 * @param inValue       the data flow value before the call
	 * @return              the data flow value at the entry to the called procedure
	 */
	public abstract A compute_Entry_OfMethod(Context<M,N,A> context, M targetMethod, N node, A inValue);

	/**
	 * Processes the inter-procedural flow function for a method call at the
	 * end of the call, to handle return values.
	 * 
	 * @param context       the value context at the call-site
	 * @param targetMethod  the target (or one of the targets) of this call site
	 * @param node          the statement containing the method call
	 * @param exitValue     the data flow value at the exit of the called procedure
	 * @return              the data flow value after the call (returned component)
	 */
//	public abstract A callExitFlowFunction(Context<M,N,A> context, M targetMethod, N node, A exitValue);
//	
//	public abstract A callLocalFlowFunction(Context<M,N,A> context, N node, A inValue);
	
	protected abstract A compute_Def_ByMethod(Context<M, N, A> currentContext, N node, A in) ;
	protected abstract void addTag(M m, FlagTag flagTag);
	protected abstract void removeTag(M m, String tag);
	
	protected abstract void addToAnalyzingContext(M m, Context<M, N, A> currentContext);
//	{
//		addTag(m, new FlagTag(FlagTag.DOING));
//		analyzingContexts.put(m, currentContext);
//	}
	
	protected abstract void removeFromAnalyzingContext(M m, Context<M, N, A> currentContext);
//	{
//		removeTag(m, "FlagTag");
//		analyzingContexts.remove(m);
//	}
	
	protected abstract void addToAnalyzedContext(M m, Context<M, N, A> currentContext);
//	{
//		addTag(m, new FlagTag(FlagTag.DONE));
//		analyzedContexts.put(m, currentContext);
//	}
}
