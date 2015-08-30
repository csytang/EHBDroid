package analysis.methodAnalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.Local;
import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Sources;
import soot.jimple.toolkits.callgraph.Targets;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;
import tags.MethodTag;
import util.LocalAnalysis;
import entry.Entry;

public class InterMethodAnalysis {
	
	public static CallGraph callGraph = Entry.getCallGraph();

	/**
	 * ʹ��callgraph���sootMethod���������з���
	 * */
	public static List<SootMethod> getTargetsMethods(SootMethod sootMethod){
		List<SootMethod> sMethods = new ArrayList<SootMethod>();
		Iterator<MethodOrMethodContext> targets = new Targets(callGraph.edgesOutOf(sootMethod));
		while(targets.hasNext()){
			SootMethod m = (SootMethod)targets.next();
			sMethods.add(m);
		}
		return sMethods;
	}
	
	/**
	 * ʹ��callgraph��õ���sootMethod�ķ���
	 * */
	public static List<SootMethod> getSourcesMethods(SootMethod sootMethod){
		List<SootMethod> sMethods = new ArrayList<SootMethod>();
		Iterator<MethodOrMethodContext> sources = new Sources(callGraph.edgesInto(sootMethod));
		while(sources.hasNext()){
			SootMethod m = (SootMethod)sources.next();
			sMethods.add(m);
		}
		return sMethods;
	}
	
	public static Iterator<Edge> getTargetEdges(SootMethod sootMethod) {
		return callGraph.edgesOutOf(sootMethod);
	}
	
	public static Iterator<Edge> getSourceEdges(SootMethod sootMethod) {
		return callGraph.edgesInto(sootMethod);
	}
	
	//��÷���sm������ǰ����
	public static List<SootMethod> getAllPreviousMethods(SootMethod sm){
		List<SootMethod> preMethods = new ArrayList<SootMethod>();
		if(!preMethods.contains(sm)){
			preMethods.add(sm);
		}
		for(int i = 0;i<preMethods.size();i++){
			Iterator<MethodOrMethodContext> sources = new Sources(callGraph.edgesInto(preMethods.get(i)));
			//Iterator it = callGraph.edgesInto(m);
			while(sources.hasNext()){
				SootMethod sourceMethod = (SootMethod)sources.next();
				if(sourceMethod.getSignature().startsWith("<android"))
					continue;
				if(!preMethods.contains(sourceMethod)&&!sourceMethod.getName().equals("dummyMainMethod")){
					preMethods.add(sourceMethod);
				}
			}
		}		
		return preMethods;
	}
	
	//�����local�йص����о䡣
	public static List<Stmt> getStmtsWithLocal(Local l,UnitGraph ug){
		List<Stmt> stmts = new ArrayList<Stmt>();
		SootMethod sm = ug.getBody().getMethod();
		MethodTag mt = new MethodTag(sm);
		Iterator<Unit> bUnits = ug.iterator();
		while(bUnits.hasNext()){
			Unit u = bUnits.next();
			List<ValueBox> vbsBoxs = u.getUseAndDefBoxes();
			for(ValueBox vb:vbsBoxs){
				if(vb.getValue() instanceof Local){					
					if(vb.getValue().equals(l)){							
						stmts.add((Stmt)u);	
						u.addTag(mt);
						break;
					}
				}
			}
		}		
		return stmts;
	}
	
	public static Set<SootClass> searchClassesContainsType(String name) {
		Set<SootClass> classes = new HashSet<SootClass>();
		Chain<SootClass> classes2 = Scene.v().getClasses();
		SootClass exceptClass = Scene.v().getSootClass(name);
		List<SootClass> subClasses = new ArrayList<SootClass>();
		if(exceptClass.isAbstract()){
			//List<SootClass> subClasses = Scene.v().getActiveHierarchy().getSubclassesOf(exceptClass);
			subClasses = Scene.v().getActiveHierarchy().getSubclassesOfIncluding(exceptClass);
		}
		classes2.removeAll(subClasses);
		for(SootClass ex:subClasses){
			for(SootClass sc:classes2)
				if(sc.isConcrete())
					for(SootMethod sm:sc.getMethods())
						if(sm.isConcrete()&&!classes.contains(sm)&&LocalAnalysis.containLocal(sm, ex.getName())){
							classes.add(sc);
							break;
						}
		}
		classes2.addAll(subClasses);				
		return classes;
	}
	
//	//��������֮���Ƿ���ڶ�����
//	private boolean hasMutilpleEdges(SootMethod src, SootMethod tgt){
//		return false;
//	}
}
