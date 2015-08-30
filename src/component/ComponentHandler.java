package component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.SootClass;
import soot.SootMethod;
import analysis.methodAnalysis.InterMethodAnalysis;

public class ComponentHandler {
	
	SootClass originalClass;
	
	Map<String, Component> map = new HashMap<String, Component>();
	String activity = "";
	
	public ComponentHandler(SootClass sc){
		this.originalClass = sc;
	}
	
	public void build(){
		resolveMap(originalClass);
		resolveActivity();
	}

	private void resolveMap(SootClass originalClass){
		Component com = Component.parseComponent(originalClass);
		String name = originalClass.getName();
		if(Component.Activity.equals(com)){
			map.put(name, com);
		} 
		//һ��fragment���Ա����еĻʹ��
		else if(Component.Fragment.equals(com)){
			map.put(name,com);
			List<SootMethod> methods = originalClass.getMethods();
			for(SootMethod initMethod:methods){
				if(initMethod.getName().equals("<init>")){
					//SootMethod initMethod = defineClass.getMethod("void <init>()");
					List<SootMethod> preMethods = InterMethodAnalysis.getAllPreviousMethods(initMethod);
					for(int i=0;i<preMethods.size();i++){
						SootMethod sm = preMethods.get(i);
						if(sm.getDeclaringClass().equals(initMethod.getDeclaringClass())){
							preMethods.remove(i);
							i--;
						}
					}
					//preMethods.remove(initMethod);
					SootMethod method = resolveComponent(preMethods);
					if(method!=null)
						resolveMap(method.getDeclaringClass());
//					for(SootMethod sm:preMethods){
//						SootClass declaringClass = sm.getDeclaringClass();
//						Component parseCom = Component.parseComponent(declaringClass);
//						//ComponentHandler componentHandler = new ComponentHandler(declaringClass);
//					}
					
//					List<Edge> edges = EntryMethodsAnalysis.getActivityEdges(initMethod);
//					for(Edge e:edges){
//						SootMethod src = e.src();
//						resolveMap(src.getDeclaringClass());
//					}	
				}
			}	
		}
		else if(Component.ViewGroup.equals(com)||
				Component.View.equals(com)){
			map.put(name,com);
			//Set<SootMethod> methods = searchMethodsContainingType(defineClass.getName());
			Set<SootClass> classes = InterMethodAnalysis.searchClassesContainsType(originalClass.getName());
			for(SootClass sClass:classes){
				resolveMap(sClass);
			}
		}
	}
	
	//���ȼ�����methods��sm������ǰ������������Щmethods�������ȼ������������: 
	//1. ����Щmethods��activity�������򷵻�activity�ࡣ2. ������activity����ViewGroup�������򷵻�Fragment�� 3. ������ViewGroup����Fragment���� �Դ����� 
	public SootMethod resolveComponent(List<SootMethod> methods){
		SootMethod activity = null;
		SootMethod fragment = null;
		SootMethod viewGroup = null;
		SootMethod view = null;
		
		boolean activityFlag = false;
		boolean fragmentFlag = false;
		boolean viewGroupFlag = false;
		boolean viewFlag = false;
		for(SootMethod sm:methods){
			SootClass declaringClass = sm.getDeclaringClass();
			Component com = Component.parseComponent(declaringClass);
			if(Component.Activity.equals(com)){
				activity = sm;
				activityFlag = true;
			}
			else if(Component.Fragment.equals(com)){
				fragment = sm;
				fragmentFlag = true;	
			}
			else if(Component.ViewGroup.equals(com)){
				viewGroup = sm;
				viewGroupFlag = true;
			}
			else if(Component.View.equals(com)){
				view = sm;
				viewFlag  = true;
			}
		}
		if(activityFlag)
			return activity;
		else if(viewGroupFlag)
			return viewGroup;
		else if(fragmentFlag)
			return fragment;
		else if(viewFlag)
			return view;
		else return null;
	}
	
	
	private void resolveActivity(){
		for(String s:map.keySet()){
			if(Component.Activity.equals(map.get(s))){
				activity = s;
			}
		}
	}
	
	public Map<String, Component> getMap() {
		return map;
	}
	
	public String getActivity() {
		return activity;
	}
}
