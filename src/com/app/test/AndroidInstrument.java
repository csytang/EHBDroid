package com.app.test;
import soot.PackManager;
import soot.Transform;

public class AndroidInstrument{
	
	public void instrument(String[] args){
        transform(args);
	}
	
	public void transform(String[] args){
		//�ڲ��࣬�ȴ����ö��Ǽ�ʱ����
		PackManager.v().getPack("jtp").add(new Transform("jtp.myInstrumenter", new EventBodyTransformer()));
		soot.Main.main(args);
	}

}