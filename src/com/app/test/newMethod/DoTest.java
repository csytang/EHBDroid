////备份
//package com.app.test.newMethod;
//
//import java.util.List;
//
//import soot.BooleanType;
//import soot.IntType;
//import soot.Local;
//import soot.Modifier;
//import soot.Scene;
//import soot.SootClass;
//import soot.SootField;
//import soot.SootMethod;
//import soot.Trap;
//import soot.Value;
//import soot.VoidType;
//import soot.jimple.AssignStmt;
//import soot.jimple.GotoStmt;
//import soot.jimple.IdentityStmt;
//import soot.jimple.IfStmt;
//import soot.jimple.IntConstant;
//import soot.jimple.Jimple;
//import soot.jimple.ReturnVoidStmt;
//import soot.jimple.StringConstant;
//import soot.jimple.ThisRef;
//import com.app.test.MethodBuilder;
//
//public class DoTest extends MethodBuilder{
//
//	public static final String CLASSNAME = "doTest";
//	public static final String SUBSIGNATURE = "void doTest()";
//	
//	public DoTest(SootClass sc, String subSignature) {
//		super(sc, subSignature);
//	}
//
//	@Override
//	protected void addUnits() {
//		Local currentActivity,classLocal,classLocal1,exception,object,arrayObject, arrayObject2,string,string1,
//		view,linkedList,flag,int0,int1,int2,reflectMethod,arrayReflectMethod,toast, arrayClasses, 
//		int3, BooleanLocal,stringBuilder;
//		{
//			currentActivity = addLocal("activity", sc_Type);
//			object = addLocal("object", object_Type);
//			view = addLocal("view", view_Type);
//			toast = addLocal("toast", toast_Type);
//			int0 = addLocal("int0", IntType.v());
//			int1 = addLocal("int1", IntType.v());
//			int2 = addLocal("int2", IntType.v());
//			int3 = addLocal("int3", IntType.v());
//			flag = addLocal("flag", BooleanType.v());
//			classLocal = addLocal("classLocal", class_Type);
//			classLocal1 = addLocal("classLocal1", class_Type);
////			booleanClassLocal = addLocal("booleanClassLocal", class_Type);
//			reflectMethod = addLocal("reflectMethod", reflectMethod_Type);
//			linkedList = addLocal("linkedList", linkedList_Type);
//			string = addLocal("string", string_Type);
//			string1 = addLocal("string1", string_Type);
//			exception = addLocal("exception", exception_Type);
//			arrayObject = addLocalArray("arrayObject", object_Type);
//			arrayObject2 = addLocalArray("arrayObject2", object_Type);
//			arrayReflectMethod = addLocalArray("arrayReflectMethod", reflectMethod_Type);
//			arrayClasses = addLocalArray("arrayClasses", class_Type);
//			BooleanLocal = addLocal("BooleanLocal", Boolean_Type);
//			stringBuilder = addLocal("stringBuilder", stringBuilder_Type);
//		}
//		
//		SootField listenerLinkedList_field = sc.getFieldByName("listenerLinkedList");
//		SootField viewLinkedList_field = sc.getFieldByName("viewLinkedList");
//		SootField isVisited_field = sc.getFieldByName("isVisited");
//		SootMethod outPrint_method = sc.getMethod(OutPrint.SUBSIGNATURE);
//		addIdentityStmt(currentActivity, new ThisRef(sc_Type));
//		
//		//label0, 
//		AssignStmt label0 = Jimple.v().newAssignStmt(linkedList, Jimple.v().newStaticFieldRef(viewLinkedList_field.makeRef()));
//		//label1, 
//		AssignStmt label1 = Jimple.v().newAssignStmt(linkedList, Jimple.v().newStaticFieldRef(viewLinkedList_field.makeRef()));
//		//label2, 
//		AssignStmt label2 = Jimple.v().newAssignStmt(int1,Jimple.v().newLengthExpr(arrayReflectMethod));
//		//label3, 
//		AssignStmt label3 = Jimple.v().newAssignStmt(arrayObject,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(1)));
//		
//		//label5, 
//		IdentityStmt label5 = Jimple.v().newIdentityStmt(exception, Jimple.v().newCaughtExceptionRef());
//		//label6
//		AssignStmt label6 = Jimple.v().newAssignStmt(int0,Jimple.v().newAddExpr(int0, IntConstant.v(1)));
//		//label4, 
//		GotoStmt label4 = Jimple.v().newGotoStmt(label6);
//		//label7
//		ReturnVoidStmt label7  = Jimple.v().newReturnVoidStmt();
//		
//		addAssignStmt(Jimple.v().newStaticFieldRef(isVisited_field.makeRef()),IntConstant.v(1));
//		body.getUnits().add(label0);
//		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(linkedList, isEmpty_method.makeRef()));
//		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label1);
//		
//		List<Value> makeText_params = paramValues();
//		makeText_params.add(currentActivity);
//		makeText_params.add(StringConstant.v("Test finishes"));
//		makeText_params.add(IntConstant.v(0));
//		addAssignStmt(toast,Jimple.v().newStaticInvokeExpr(makeText_method.makeRef(),makeText_params));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(toast, show_method.makeRef()));
//		body.getUnits().add(label7);
//		//addReturnVoidStmt();
//		
//		body.getUnits().add(label1);
//		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
//		addAssignStmt(view, Jimple.v().newCastExpr(object, view_Type));
//		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(listenerLinkedList_field.makeRef()));
//		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
//		addAssignStmt(classLocal1, Jimple.v().newVirtualInvokeExpr(object,getClass_method.makeRef()));
//		addAssignStmt(arrayReflectMethod, Jimple.v().newVirtualInvokeExpr(classLocal1,getMethods_method.makeRef()));
//		addAssignStmt(int0, IntConstant.v(0));
//		
//		body.getUnits().add(label2);
//		addIfStmt(Jimple.v().newGeExpr(int0, int1), label0);
//		addAssignStmt(reflectMethod, Jimple.v().newArrayRef(arrayReflectMethod, int0));
//		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(reflectMethod, reflectMethodGetName_method.makeRef()));
//		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string,startsWith_method.makeRef(),StringConstant.v("on")));
//		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label6);
//		
//		//新添加的
//		{
//			body.getUnits().add(label3);
//			addAssignStmt(arrayObject2,Jimple.v().newNewArrayExpr(object_Type, IntConstant.v(2)));
//			addAssignStmt(arrayClasses, Jimple.v().newVirtualInvokeExpr(reflectMethod, getParameterTypes_method.makeRef()));
//			addAssignStmt(int3,Jimple.v().newLengthExpr(arrayClasses));
////			AssignStmt label9 = Jimple.v().newAssignStmt(int3,Jimple.v().newLengthExpr(arrayClasses));
//			IfStmt label10 = Jimple.v().newIfStmt(Jimple.v().newNeExpr(int3, IntConstant.v(2)), label6);
//			addIfStmt(Jimple.v().newNeExpr(int3, IntConstant.v(1)), label10);
//			
//			List<Value> outPrint_params = paramValues();
//			outPrint_params.add(reflectMethod);
//			outPrint_params.add(view);
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, outPrint_method.makeRef(),outPrint_params));
//			
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject,IntConstant.v(0)), view);
//			List<Value> invoke_params = paramValues();
//			invoke_params.add(object);
//			invoke_params.add(arrayObject);
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(reflectMethod, invoke_method.makeRef(),invoke_params));
//			addGotoStmt(label4);
//			body.getUnits().add(label10);
//			addAssignStmt(classLocal, Jimple.v().newArrayRef(arrayClasses, IntConstant.v(0)));
////			addAssignStmt(booleanClassLocal, ClassConstant.v("boolean"));
//			addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(classLocal, classGetName_method.makeRef()));
//			addAssignStmt(string1, StringConstant.v("boolean"));
//			addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(), string));
//			
//			AssignStmt label11 = Jimple.v().newAssignStmt(classLocal, Jimple.v().newArrayRef(arrayClasses, IntConstant.v(1)));
//			addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label11);
//			
//			addAssignStmt(BooleanLocal, Jimple.v().newStaticInvokeExpr(valueOf_method.makeRef(),IntConstant.v(1)));
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject2,IntConstant.v(0)), BooleanLocal);
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject2,IntConstant.v(1)), view);
//			List<Value> param2 = paramValues();
//			param2.add(object);
//			param2.add(arrayObject2);
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, outPrint_method.makeRef(),outPrint_params));
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(reflectMethod, invoke_method.makeRef(),param2));
//			addAssignStmt(BooleanLocal, Jimple.v().newStaticInvokeExpr(valueOf_method.makeRef(),IntConstant.v(0)));
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject2,IntConstant.v(0)), BooleanLocal);
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(reflectMethod, invoke_method.makeRef(),param2));
//			addGotoStmt(label4);
//			
//			body.getUnits().add(label11);
////			addAssignStmt(booleanClassLocal, ClassConstant.v("boolean"));
//			addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(classLocal, classGetName_method.makeRef()));
//			addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string1, stringEquals_method.makeRef(), string));
//			addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), label4);
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject2,IntConstant.v(0)), view);
//			addAssignStmt(BooleanLocal, Jimple.v().newStaticInvokeExpr(valueOf_method.makeRef(),IntConstant.v(1)));
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject2,IntConstant.v(1)),BooleanLocal);
//			List<Value> param3 = paramValues();
//			param3.add(object);
//			param3.add(arrayObject2);
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, outPrint_method.makeRef(),outPrint_params));
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(reflectMethod, invoke_method.makeRef(),param3));
//			addAssignStmt(BooleanLocal, Jimple.v().newStaticInvokeExpr(valueOf_method.makeRef(),IntConstant.v(0)));
//			addAssignStmt(Jimple.v().newArrayRef(arrayObject2,IntConstant.v(1)), BooleanLocal);
//			addInvokeStmt(Jimple.v().newVirtualInvokeExpr(reflectMethod, invoke_method.makeRef(),param3));
//			
//			body.getUnits().add(label4);
//		}
//		body.getUnits().add(label6);
//		addGotoStmt(label2);
//		body.getUnits().add(label5);
//		//addAssignStmt(classLocal, Jimple.v().newVirtualInvokeExpr(object,getClass_method.makeRef()));
//		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(classLocal1, classGetName_method.makeRef()));
//		addAssignStmt(int2, Jimple.v().newVirtualInvokeExpr(view, getId_method.makeRef()));
//		
//		addAssignStmt(stringBuilder, Jimple.v().newNewExpr(stringBuilder_Type));
//		addInvokeStmt(Jimple.v().newSpecialInvokeExpr(stringBuilder, stringBuilderInit_method.makeRef()));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v("Error happens in <EventHandler name: ")));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),string));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v(" View id: ")));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendInt_method.makeRef(),int2));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderAppendString_method.makeRef(),StringConstant.v(">")));
//		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(stringBuilder, stringBuilderToString_method.makeRef()));
//		List<Value> paramValues = paramValues();
//		paramValues.add(StringConstant.v("EVENT"));
//		paramValues.add(string);
//		addInvokeStmt(Jimple.v().newStaticInvokeExpr(logV_method.makeRef(),paramValues));
//		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(exception,printStackTrace_method.makeRef()));
//		addGotoStmt(label7);
//		
//		Trap trap = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Exception"), label3, label4, label5);
//		body.getTraps().add(trap);
//	}
//
//	@Override
//	protected void newMethodName() {
//		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
//	}
//}
//
//

