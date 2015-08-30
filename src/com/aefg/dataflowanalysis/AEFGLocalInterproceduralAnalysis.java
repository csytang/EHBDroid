package com.aefg.dataflowanalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.JInvokeStmt;
import tags.FlagTag;
import util.ArrayUtil;
import vasco.Context;
import analysis.methodAnalysis.IntraMethodAnalysis;

public class AEFGLocalInterproceduralAnalysis extends AEFGInterAnalysis<Map<Local, List<Value>>>{
	
	public AEFGLocalInterproceduralAnalysis(SootMethod m, List<Type> types) {
		super(m, types);
	}

	public AEFGLocalInterproceduralAnalysis(SootMethod m, List<Type> types, int model) {
		super(m, types, model);
	}
	
	//����local lhs��ֵ
	private void assign(Local lhs, Value rhs,
			Map<Local, List<Value>> input, Map<Local, List<Value>> output) {
		if (rhs instanceof CastExpr) {
			rhs = ((CastExpr) rhs).getOp();
		}
		if (rhs instanceof Constant) {
			output.put(lhs, ArrayUtil.toList(rhs));
		} 
		else if (rhs instanceof Local) {
			if(input.containsKey(rhs)) {
				output.put(lhs, input.get(rhs));
			}
		}
		//��localָ��Fieldʱ����Field��ֵ��ֵ��local��
		else if(rhs instanceof FieldRef){
			if(field_anal_or_not!=no_field_anal){
				updateToLocal(lhs, rhs, output);
			}
			if(output.get(lhs)==null){
				output.put(lhs, ArrayUtil.toList(rhs));
			}
		}
		else {
			output.put(lhs, new ArrayList<Value>());
		}			
	}

	/**��FieldRefָ��ֵ�����¸�Local lhs
	 * @param lhs �����µ�Local
	 * @param rhs lhsָ���field
	 * @param out ��д��map
	 * */
	private void updateToLocal(Local lhs, Value rhs,
			Map<Local, List<Value>> output) {
		FieldRef fr = (FieldRef)rhs;
		Map<FieldRef, List<Value>> map = parseField(fr);
		//System.out.println("fr���ڵ����ǣ� "+fr.getField().getDeclaringClass()+" Field is: "+fr.getField());
		for(FieldRef f:map.keySet()){
			if(f.getField().equals(fr.getField())){
				List<Value> list = map.get(f);
				//����ִ�д���
				if(list!=null&&list.size()>0){
					if(output.get(lhs)!=null)
						output.get(lhs).addAll(list);
					else 
						output.put(lhs, list);
				}
			}
		}
	}
	
	/**
	 * ����field˼·�����ҵ�field�Ķ��巽����������fieldָ���ֵ��
	 * 
	 * ���裺
	 * 1 �õ�fr���ڵ����methods��
	 * 
	 * 2 ������method����field��������ֵʱ�����unit��Map��
	 * 
	 * 3 �õ�Map<SootMethod, List<Unit>>��������map��
	 * 
	 * 4 ʹ��AEFGFieldAnalysis ���fieldֵ��
	 * 
	 * 
	 * ʹ��map��ԭ���ǣ� ��fr���ڶ�������ڶ���
	 * */
	public Map<FieldRef, List<Value>> parseField(FieldRef fr){
		Map<FieldRef, List<Value>> output = new HashMap<FieldRef, List<Value>>();
		List<SootMethod> methods = fr.getField().getDeclaringClass().getMethods();
		for(SootMethod sm:methods){
			if(sm.isConcrete()){
				IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
				List<AssignStmt> assignStmts = ma.getAssignStmts();			
				for(AssignStmt s:assignStmts){	
					if(s.getLeftOp() instanceof FieldRef){
						FieldRef frRef = (FieldRef)s.getLeftOp();
						if(frRef.getField().equals(fr.getField())){
							AEFGFieldAnalysis analysis = new AEFGFieldAnalysis(sm, ArrayUtil.toList(fr.getField().getType()));
							analysis.assign(fr, s.getRightOp(), null, output, s);
						}
					}
				}
			}
		}
		return output;
	}
	
	/**
	 * �򵥷�������������һ���򵥵ĸ�ֵ���ʱ
	 * */
	@Override
	public Map<Local, List<Value>> compute_Def_ByConstant(
			Context<SootMethod, Unit, Map<Local, List<Value>>> context,
			Unit unit, Map<Local, List<Value>> inValue) {
		// Initialize result to input
		Map<Local, List<Value>> outValue = copy(inValue);
		// Only statements assigning locals matter
		if (unit instanceof AssignStmt) {
			// Get operands
			Value lhsOp = ((AssignStmt) unit).getLeftOp();
			Value rhsOp = ((AssignStmt) unit).getRightOp();
			if (lhsOp instanceof Local) {
				assign((Local) lhsOp, rhsOp, inValue, outValue);		
			}
		}
		return outValue;
	}
	
