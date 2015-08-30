package em;

import java.util.ArrayList;
import java.util.List;

import singlton.SingltonFactory;
import soot.SootMethod;
import soot.jimple.InvokeStmt;
import tags.Tagger;

import com.aefg.intenttoactivity.ITS;
import com.aefg.intenttoactivity.ITSPair;
import com.aefg.intenttoactivity.StartActivity;

public class EM {
	public EM(singlton.SingltonFactory.G g){}
	public static EM v(){
		return SingltonFactory.v().getEM();
	}
	
	//��EMResolver�и�ֵ
	private List<EMTriple> emTriples = new ArrayList<EMTriple>();
	public  List<EMTriple> getEMTriples(){
		return emTriples;
	}
	
	public void genEm(){
		List<ITSPair> itsPairs = ITS.v().getITSPairs();
		for(ITSPair itsPair:itsPairs){
			StartActivity o2 = itsPair.getO2();
			InvokeStmt is = o2.getValue();
			SootMethod sm = Tagger.getMethodTag(is);
			
			if(sm.getName().equals("startActivity")){
				//�޳����е�startActivity���
				System.out.println(this.getClass()+"  �޳�StartActivity: "+sm);
				continue;
			}
			new EMBuilder(sm).build(itsPair);
		}
	}
}