package com.app.test.newMethod;

import java.util.List;

import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;


/**
 *  add doTest() method;
 *  <code>
 *  doTest(){<br>
 *  while(!viewLinkedlist.isEmpty()){
 *  	View v = viewLinkedList.poll();
 *  	Object o = listenerLinkedList.poll();
 *  	String registar = registarlinkedList.poll();
 *      Class class = o.getClass();
 *      Method[] methods = class.getMethods();
 *      List callbacks = map.get(registar);
 *      if(callbacks!=null){
 *      	Iterator it = callbacks.iterator();
 *      	while(it.hasNext()){
 *      		String callback = it.next();
 *      		for(int i=0;i<methods.length;i++){
 *      			if(callback.equals(method[i].getName)){
 *      				doViewAnalysis(method,view,object);
 *      			}
 *      		}
 *      	}
 *      }
 *  }
 *  }
 *  </code>
 *  
 * */
public class DoTest extends MethodBuilder{

	public static final String CLASSNAME = "doTest";
	public static final String SUBSIGNATURE = "void doTest()";
	
	public DoTest(SootClass sc, String subSignature) {
		super(sc, subSignature);
	}

	@Override
	protected void addUnits() {
		
		Local currentActivity,classLocal1,object,object1,mapObject,string,callBackString,
		view,linkedList,flag,int0,int1,reflectMethod,arrayReflectMethod,toast, map,list,
		iterator,iteratorViewLinkedList;
		{
			currentActivity = addLocal("activity", sc_Type);
			object = addLocal("object", object_Type);
			object1 = addLocal("object1", object_Type);
			mapObject = addLocal("mapObject", object_Type);
			view = addLocal("view", view_Type);
			toast = addLocal("toast", toast_Type);
			int0 = addLocal("int0", IntType.v());
			int1 = addLocal("int1", IntType.v());
			flag = addLocal("flag", BooleanType.v());
			classLocal1 = addLocal("classLocal1", class_Type);
			reflectMethod = addLocal("reflectMethod", reflectMethod_Type);
			linkedList = addLocal("linkedList", linkedList_Type);
			string = addLocal("string", string_Type);
			callBackString = addLocal("callBackString", string_Type);//String callback = iterator.next();
			arrayReflectMethod = addLocalArray("arrayReflectMethod", reflectMethod_Type);// Method[] methods;
			map = addLocal("map", map_Type);// map = registarToCallBacks;
			list = addLocal("list", list_Type);// list = map.get(key); callbacks
			iterator = addLocal("iterator", iterator_Type);// iterator = list.iterator()
			iteratorViewLinkedList = addLocal("iteratorViewLinkedList", iterator_Type); 
		}
		
		SootField listenerLinkedList_field = sc.getFieldByName(LISTENERLINKEDLIST);
		SootField viewLinkedList_field = sc.getFieldByName(VIEWLINKEDLIST);
		SootField isVisited_field = sc.getFieldByName(ISVISITED);
		SootField viewRegistarLinkedList_field = sc.getFieldByName(VIEWREGISTARLINKEDLIST);
		SootField viewToCallBack_field = Scene.v().getSootClass(CallBack).getFieldByName(VIEWTOCALLBACKS);
		SootMethod doViewAnalysis_method = sc.getMethod(DoViewAnalysis.SUBSIGNATURE);
		
		addIdentityStmt(currentActivity, new ThisRef(sc_Type));
		
		//label0, linkedList = viewLinkedList;
		AssignStmt isEmpty = Jimple.v().newAssignStmt(flag, Jimple.v().newInterfaceInvokeExpr(iteratorViewLinkedList, hasNext_method.makeRef()));
		
		//label1, linkedList = viewLinkedList;
		AssignStmt label1 = Jimple.v().newAssignStmt(linkedList, Jimple.v().newStaticFieldRef(viewLinkedList_field.makeRef()));
		
		//label2, int1 = arrayReflectMethod.length
		AssignStmt label2 = Jimple.v().newAssignStmt(int1,Jimple.v().newLengthExpr(arrayReflectMethod));

		//label6 int0 = int0+1
		AssignStmt iplusplus = Jimple.v().newAssignStmt(int0,Jimple.v().newAddExpr(int0, IntConstant.v(1)));
		//label7, return
		ReturnVoidStmt returnVoidStmt  = Jimple.v().newReturnVoidStmt();
		
		addAssignStmt(Jimple.v().newStaticFieldRef(isVisited_field.makeRef()),IntConstant.v(1));
		
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(viewLinkedList_field.makeRef()));
		