	/**
	 * ���㷽��targetMethod�����ֵ��
	 * ����ǣ���calleeMethod������ڳ�ʼ����
	 * */
	@Override
	public Map<Local, List<Value>> compute_Entry_OfMethod(
			Context<SootMethod, Unit, Map<Local, List<Value>>> context,
			SootMethod calledMethod, Unit unit, Map<Local, List<Value>> inValue) {
		// Initialise result to empty map
		Map<Local, List<Value>> entryValue = topValue();
		// Map arguments to parameters
		InvokeExpr ie = ((Stmt) unit).getInvokeExpr();
		for (int i = 0; i < ie.getArgCount(); i++) {
			Value arg = ie.getArg(i);
			if(calledMethod.getDeclaringClass().isPhantomClass())
				break;
			Local param = calledMethod.retrieveActiveBody().getParameterLocal(i);
			assign(param, arg, inValue, entryValue);
		}
		// And instance of the this local
		if (ie instanceof InstanceInvokeExpr) {
			Value instance = ((InstanceInvokeExpr) ie).getBase();
			
			if(!calledMethod.getDeclaringClass().isPhantomClass()){
				Local thisLocal = calledMethod.retrieveActiveBody().getThisLocal();
				assign(thisLocal, instance, inValue, entryValue);
			}
		}
		// Return the entry value at the called method
		return entryValue;
	}


	/**
	 * ���ø÷�����������node����isTargetMethod����
	 * */
	@Override
	protected Map<Local, List<Value>> compute_Def_ByMethod(
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext,
			Unit node, Map<Local, List<Value>> in) {
		Map<Local, List<Value>> afterCallValue = copy(in);
	
		if(isAssignStmt(node)){
			AssignStmt as = (AssignStmt)node;
			Local l =(Local) as.getLeftOp();
			Value rightOp = as.getRightOp();
			if(afterCallValue.get(l)==null){
				List<Value> list = new ArrayList<Value>();
				list.add(rightOp);
				afterCallValue.put(l, list);
			}
			else afterCallValue.get(l).add(rightOp);
		}
		else if(isInvokeStmt(node)){
			JInvokeStmt is = (JInvokeStmt)node;
			InstanceInvokeExpr iie = (InstanceInvokeExpr)is.getInvokeExpr();
			Value value = is.getInvokeExpr();
			Local l =(Local) iie.getBase();
			//Local l = (Local)((ValueBox)is.getUseBoxes().get(0)).getValue();;
			if(afterCallValue.get(l)==null){
				List<Value> list = new ArrayList<Value>();
				list.add(value);
				afterCallValue.put(l, list);
			}
			else {
				afterCallValue.get(l).add(value);
			}
		}
		return afterCallValue;
	}

	//�����������setClassName($r1,$r2) $r2��һ��������
	//���ڽ���Intentʱ������Ϊ������䣬��Ҫ��$r2����Ϣ��ӳ����
//	public void getLocalOfParam(Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext,
//			Unit node,Value v){
//		//TODO 
// 		if(v instanceof InvokeExpr){
//			List<Value> args = ((InvokeExpr) v).getArgs();
//			for(Value param:args){
//				if(param instanceof StringConstant){
//					List<Value> list = currentContext.getValueBefore(node).get(param);
//				}
//			}
//		}
//	}
	
	@Override
	public Map<Local, List<Value>> copy(Map<Local, List<Value>> src) {
		return new HashMap<Local, List<Value>>(src);
	}

	/**
	 * ȡ��
	 * */
	@Override
	public Map<Local, List<Value>> meet(Map<Local, List<Value>> op1,
			Map<Local, List<Value>> op2) {
		Map<Local, List<Value>> result = new HashMap<Local, List<Value>>(op1);
		// Then add everything in the second operand, bottoming out the common keys with different values
		for (Local x : op2.keySet()) {
			if (op1.containsKey(x)) {
				// Check the values in both operands
				List<Value> c1 = op1.get(x);
				List<Value> c2 = op2.get(x);
				if (c1 != null && c1.equals(c2) == false) {
					// Set to non-constant
					if(c2!=null){
						c1.addAll(c2);
						result.put(x, c1);
					}
				}
			} else {
				// Only in second operand, so add as-is
				result.put(x, op2.get(x));
			}
		}
		return result;
	}

	@Override
	public  Map<Local, List<Value>> topValue() {
		return new HashMap<Local, List<Value>>();
	}

	@Override
	protected void addToAnalyzingContext(SootMethod m,
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext) {
		addTag(m, new FlagTag(FlagTag.DOING));
		analyzingContexts.put(m, currentContext);
		Contexts.v().getAnalyzingContexts().put(m, currentContext);
	}

	@Override
	protected void removeFromAnalyzingContext(SootMethod m,
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext) {
		removeTag(m, "FlagTag");
		analyzingContexts.remove(m);
		Contexts.v().getAnalyzingContexts().remove(m);
	}

	@Override
	protected void addToAnalyzedContext(SootMethod m,
			Context<SootMethod, Unit, Map<Local, List<Value>>> currentContext) {
		removeFromAnalyzingContext(m, currentContext);
		addTag(m, new FlagTag(FlagTag.DONE));
		analyzedContexts.put(m, currentContext);
		Contexts.v().getAnalyzedContexts().put(m, currentContext);
	}

}
