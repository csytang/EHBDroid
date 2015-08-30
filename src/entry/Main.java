package entry;

import graph.ActivityEventFlowGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.app.test.CallBack;

import singlton.Global;
import singlton.SingltonFactory;
import soot.SootClass;
import sootAnalysis.Instrumentor;
import test.Event;
import test.EventExplorer;
import test.Events;
import test.Graph;
import util.StringHandler;

public class Main {
	public static String appName;
	public static String desria = "/mnt/sdcard/";
	public static void main(String[] args) throws Exception {
		Logger log = LoggerFactory.getLogger(Main.class);
		long start = System.currentTimeMillis();

		String defaultDir = "output";
		String apkFileLocation = args[1];
		appName = apkFileLocation.substring(
				apkFileLocation.indexOf("/") + 1, apkFileLocation.indexOf("."));
		String seriDir = defaultDir + "/" + appName + ".txt";
		String edges = defaultDir + "/" + appName + "Edges.txt";
		String eventsDir = defaultDir + "/" + appName + "Events.txt";
		String eventDir = defaultDir + "/" + appName + "Event.txt";

		Entry entry = new Entry(apkFileLocation, args[3]);
		soot.G.reset();
		entry.initSoot();

		// Set<String> activityClasses = entry.getActivityClasses();
		//
		// List<ActivityAndFilter> filters = IntentFilter.getFilters();
		// for(ActivityAndFilter aad:filters){
		// System.out.println(aad.getActivity()+" "+aad.getmFliters());
		// }

		Instrumentor ins = SingltonFactory.v().getInstrumentor();
		ins.instrument();
		doTest();
		// Events.v().getEvents();Fi
		serializationEvents(Events.v(), eventsDir);
		deserializationEvents(eventsDir, eventDir);

		// List<TempEdge> tempEdges = TempGraph.v().getTempEdges();
		// System.out.println("ת��ǰ: GraphStorage�Ĵ�С��  "+tempEdges.size());
		// System.err.println("GraphStorage: ");
		// for(TempEdge te:tempEdges){
		// System.out.println("+++"+te.toString());
		// }
		// System.err.println("GraphStorage Finishes");
		// List<TempEdge> transform = Entrying.transform(tempEdges,
		// activityClasses);
		// new ActivityEventFlowGraphBuilder(transform).build();
		// ActivityEventFlowGraph graph = ActivityEventFlowGraph.v();
		// graph.build();
		// System.out.println("ͼ�Ľڵ�����: "+graph.getNodes().size());
		// System.out.println("ͼ�ı�����: "+graph.getEdges().size());
		// serializationAEFG(graph,seriDir);
		// deserializationAEFG(seriDir,edges);
		//DrawGraph.drawGraph(graph,defaultDir,args[5],appName);
		long end = System.currentTimeMillis();

		log.info("RunningTime: " + (end - start) + "ms", Main.class);

		// ���Կ�ʼ
		// System.out.println("------------------------------------\n------------------------------------");
		// log.info("Starting to generate testing case.",Main.class);
		// test.Main m = new test.Main();
		// //m.genTestCase(g);
		//
	}

	private static void serializationObject(Object g, String edgeStorageLocation) {
		File file = new File(edgeStorageLocation);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// Graph���л�
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			Class c = CallBack.class;
			oos.writeObject(g);
			oos.flush();
			oos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void serializationAEFG(ActivityEventFlowGraph g,
			String edgeStorageLocation) {
		serializationObject(g, edgeStorageLocation);
	}

	public static void serializationEvents(Events events, String eventDir) {
		serializationObject(events, eventDir);
	}

	private static void deserializationAEFG(String app, String edges)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ActivityEventFlowGraph aeg;
		FileInputStream fis = new FileInputStream(app);
		ObjectInputStream ois = new ObjectInputStream(fis);
		aeg = (ActivityEventFlowGraph) ois.readObject();
		ois.close();
		fis.close();

		List<List<String>> ssList = aeg.getStrEdges();
		Graph g = new Graph(ssList);
		FileOutputStream out = null;
		File file2 = new File(edges);
		try {
			file2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = new FileOutputStream(file2);
		int count = ssList.size();
		for (int i = 0; i < count; i++) {
			List<String> strings = ssList.get(i);
			out.write((i + " src: " + strings.get(0) + " tgt: "
					+ strings.get(1) + " \n" + "view: " + strings.get(2) + "\n")
					.getBytes());
		}
		out.close();
	}

	public static void deserializationEvents(String eventsDir, String eventDir)
			throws IOException, ClassNotFoundException {
		Events events;
		FileInputStream fis = new FileInputStream(eventsDir);
		ObjectInputStream ois = new ObjectInputStream(fis);
		events = (Events) ois.readObject();
		ois.close();
		fis.close();

		File file2 = new File(eventDir);
		file2.createNewFile();
		FileOutputStream out = new FileOutputStream(file2);
		for (Event e : events.getEvents()) {
			String activity = e.getActivity();
			String eventType = e.getEventType();
			String view = e.getView();
			int viewId = StringHandler.splitNumsForViews(view);
			if ((activity != null || activity != "") && eventType != ""
					&& viewId != -1)
				out.write(("('" + activity + "','" + viewId + "','" + eventType + "')\n")
						.getBytes());
		}
		out.close();
	}

	public static void doTest() {
		List<SootClass> sootClasses = Global.v().getSootClasses();
		System.out.println("start to analyze EventExplorer!!!");
		EventExplorer eventExplorer = new EventExplorer(sootClasses);
		eventExplorer.instrument();
		System.out.println("EventExplorer stops!!!");
	}

}
