package sootAnalysis;

/*
 * һ���ࣺ
 * �������򣬾ֲ��������ϸ��塣
 * */

import em.EM;
import em.EMTriple;
import entryPointCreator.AndroidEntryPointConstants;
import graphTemp.TempConEdge;
import graphTemp.TempEdge;
import graphTemp.TempGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import singlton.Global;
import singlton.SingltonFactory;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.StringConstant;
import soot.tagkit.IntegerConstantValueTag;
import soot.util.Chain;
import tags.MethodTag;
import util.ClassAnalysis;
import view.ViewBuilder;
import view.ViewCondition;
import analysis.methodAnalysis.InterMethodAnalysis;
import analysis.methodAnalysis.IntraMethodAnalysis;

import com.aefg.intenttoactivity.ITS;
import com.aefg.intenttoactivity.ITSPair;
import com.android.event.EventContants;
import component.Component;
import component.ComponentHandler;

public class Instrumentor {
	
	private int count = 0;
	private final Logger logger = LoggerFactory.getLogger(getClass());   
	public Instrumentor(SingltonFactory.G g){}	
	public static Instrumentor v(){
		return SingltonFactory.v().getInstrumentor();
	}
	
	//��ʱ��
	public List<TempEdge> tempEdges = TempGraph.v().getTempEdges();

	//��ʼ����
	public void instrument(){				
		//TODO
		List<SootClass> buildAllClasses = resolveAllClasses(Scene.v().getClasses());	
		Global.v().getSootClasses().addAll(buildAllClasses);
		analyzeAllClasses(buildAllClasses);
		
		//test();
		ITS.v().genITS();
		System.out.println("ITS has been done! Size is: "+ITS.v().getITSPairs().size());
		EM.v().genEm();
		System.out.println("EM has been done! Size is: "+EM.v().getEMTriples().size());
		analyzeEM();
		logger.info("Instrumentor analysis finishes.",getClass());
	}
	
	public void test(){
		System.out.println("Start to do test");
		SootClass sootClass = Scene.v().getSootClass("com.google.android.gm.welcome.o");
		SootMethod method = sootClass.getMethodByName("<init>");
		System.out.println("��ʼ����:"+method+"\nֱ��ǰ����");
		for(SootMethod sm:InterMethodAnalysis.getSourcesMethods(method)){
			System.out.println(method+" sources: "+sm);
		}
		System.out.println("����ǰ��:");
		for(SootMethod sm:InterMethodAnalysis.getAllPreviousMethods(method)){
			System.out.println(method+" sources: "+sm);
		}
	
		SootClass sootClass2 = Scene.v().getSootClass("com.android.mail.ui.F");
		SootMethod method2 = sootClass2.getMethodByName("<init>");
		System.out.println("��ʼ����:"+method2+"\nֱ��ǰ����");
		for(SootMethod sm:InterMethodAnalysis.getSourcesMethods(method2)){
			System.out.println(method2+" sources: "+sm);
		}
		System.out.println("����ǰ��:");
		for(SootMethod sm:InterMethodAnalysis.getAllPreviousMethods(method2)){
			System.out.println(method2+" sources: "+sm);
		}
		SootClass sootClass3 = Scene.v().getSootClass("com.android.mail.ui.bb");
		SootMethod method3 = sootClass3.getMethod("void <init>(com.android.mail.ui.ConversationViewFragment,byte)");
		System.out.println("��ʼ����:"+method3+"\nֱ��ǰ����");
		for(SootMethod sm:InterMethodAnalysis.getSourcesMethods(method3)){
			System.out.println(method3+" sources: "+sm);
		}
		System.out.println("����ǰ��:");
		for(SootMethod sm:InterMethodAnalysis.getAllPreviousMethods(method3)){
			System.out.println(method3+" sources: "+sm);
		}
	}
	
