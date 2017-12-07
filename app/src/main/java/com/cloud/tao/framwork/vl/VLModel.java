package com.cloud.tao.framwork.vl;

public class VLModel
{
	private VLModelManager mModelManager;
	
	public VLModel()
	{
		//mModelManager = VLApplication.instance().getModelManager();
	}
	
	protected void onCreate()
	{
	}
	
	protected void onAfterCreate()
	{
	}
	
	protected <T> T getModel(Class<T> modelClass)
	{
		return mModelManager.getModel(modelClass);
	}
	

}