		addAssignStmt(iteratorViewLinkedList,Jimple.v().newInterfaceInvokeExpr(linkedList, iterator_method.makeRef()));
		body.getUnits().add(isEmpty);
		addIfStmt(Jimple.v().newNeExpr(flag, IntConstant.v(0)), label1);
		
		List<Value> makeText_params = paramValues();
		makeText_params.add(currentActivity);
		makeText_params.add(StringConstant.v("Test finishes"));
		makeText_params.add(IntConstant.v(0));
		addAssignStmt(toast,Jimple.v().newStaticInvokeExpr(makeText_method.makeRef(),makeText_params));
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(toast, show_method.makeRef()));
		//add ReturnVoidStmt
		body.getUnits().add(returnVoidStmt);
		
		// view = viewLinkedList.poll();
		body.getUnits().add(label1);// linkedList = viewLinkedList;
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
		addAssignStmt(view, Jimple.v().newCastExpr(object, view_Type));
		
		// string2 = viewRegistarLinkedList.poll();
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(viewRegistarLinkedList_field.makeRef()));
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
		addAssignStmt(callBackString, Jimple.v().newCastExpr(object, string_Type));
		
		// object = listenerLinkedList.poll();
		addAssignStmt(linkedList, Jimple.v().newStaticFieldRef(listenerLinkedList_field.makeRef()));
		addAssignStmt(object, Jimple.v().newVirtualInvokeExpr(linkedList,poll_method.makeRef()));
		
		// methods = object.getClass().getMethods();
		addAssignStmt(classLocal1, Jimple.v().newVirtualInvokeExpr(object,getClass_method.makeRef()));
		addAssignStmt(arrayReflectMethod, Jimple.v().newVirtualInvokeExpr(classLocal1,getMethods_method.makeRef()));
		
		// if(map.get(string2)==null) goto while (!viewLinkedList.isEmpty()) 
		addAssignStmt(map, Jimple.v().newStaticFieldRef(viewToCallBack_field.makeRef()));
		addAssignStmt(mapObject, Jimple.v().newInterfaceInvokeExpr(map, mapGet_method.makeRef(),callBackString));
		addAssignStmt(list, Jimple.v().newCastExpr(mapObject, list_Type));
		addIfStmt(Jimple.v().newEqExpr(list, NullConstant.v()), isEmpty);
		addAssignStmt(iterator, Jimple.v().newInterfaceInvokeExpr(list, iterator_method.makeRef()));
		
		//循环开始 iterator.hasNext()
		AssignStmt hasNext = Jimple.v().newAssignStmt(flag, Jimple.v().newInterfaceInvokeExpr(iterator, hasNext_method.makeRef()));
		body.getUnits().add(hasNext);
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), isEmpty);//跳出循环,viewLinkedList.isEmpty()
		addAssignStmt(object1, Jimple.v().newInterfaceInvokeExpr(iterator, next_method.makeRef()));
		addAssignStmt(callBackString, Jimple.v().newCastExpr(object1, string_Type));
		
		addAssignStmt(int0, IntConstant.v(0));
		body.getUnits().add(label2);//int1 = arrayReflectMethod.length
		addIfStmt(Jimple.v().newGeExpr(int0, int1), hasNext);//跳出循环, iterator.hasNext()
		
		addAssignStmt(reflectMethod, Jimple.v().newArrayRef(arrayReflectMethod, int0));
		addAssignStmt(string, Jimple.v().newVirtualInvokeExpr(reflectMethod, reflectMethodGetName_method.makeRef()));
		addAssignStmt(flag, Jimple.v().newVirtualInvokeExpr(string,stringEquals_method.makeRef(),callBackString));
		addIfStmt(Jimple.v().newEqExpr(flag, IntConstant.v(0)), iplusplus);//调出循环, i++
		List<Value> paramValues = paramValues();
		paramValues.add(reflectMethod);
		paramValues.add(view);
		paramValues.add(object);
		
		addInvokeStmt(Jimple.v().newVirtualInvokeExpr(currentActivity, doViewAnalysis_method.makeRef(),paramValues));
		
		List<Value> logParams = paramValues();
		logParams.add(view);
		logParams.add(object);
		logParams.add(string);
		addInvokeStmt(Jimple.v().newStaticInvokeExpr(utilLog_method.makeRef(),logParams));
		addGotoStmt(hasNext);//break;
		
		body.getUnits().add(iplusplus);//i++
		addGotoStmt(label2);////int1 = arrayReflectMethod.length
	}

	@Override
	protected void newMethodName() {
		currentMethod = new SootMethod(CLASSNAME, paramTypes(), VoidType.v(),Modifier.PUBLIC);
	}
}