	//����em
	public void analyzeEM(){
		List<EMTriple> emTriples = EM.v().getEMTriples();
		for(EMTriple em:emTriples){
			SootMethod sm = em.getO1();
			ITSPair itsPair = em.getO3();
			List<Value>	itsValues = new ArrayList<Value>();
			List<Value> emValues = new ArrayList<Value>();
			if(itsPair.hasTag()){
				itsValues = itsPair.getTag().getValues();
			}
			if(em.hasTag()){
				emValues = em.getTag().getValues();
			}
			emValues.addAll(itsValues);
			String activity = itsPair.getO1().getActivity();
			executePhaseOne(sm, emValues ,activity);
		}
	}
	
	/**
	 * ִ�н׶�1����������Ŀ�ģ�����ں����������ۡ�
	 * @param sootMethod ��һЩ��ں��������CallBack
	 * @param view ������Ϣ
	 * @param targetActivity Ŀ��
	 * �����ǣ�em�Ĵ�С
	 * */
	private void executePhaseOne(SootMethod sootMethod, List<Value> view, String targetActivity){	
		String signature = sootMethod.getSubSignature();
		System.out.println(count+++" "+sootMethod+" TargetNode: "+targetActivity);
		if(signature.contains(AndroidEntryPointConstants.BROADCAST_ONRECEIVE)
				||signature.contains("void run()")
				||signature.contains(AndroidEntryPointConstants.ACTIVITY_ONRESUME)){
			return;
		}
		else if (signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCREATE)
				||signature.contains(AndroidEntryPointConstants.ACTIVITY_ONSTART)) {
			buildTempGraphAuto(sootMethod,"onCreate",view,targetActivity);
			return;
		}
		else if(signature.contains(AndroidEntryPointConstants.ACTIVITY_ONITEMCLICK)
				||signature.contains(AndroidEntryPointConstants.ACTIVITY_ONLISTITEMCLICK)){
			buildTempGraphAuto(sootMethod,"ItemsList",view,targetActivity);
			return;
		}
		else if (signature.contains(AndroidEntryPointConstants.ACTIVITY_ONOPTIONSITEMSELECTED)||
				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONMENUITEMSELECTED)) {
			//TODO
			buildTempGraphForMenuItem(sootMethod,"OptionMenu",view,targetActivity);
			return;
		}
		else if(signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCREATECONTEXTMENU)||
				signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCONTEXTITEMSELECTED)){
			buildTempGraphForMenuItem(sootMethod,"ContextMenu",view,targetActivity);
			return;
		}
		else if(EventContants.getCallbackAndListener().containsKey(signature)){
			String listener = EventContants.getCallbackAndListener().get(signature);
			String listenerMethod = EventContants.getListenerAndListenerMethod().get(listener);
			deepAnalyze(sootMethod,view,targetActivity,listenerMethod);
		}
