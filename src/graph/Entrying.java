package graph;


import graphTemp.TempEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import soot.Scene;
import soot.SootClass;

/**
 * ��InvokeStmtStructure��ڻ��������ǻ�Ľڵ�ת���ɻ�ڵ㡣 �����ڵ�ĸ���Ϊ�ǻ�ڵ㣬������ת�������ࡣ
 * */
public class Entrying {
	
	/**
	 *ת�����裺
	 * 1. �������е���ʵ���
	 * 2. ��gss������
	 * 3. ��gss��������������
	 * 4. ��������������ʵ������临��һ����isss��
	 * @return 
	 * */
	public static List<TempEdge> transform(List<TempEdge> outTempEdges,Set<String> activityClasses){
		List<TempEdge> inTempEdges = new ArrayList<TempEdge>();
		for(int i = 0;i<outTempEdges.size();i++){
			//SootMethod sm = gss.get(i).getBodyMethod();
			String sourceNode = outTempEdges.get(i).getSourceNode();
			SootClass sc = Scene.v().getSootClass(sourceNode);
			List<SootClass> subClasses = new ArrayList<SootClass>();
			if(Scene.v().getActiveHierarchy().getSubclassesOf(sc).size()>0){
				subClasses = Scene.v().getActiveHierarchy().getSubclassesOf(sc);
			}
			boolean flag = false;
			for(SootClass s:subClasses){
//				List<String> strings = getMethods(s.getMethods());
//				if(activityClasses.contains(s.getName())&&!strings.contains(sm.getSubSignature())){
//					GraphStorage is = gss.get(i).clone();
//					is.setClassName(s);
//					if(!isss.contains(is))
//						isss.add(is);
//					//flag = true;
//				}
				
				//�޸ġ������жϵ�ǰ��s�����Ƿ���sm��֮ǰ�Ŀ������ڣ���������д����ķ�����ô�죬���ڲ�����
				if(activityClasses.contains(s.getName())){
					TempEdge is = outTempEdges.get(i).clone();
					is.setSourceNode(s.getName());
					if(!inTempEdges.contains(is))
						inTempEdges.add(is);
				}
			}	
			if(!activityClasses.contains(sc.getName())){			
				flag = true;
			}
			if(flag){
				outTempEdges.remove(i);
				i--;
				continue;
			}
		}
		outTempEdges.addAll(inTempEdges);	
		return outTempEdges;
	}
	
//	public static List<String> getMethods(List<SootMethod> sms){
//		List<String> list = new ArrayList<String>();
//		for(SootMethod sm:sms){
//			list.add(sm.getSubSignature());
//		}
//		return list;
//	}
}
