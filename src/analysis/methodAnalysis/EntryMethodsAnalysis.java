package analysis.methodAnalysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InvokeStmt;
import soot.jimple.toolkits.callgraph.Edge;
import entry.Entry;
import entryPointCreator.AndroidEntryPointConstants;

//������ڷ�����1. entryPoints 2. callbacks

public class EntryMethodsAnalysis extends InterMethodAnalysis{
	
	//��sm����ڷ�����
	public static List<Edge> getEntryEdges(SootMethod sm){
		//TODO
		//��EM��TM����¼���е�������Ϣ��
		List<Edge> edges = new ArrayList<Edge>();
		List<SootMethod> preMethods = new ArrayList<SootMethod>();
		if(!preMethods.contains(sm)){
			preMethods.add(sm);
			//�ж�sm����ڷ���
			if(!isEntryMethod(sm)){											
				for(int i = 0;i<preMethods.size();i++){
					Iterator<Edge> it = callGraph.edgesInto(preMethods.get(i));
					while(it.hasNext()){
						Edge e = it.next();
						SootMethod src = e.src();
						if(!src.equals(e.tgt())){
							if(!src.getName().equals("dummyMainMethod")){
								//�ų�android�ķ���
								if(!src.getSignature().startsWith("<android")){
									if(!preMethods.contains(src)){
										//��sourceMethod����ڷ���ʱ������ͣ����
										if(isEntryMethod(src))
											edges.add(e);
										else
											preMethods.add(src);	
										
									}
									else if(preMethods.contains(src)&&!edges.contains(e)) {
										if(isEntryMethod(src)){
											edges.add(e);
										}	
									}
								}
							}
						}
					}
				}		
			}
		}
//		else {
//			Edge e = new Edge(sm, null, sm);
//			edges.add(e);
//		}
		return edges;
	}	
	
//	//����sm��ֱ���ﵽ���deep
//	public static Set<Edge> searchEdges(SootMethod sm, int deep){
//		Set<Edge> edges = new HashSet<Edge>();
//		Iterator<Edge> edgesInto = callGraph.edgesInto(sm);
//		
//		Set<Edge> intraEdges = new HashSet<Edge>(); 
//		
//		if(deep-->0){
//			while(edgesInto.hasNext()){
//				edges.add(edgesInto.next());
//				intraEdges.add(edgesInto.next());
//			}
//			for(Edge e:intraEdges){
//				Set<Edge> searchEdges = searchEdges(e.src(), deep);
//				edges.addAll(searchEdges);
//			}
//		}
//		return edges;
//	}
	
	//�ж�sm�Ƿ�����ڷ���
	public static boolean isEntryMethod(SootMethod sm){
		return isEntry(sm, Entry.entrypoints);
	}
	//sm��Activity���������ڷ���
	public static boolean isActivityMethod(SootMethod sm){
		return isEntry(sm, Entry.fakeActivities);
	}
	
	private static boolean isEntry(SootMethod sm, Set<String> set) {
		String signature = sm.getSignature();
		String subSignature = sm.getSubSignature();	
		//if(signature.startsWith("<android")){
			if(subSignature.contains(AndroidEntryPointConstants.BROADCAST_ONRECEIVE)
				||subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONRESUME)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONCREATE)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONSTART)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICK)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONMENUITEMSELECTED)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICKDIALOG)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONITEMCLICK)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONLISTITEMCLICK)||
				subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONOPTIONSITEMSELECTED)
				||subSignature.contains(AndroidEntryPointConstants.ACTIVITY_ONMENUITEMSELECTED)){
				
				//���ҽ���sm��ǰ��ΪdummyMainMethodʱ��sm����ں�����
//				boolean flag = false;
//				List<SootMethod> sourcesMethods = InterMethodAnalysis.getSourcesMethods(sm);
//				if(sourcesMethods.size()==1){
//					if(sourcesMethods.get(0).getName().contains("dummyMainMethod")){
//						flag = true;
//					}
//				}
				
//				for(SootMethod source:InterMethodAnalysis.getSourcesMethods(sm))
//				{
//					if(source.getName().contains("dummyMainMethod")){
//						flag = true;
//					}
//				}
//				if(flag){
					SootClass sc = sm.getDeclaringClass();
					if(set.contains(sc.getName())){
						return true;
					}
				}
			//}
		//}
		//else if(signature.startsWith("<java.lang"))
			if(subSignature.contains("void run()"))
				return true;
		return false;
	}
	
	//��startActivity����Ӧ�ı�
	public static Edge getStartActivityEdge(SootMethod sm,InvokeStmt startActivity){	
		Iterator<Edge> ii = callGraph.edgesOutOf(sm);
		while(ii.hasNext()){
			Edge e = ii.next();
			if(e.srcStmt().equals(startActivity)){											
				return e;
			}						
		}	
		return null;
	}	
	
	//���sm��ֱ��ǰ��
//		public static List<SootMethod> getDirectPre(SootMethod sm){
//			List<SootMethod> directPre = new ArrayList<SootMethod>();
//			Iterator<Edge> iterator = callGraph.edgesInto(sm);
//			while(iterator.hasNext()){
//				Edge next = iterator.next();
//				directPre.add(next.src());
//			}
//			return directPre;
//		}
	
	//���׶�2ʹ�ã�������
	public static List<Edge> getActivityEdges(SootMethod sm){
		List<Edge> edges = new ArrayList<Edge>();
		List<SootMethod> preMethods = new ArrayList<SootMethod>();
		if(!preMethods.contains(sm)){
			preMethods.add(sm);
			if(!isActivityMethod(sm)){											
				for(int i = 0;i<preMethods.size();i++){
					Iterator<Edge> it = callGraph.edgesInto(preMethods.get(i));
					while(it.hasNext()){
						Edge e = it.next();
						SootMethod src = (SootMethod)e.getSrc();
						//�����ǻ�
						if(!src.equals(e.tgt())){
							if(!src.getSignature().startsWith("<android")){
								if(!src.getName().equals("dummyMainMethod")){
									if(isActivityMethod(src)){
										if(!edges.contains(e)){
											edges.add(e);
											continue;
										}
									}
									else if(!preMethods.contains(src)){
										preMethods.add(src);							
									}
								}
							}
						}
					}
				}		
			}
		}
		return edges;
	}
	
//		String signature = sm.getSubSignature();		
//		if(signature.contains(AndroidEntryPointConstants.BROADCAST_ONRECEIVE)
//				||signature.contains(AndroidEntryPointConstants.ACTIVITY_ONRESUME)||
//				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCREATE)
//				||signature.contains(AndroidEntryPointConstants.ACTIVITY_ONSTART)||
//				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICK)||
//				signature.contains("onContextItemSelected")||
//				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICKDIALOG)||
//				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONITEMCLICK)
//				||signature.contains(AndroidEntryPointConstants.ACTIVITY_ONLISTITEMCLICK)||
//				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONOPTIONSITEMSELECTED)
//				||signature.contains("onMenuItemSelected")){
//			SootClass sc = sm.getDeclaringClass();
//			if(Entry.fakeActivities.contains(sc.getName())){
//				return true;
//			}
//		}
//		else if(signature.contains("void run()"))
//				return true;
//			return false;
//		}
}
