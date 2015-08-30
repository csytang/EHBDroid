package com.aefg.dataflowanalysis;




import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.app.test.AppDir;


//s instanceof String = s.getClass().getName.equals("java.lang.String")

public class Test {
	Test t;
	public static String name;
	
	static{
		Test test = new Test();
		test.isValid();
	}
	
	public static int[] i = new int[100];
	
	public static List<String> strings = new ArrayList<String>();
	
	public static void isValid(boolean flag, int t, String c,long l){
		System.out.println("hah");
	}
	public static boolean isValid(){
		return false;
	}
	
	public static String splitWord(String s,String orignal){
		String result = "";
		if(isWord(s)){
			result = s;
			String t = orignal.substring(result.toCharArray().length);
			result = result+" "+splitWord(t, t);
			return result;
		}
		else{
			if(s.toCharArray().length>0){
				s = s.substring(0, s.toCharArray().length-1);
				result = splitWord(s,orignal);
			}
		}
		return result;
	}
	
	public static boolean isWord(String s){
		String[] strings = {"I","am","dog","cat"};
		if(Arrays.asList(strings).contains(s)){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(splitWord("IdogamIcat","IdogamIcat"));
		//System.out.println(isWord("I"));
		double d = 7154*0.0493;
		System.out.println(d);
		
//		Object object = 1;
//		int j = 10;
//		Object object2 = j;
//		Integer i = (Integer)object;
//		i.intValue();
//		
//		String hString = name;
//		hString = "qian";
//		System.out.println(name);
//		LinkedList<Integer> clone = (LinkedList<Integer>) AppDir.linkedList.clone();
//		
//		clone.set(9, 1);
//		System.out.println(AppDir.linkedList.get(9));
//		
//		Runnable runnable = new Runnable() {
//			
//			@Override
//			public void run() {
//				while (true) {
//					try {
//						Thread.sleep(1/2);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		};
//		Thread thread = new Thread(runnable);
//		thread.start();
//		
//		System.out.println("Service");
//		for(String key:CallBack.serviceToCallBacks.keySet()){
//			for(String value:CallBack.serviceToCallBacks.get(key)){
//				System.out.println(value);
//			}
//		}
//		
//		System.out.println("View");
//		for(String key:CallBack.viewToCallBacks.keySet()){
//			for(String value:CallBack.viewToCallBacks.get(key)){
//				System.out.println(value);
//			}
//		}
//		
//		System.out.println("Dialog");
//		for(String key:CallBack.dialogToCallBacks.keySet()){
//			for(String value:CallBack.dialogToCallBacks.get(key)){
//				System.out.println(value);
//			}
//		}
		
//		Object[] objects = {1,1l};
//		
//		Value v = NullConstant.v();
//		if(v==NullConstant.v()){
//			System.out.println(42);
//		}
//		
//		List<String> list = new ArrayList<String>();
//		Object object = "qia";
//		String string = (String)object;
//		list.add(string);
//		
//		
//		LinkedList<String> strings = new LinkedList<String>();
//		strings.add("qian1");
//		strings.add("qian2");
//		
//		System.out.println(strings.pop());
//		System.out.println(strings.pop());
//		System.out.println(strings);
//		
//		
////		System.out.println("qian");
////		Thread thread = new Thread(){
////			public void run() {
////				System.out.println("xiangxing");
////			};
////		};
////		thread.start();
//		
//		try {
//			Class<?> forName = Class.forName("analysis.Test");
//			Class<?>[] list2 = {boolean.class,int.class,String.class,long.class};
//			try {
//				Method method = forName.getMethod("isValid", list2);
//				Class<?>[] parameterTypes2 = method.getParameterTypes();
//				
//				Object[] objects2 = {true,1,"qian",1l};
//				try {
//					method.invoke(forName, objects2 );
//				} catch (IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IllegalArgumentException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvocationTargetException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			} catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SecurityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		System.out.println("waiting...");
//		LinkedList<String> linkedList = new LinkedList<String>();
//		linkedList.offer("1");
//		linkedList.offer("2");
//		linkedList.offer("3");
//		linkedList.offer("4");
//		
//		while(!linkedList.isEmpty()){
//			String peek = linkedList.poll();
//			System.out.println(peek);
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		if(linkedList.isEmpty()){
//			System.out.println("Finished!");
//		}
		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("I am back");
		
		
//		try {
//			Class<?> forName = Class.forName("analysis.Test");
////			Class<?>[] list = new ArrayList<Class>();
////			
////			list.add(boolean.class);
////			list.add(int.class);
////			list.add(String.class);
//			
//			Class<?> forName2 = Class.forName("java.lang.Boolean");
//			for(Method m:forName2.getMethods()){
//				System.out.println(m.getName());
//			}
//			Class<?>[] list = {boolean.class,int.class,String.class};
//			Method method = forName.getMethod("isValid", list);
//			System.out.println(method.getName());
//			for(Method m:forName.getMethods()){
//				if(m.getName().equals("isValid")){
//					System.out.println(m.getParameterTypes()[0].equals(boolean.class));
//				}
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String li = "setOn[A-Za-z]+Listener";
//		name = "setOnClickListener";
//		System.out.println(name.matches(li));
//		
//		isValid(null)
		
		
//		strings.add("123");
//		String name2 = Test.class.getName();
//		try {
//			Field field = Class.forName(name2).getField("name");
//			Field stringsField =  Class.forName(name2).getField("strings");
//			Field modifiersField = Field.class.getDeclaredField("modifiers");  
//			//modifiersField.setAccessible(true);  
//			System.out.println(stringsField.get(null));
//			List<String> string  = (List)stringsField.get(null);
//			string.add("qian");
//			System.out.println(stringsField.get(null));
//			stringsField.set(null, string);  
//			
//			field.set(null, "xiangxing"); 
//			//System.out.println(stringsField.get(null));
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(name);
//		System.out.println(strings);
//		int t = 10;
//		for(int i = 0;i<10;i++){
//			if(i%2==0){
//				if(t>0){
//					continue;
//				}
//			}
//			System.out.println(i);
//		}
		
		//测试sysout跟syserr的区别
		//测试结果：两者的顺序会以任何形式输出。
//		System.out.println("Debugging   Info.");
//		System.err.println("Debugging   Info.");
//		
//		//测试StringHandler
//		Integer id = StringHandler.splitNumsForViews("$r7 = virtualinvoke $r4.<android.view.LayoutInflater: android.view.View inflate(int,android.view.ViewGroup)>(2130968693, null);");
//		System.out.println(id);
		//测试运算符的优先级: == 与 +
//		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
//		List<String> list = map.get("qian");
//		System.out.println("qian: "+(list==null));
//		//Test t = new Test();
//		System.out.println(TypeAndFormat.getTypes());
		
		//联系正则表达式
//		String a="id:virtualinvoke $r0.<com.zy.sharpeye.main.InstructionsActivity: android.view.View findViewById(int)>(2131230726)";
//		Pattern p = Pattern.compile("(\\d+)");      
//		Matcher m = p.matcher(a);  
//		List<Integer> integers = new ArrayList<Integer>();
//		while(m.find()){
//			String group = m.group();
//			int i = Integer.valueOf(group);
//			if(i>10000000)
//				integers.add(Integer.valueOf(group));
//		}
	}
//	// Make sure we get line numbers and whole program analysis
//	static {
//		soot.options.Options.v().set_keep_line_number(true);
//		soot.options.Options.v().set_whole_program(true);
//		Options.v().set_soot_classpath("D://Sootlib/soot.jar;" +
//			"D:/Sootlib/jce.jar;"+
//			"D:/Sootlib/rt.jar;D:/Sootlib/jasminclasses-2.3.0.jar;" +
//			"D:/Sootlib/polyglotclasses-1.3.5.jar;"+
//			"D:/WorkSpace/Administrator/workspace/PointsTo/bin"
//			);
//	}
//	
//	private static SootClass loadClass(String name, boolean main) {
//		SootClass c = Scene.v().loadClassAndSupport(name);
//		c.setApplicationClass();
//		if (main) Scene.v().setMainClass(c);
//		return c;
//	}
//	
//	public void doAnalysis(SootMethod analyzeMethod){
//		
//		AEFGLocalInterproceduralAnalysis analysis = new AEFGLocalInterproceduralAnalysis(analyzeMethod, TypeDefinition.getTypes());
//		Context<SootMethod, Unit, Map<Local, List<Value>>> doInterprocedure = analysis.doInterprocedure(analyzeMethod,  new HashMap<Local, List<Value>>());
//		//DataFlowSolution<Unit, Map<Local, List<Value>>> solution = analysis.getMeetOverValidPathsSolution();
//		//Set<SootMethod> methods = analysis.getMethods();
//		System.out.println("----------------------------------------------------------------");
//		for (Unit unit : analyzeMethod.getActiveBody().getUnits()) {
//			System.out.println("----------------------------------------------------------------");
//			System.out.println(unit);
//			System.out.println("IN:  " + formatConstants(doInterprocedure.getValueBefore(unit)));
//			System.out.println("OUT: " + formatConstants(doInterprocedure.getValueAfter(unit)));
//		}	
//	}
//	
//	public static String formatConstants(Map<Local, List<Value>> value) {
//		if (value == null) {
//			return "";
//		}
//		StringBuffer sb = new StringBuffer();
//		for (Entry<Local, List<Value>> entry : value.entrySet()) {
//			Local local = entry.getKey();
//			List<Value> constant = entry.getValue();
//			if (constant != null) {
//				sb.append("(").append(local).append("=").append(constant).append(") ");
//			}
//		}
//		return sb.toString();
//	}
//		String string = "\"1\"";
//		
//		String string2 = "nmn";
//		String[] split = string2.split("n");
//		System.out.println(split[0]+split[1]);
//		
////		StringConstant v = StringConstant.v("android.intent.action.VIEW");
////		String string2 = v.toString();
////		
////		String substring = string2.substring(string2.indexOf("\""), string2.lastIndexOf("\""));
////		
////		System.out.println(substring);
////		
////		System.out.println(v.toString().split("\"")[1]);
//		
//		Pattern pattern = Pattern.compile("^\\s*<(.*?):\\s*(.*?)>\\s*$");
//		Matcher matcher = pattern.matcher("<android:q()>");
//		
//		 if(matcher.find()){
//	        	String className = matcher.group(1);
//	        	System.out.println(className);
//	        	String params = "";
//				if(subSignature)
//					params = matcher.group(2);
//				else
//					params = parseString;
//				
//				if(result.containsKey(className))
//					result.get(className).add(params);
//				else {
//					Set<String> methodList = new HashSet<String>(); 
//					methodList.add(params);
//					result.put(className, methodList);
//				}
	        
		
//		loadClass("Analysis.Item",false);
//		loadClass("Analysis.Container",false);
//		SootClass c = loadClass("Analysis.Test1",true);
//		soot.Scene.v().loadNecessaryClasses();
//		soot.Scene.v().setEntryPoints(EntryPoints.v().all());
//
//		SootMethod methodByName = c.getMethodByName("main");
//		CHATransformer.v().transform();
//		
//		Test t = new Test();
//		t.doAnalysis(methodByName);
//			
//		String string = "qainan";
//		char[] ch = string.toCharArray();
//		List<Character> cList = new ArrayList<Character>();
//		StringBuilder sb = new StringBuilder();
//		for(int i=0;i<ch.length;i++){
//			if(!cList.contains(ch[i])){
//				cList.add(ch[i]);
//				sb.append(ch[i]);
//			}
//		}
//		System.out.println(sb.toString());
	
	public static HashMap<String, Set<String>> parseClassNames(List<String> methods, boolean subSignature){
		HashMap<String, Set<String>> result = new HashMap<String,  Set<String>>();
		Pattern pattern = Pattern.compile("^\\s*<(.*?):\\s*(.*?)>\\s*$");
		for(String parseString : methods){
			//parse className:
			String className = "";
	        Matcher matcher = pattern.matcher(parseString);
	        if(matcher.find()){
	        	className = matcher.group(1);
	        	String params = "";
				if(subSignature)
					params = matcher.group(2);
				else
					params = parseString;
				
				if(result.containsKey(className))
					result.get(className).add(params);
				else {
					Set<String> methodList = new HashSet<String>(); 
					methodList.add(params);
					result.put(className, methodList);
				}
	        }
		}
		return result;
	}

}