//		//done
//		else if (signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICK)) {		
//			deepAnalyze(sootMethod,view,targetActivity,AndroidEntryPointConstants.SETONCLICKLISTENER);
//			return;
//		}
//		else if (signature.contains("onPreferenceTreeClick")) {		
//			System.out.println("onPreferenceTreeClick happens!");
//			deepAnalyze(sootMethod,view,targetActivity,"setOnPreferenceClickListener");
//			return;
//		}
//		else if(signature.contains("onContextItemSelected")){
//			deepAnalyze(sootMethod,view,targetActivity,AndroidEntryPointConstants.SETONCONTEXTLISTENER);
//			return;
//		}
		//��ΪonClickDialogʱ��onClickDialog��Ӧ����showDialog�������Ҫ��showDialog���ڵķ���
		else if(signature.contains(AndroidEntryPointConstants.ACTIVITY_ONCLICKDIALOG)){					
			//TODO
			SootClass sc = sootMethod.getDeclaringClass();
			sc = ClassAnalysis.getOutClass(sc);
			for(SootMethod sm:sc.getMethods()){
				IntraMethodAnalysis ma = new IntraMethodAnalysis(sm);
				boolean b = ma.containsStmt("showDialog");
				if(b){				
					executePhaseOne(sm, view, targetActivity);
				}
			}
			return;
		}	
		else if(sootMethod.getDeclaringClass().getName().contains("$")){
			System.out.println(Instrumentor.class+" 318!!$$$$$$$$$$$");
			return;
		}		
		else {
			return;
		}
	}
	
	private void buildTempGraphAuto(SootMethod sootMethod, String invoker, List<Value> values, String targetActivity) {
		String view = ViewCondition.computeView(values);
		buildTempGraph(sootMethod, invoker, targetActivity, view);
	}
	
	private void buildTempGraphForMenuItem(SootMethod sootMethod, String invoker, List<Value> values, String targetActivity) {
		String view = ViewCondition.computeViewForMenuItem(values);
		buildTempGraph(sootMethod, invoker, targetActivity, view);
//		SootClass sootClass = sootMethod.getDeclaringClass();
//		Component component = Component.getComponent(sootClass);
//		String componentName = sootClass.getName();
//		StringConstant v = StringConstant.v(invoker+view);
//		TempConEdge tempConEdge = new TempConEdge(componentName,component,v);
//		TempEdge tempEdge = new TempEdge(componentName, targetActivity,tempConEdge);
//		TempGraph.v().getTempEdges().add(tempEdge);
	}
	
	private void buildTempGraph(SootMethod sootMethod, String invoker,
			String targetActivity, String view) {
		SootClass sootClass = sootMethod.getDeclaringClass();
		Component com = Component.parseComponent(sootClass);
		ComponentHandler ch = new ComponentHandler(sootClass);
		ch.build();
		String activity = ch.getActivity();
		Map<String, Component> map = ch.getMap();
		
		if(activity==""){
			System.err.println("Class: "+sootClass+" Type: "+com+" isn't attached to an Activity!");
			return;
		}
		//Component component = Component.getComponent(sootClass);
		//String componentName = sootClass.getName();
		StringConstant v = StringConstant.v(invoker+view);
		TempConEdge tempConEdge = new TempConEdge(map,v);
		TempEdge tempEdge = new TempEdge(activity, targetActivity,tempConEdge);
		TempGraph.v().getTempEdges().add(tempEdge);
	}
	
	private void deepAnalyze(SootMethod sootMethod,List<Value> values, String targetActivity, String listenerMethodName) {
		String view = ViewCondition.computeView(values);
		SootClass listener = sootMethod.getDeclaringClass();
		ViewBuilder viewBuilder = new ViewBuilder(listenerMethodName, listener);
		viewBuilder.build();
		Map<Integer, SootClass> idToClass = viewBuilder.getIdToClass();
		Map<AssignStmt, SootClass> viewToClass = viewBuilder.getViewToClass();
		Set<Integer> keySet = idToClass.keySet();
		Set<AssignStmt> keySet2 = viewToClass.keySet();
		System.out.println("Phase deep analyze: "+"SootMethod: "+sootMethod+" keySet: "+keySet.size());
		
		//��ʽ1
		for(AssignStmt v:keySet2){
			SootClass sootClass = viewToClass.get(v);
			Component com = Component.parseComponent(sootClass);
			System.out.println("Phase deep analyze: "+"value: "+v+" defineClass: "+sootClass);
			ComponentHandler componentHandler = new ComponentHandler(sootClass);
			componentHandler.build();
			Map<String, Component> map = componentHandler.getMap();
			String sourceActivity = componentHandler.getActivity();
			if(sourceActivity==""){
				System.err.println("Class: "+sootClass+" Type: "+com+" isn't attached to an Activity!");
				continue;
			}
			
			//int computeView = ViewBuilder.checkEquals(view, i+"");
			//if(computeView!=-1){
			StringConstant v2 = StringConstant.v(v+" "+view);
			TempConEdge tempConEdge = new TempConEdge(map, v2);
			TempEdge tempEdge = new TempEdge(sourceActivity, targetActivity,tempConEdge);
			TempGraph.v().getTempEdges().add(tempEdge);
		    //}
		}
		
		//��ʽ2
//		for(Integer i:keySet){
//			//System.out.println();
//			SootClass defineClass = idToClass.get(i);
//			Component com = Component.parseComponent(defineClass);
//			System.out.println("Phase deep analyze: "+"id: "+i+" defineClass: "+defineClass);
//			ComponentHandler componentHandler = new ComponentHandler(defineClass);
//			componentHandler.build();
//			//componentHandler.resolveMap(defineClass);
//			Map<String, Component> map = componentHandler.getMap();
//			String sourceActivity = componentHandler.getActivity();
//			if(sourceActivity==""){
//				System.err.println("Class: "+defineClass+" Type: "+com+" isn't attached to an Activity!");
//				continue;
//			}
//			
//			
//			
//			int computeView = ViewBuilder.checkEquals(view, i+"");
//			if(computeView!=-1){
//				TempConEdge tempConEdge = new TempConEdge(map, IntConstant.v(computeView));
//				TempEdge tempEdge = new TempEdge(sourceActivity, targetActivity,tempConEdge);
//				TempGraph.v().getTempEdges().add(tempEdge);
//			}
//		}
	}
	
	private void analyzeAllClasses(List<SootClass> sootClasses){
		for(SootClass sootClass:sootClasses){
			if(!sootClass.isInterface()){
//				for(SootMethod sm:sootClass.getMethods()){	
//					if(sm.isConcrete()){
//						MenuHandler.v().analyzeContentView(sm);
//					}
//				}
				for(SootMethod sm:sootClass.getMethods()){
					if(sm.isConcrete()){
						analyzeMethod(sm);
//						if(sm.getSignature().contains(AndroidEntryPointConstants.ACTIVITY_ONCREATEOPTIONSMENU)){
//							MenuHandler.v().retriveMenuItem(sm, MenuType.optionMenu);
//						}
//						else if(sm.getSignature().contains(AndroidEntryPointConstants.ACTIVITY_ONCREATECONTEXTMENU)){
//							MenuHandler.v().retriveMenuItem(sm, MenuType.contextMenu);
//						}
					}
				}
			}
		}
	}
	
	//һ��Methodֻ����һ��startActivity, ���ж��intent�ڴ˶��壬��Ĭ�϶��ᱻstartActivity����
	private void analyzeMethod(SootMethod sootMethod){
		List<InvokeStmt> allStartActivities = Global.v().getAllStartActivities();
		Body body = sootMethod.retrieveActiveBody();
		Chain<Unit> chain = body.getUnits();
		//��ÿ�������������
		int index = 0;
		for(Unit unit:chain){			
			index++;
			if(unit instanceof InvokeStmt){
				InvokeStmt s = (InvokeStmt)unit;
				InvokeExpr ie = s.getInvokeExpr();
				if((ie.getMethod().getSubSignature().equals("void startActivity(android.content.Intent)")||
						ie.getMethod().getSubSignature().equals("void startActivityForResult(android.content.Intent,int)"))
						&&ie.getMethod().getDeclaringClass().getName().startsWith("android")){
					s.addTag(new MethodTag(sootMethod));
					s.addTag(new IntegerConstantValueTag(index));
					allStartActivities.add(s);	
					break;
				}
			}		
		}
	}   
	
	private List<SootClass> resolveAllClasses(Chain<SootClass> ch){	
		//�޸ĳɱ������е���
		List<SootClass> allClasses = new ArrayList<SootClass>();
		for(SootClass s:ch){
			if(s.isConcrete()){
				if(!s.getName().startsWith("android")&&!s.getName().startsWith("java")&&
						!s.getName().startsWith("org")){
					allClasses.add(s);
				}			
			}
		}
		return allClasses;
	}			
}
